package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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

    TableLayout suretyMembersTable;
    TableLayout availableMembersTable;

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

        suretyMembersTable = findViewById(R.id.suretyMembersTable);
        availableMembersTable = findViewById(R.id.availableSuretyMembersTable);

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
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try{
                JSONObject tempObject = (JSONObject) jsonArray.get(0);

                JSONArray members = new JSONArray(tempObject.getString("registered_members"));
                JSONArray availableMembers = new JSONArray(tempObject.getString("available_members"));

                if(members.length() > 0){
                    JSONObject jsonObject = (JSONObject) members.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    suretyMembersTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < members.length(); i++) {
                    JSONObject jsonObject = (JSONObject) members.get(i);

                    if(i%2 != 0) {
                        suretyMembersTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        suretyMembersTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

                if(availableMembers.length() > 0){
                    JSONObject jsonObject = (JSONObject) availableMembers.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    availableMembersTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < availableMembers.length(); i++) {
                    JSONObject jsonObject = (JSONObject) availableMembers.get(i);

                    if(i%2 != 0) {
                        availableMembersTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        availableMembersTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

            }catch(JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON format invalid",Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
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