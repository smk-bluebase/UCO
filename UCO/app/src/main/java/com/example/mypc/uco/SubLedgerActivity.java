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
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

public class SubLedgerActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView memberNameTextView;
    TextView memberNoTextView;

    String title;
    String memberName;
    String memberNo;

    ProgressDialog progressDialog;

    Context context = this;
    private String urlSubLedger = CommonUtils.IP + "/UCO/ucoandroid/sub_ledger.php";

    JsonObject jsonObject;
    JsonObject jsonData = new JsonObject();

    String jsonString;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_ledger);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        memberNo = intent.getStringExtra("memberNo");
        memberName = intent.getStringExtra("memberName");
        jsonString = intent.getStringExtra("jsonData");

        JsonParser parser = new JsonParser();
        jsonData = (JsonObject) parser.parse(jsonString);

        progressDialog = new ProgressDialog(this);

        titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);

        memberNameTextView = findViewById(R.id.memberName);
        memberNameTextView.setText("Member Name : " + memberName);

        memberNoTextView = findViewById(R.id.memberNo);
        memberNoTextView.setText("Member No : " + memberNo);

        tableLayout = findViewById(R.id.displayTable);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);
        jsonObject.addProperty("title", title);

        PostSubLedger postSubLedger = new PostSubLedger(this);
        postSubLedger.checkServerAvailability(2);
    }

    private class PostSubLedger extends PostRequest {
        public PostSubLedger(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlSubLedger, jsonObject);
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

                    tableLayout.addView(createTableRow(Color.WHITE, getResources().getColor(R.color.c1), tableHeader));
                }

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if(i%2 != 0) {
                        tableLayout.addView(createTableRow(Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject));
                    }else {
                        tableLayout.addView(createTableRow(Color.BLACK, Color.WHITE, jsonObject));
                    }
                }
            }catch(JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON format invalid",Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }

    public TableRow createTableRow(int textColor, int backgroundColor, JSONObject jsonObject){
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tableRow.setPadding(1, 1, 1, 1);

        try {
            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()) {
                String key = keys.next();

                if(key.equals("date")){
                    try {
                        Date oldDate = new SimpleDateFormat("yyyy-mm-dd").parse(jsonObject.get(key).toString());
                        String newDate = new SimpleDateFormat("dd-mm-yyyy").format(oldDate);
                        tableRow.addView(createTextView(textColor, backgroundColor, newDate, true));
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                }else {
                    if(keys.hasNext()) {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), true));
                    }else {
                        tableRow.addView(createTextView(textColor, backgroundColor, jsonObject.get(key).toString(), false));
                    }
                }
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        return tableRow;
    }

    public TextView createTextView(int textColor, int backgroundColor, String text, boolean isMarginRight){
        TextView textView = new TextView(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.2f);
        if(isMarginRight) params.setMargins(0, 0, 5, 5);
        else params.setMargins(0, 0, 0, 5);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(backgroundColor);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5, 5, 5, 5);
        textView.setText(text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        textView.setTextSize(10);
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
                Intent intent1 = new Intent(SubLedgerActivity.this, LedgerActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isSubLedger", "true");
                intent1.putExtra("jsonData", jsonData.toString());
                startActivity(intent1);
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(SubLedgerActivity.this, SubLedgerActivity.class);
                intent2.putExtra("memberNo", memberNo);
                intent2.putExtra("memberName", memberName);
                intent2.putExtra("title", title);
                intent2.putExtra("jsonData", jsonData.toString());
                startActivity(intent2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(SubLedgerActivity.this, LedgerActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        startActivity(i);
    }
}