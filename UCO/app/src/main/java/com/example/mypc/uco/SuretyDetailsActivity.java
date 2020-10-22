package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

public class SuretyDetailsActivity extends AppCompatActivity {
    TextView memberNameTextView;
    TextView memberNoTextView;

    String memberNo;
    String memberName;
    String urlSuretyDetails = CommonUtils.IP + "/UCO/ucoandroid/surety_details.php";

    JsonObject jsonObject;
    ProgressDialog progressDialog;
    Context context = this;

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surety_details);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent = getIntent();
        memberNo = intent.getStringExtra("memberNo");
        memberName = intent.getStringExtra("memberName");

        memberNameTextView = findViewById(R.id.memberName);
        memberNameTextView.setText("Member Name : " + memberName);

        memberNoTextView = findViewById(R.id.memberNo);
        memberNoTextView.setText("Member No : " + memberNo);

        tableLayout = findViewById(R.id.displayTable);

        if(!memberNo.isEmpty()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jsonObject = new JsonObject();
            jsonObject.addProperty("memberNo", memberNo);

            PostSuretyDetails postSuretyDetails = new PostSuretyDetails(this);
            postSuretyDetails.checkServerAvailability(2);
        }

    }

    private class PostSuretyDetails extends PostRequest{
        public PostSuretyDetails(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlSuretyDetails, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try{
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    tableLayout.addView(createTableRow(Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true));
                }

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if(i%2 != 0) {
                        tableLayout.addView(createTableRow(Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false));
                    }else {
                        tableLayout.addView(createTableRow(Color.BLACK, Color.WHITE, jsonObject, false));
                    }
                }
            }catch(JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON format invalid",Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }

    public TableRow createTableRow(int textColor, int backgroundColor, JSONObject jsonObject, boolean isHeader){
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tableRow.setPadding(1, 1, 1, 1);

        try {
            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()) {
                String key = keys.next();

                if(jsonObject.get(key).toString().isEmpty()) continue;

                if(!isHeader) {
                    if (keys.hasNext()) {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), true, Gravity.LEFT));
                    } else {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), false, Gravity.LEFT));
                    }
                }else {
                    if (keys.hasNext()) {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), true, Gravity.CENTER));
                    } else {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), false, Gravity.CENTER));
                    }
                }

            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        return tableRow;
    }

    public TextView createTextView(int textColor, int backgroundColor, String text, boolean isMarginRight, int orientation){
        TextView textView = new TextView(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.2f);
        if(isMarginRight) params.setMargins(0, 0, 5, 5);
        else params.setMargins(0, 0, 0, 5);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(backgroundColor);
        textView.setGravity(orientation);
        textView.setPadding(5, 5, 5, 5);
        textView.setText(text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        textView.setTextSize(8);
        textView.setTextColor(textColor);
        return textView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.reconnect_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1 = new Intent(SuretyDetailsActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(SuretyDetailsActivity.this, SuretyDetailsActivity.class);
                intent2.putExtra("memberNo", memberNo);
                intent2.putExtra("memberName", memberName);
                startActivity(intent2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SuretyDetailsActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}