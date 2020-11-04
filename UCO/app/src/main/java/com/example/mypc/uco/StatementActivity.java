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

public class StatementActivity extends AppCompatActivity {
    Context context = this;
    String memberNo;
    String memberName;

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    String urlStatement = CommonUtils.IP + "/UCO/ucoandroid/get_statement.php";

    TableLayout srfTable;
    TableLayout thriftTable;
    TableLayout suretyTable;
    TableLayout festivalTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

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

        TextView memberNameTextView = findViewById(R.id.memberName);
        memberNameTextView.setText("Member Name : " + memberName);

        TextView memberNoTextView = findViewById(R.id.memberNo);
        memberNoTextView.setText("Member No : " + memberNo);

        srfTable = findViewById(R.id.srfTable);
        thriftTable = findViewById(R.id.thriftTable);
        suretyTable = findViewById(R.id.suretyTable);
        festivalTable = findViewById(R.id.festivalTable);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);

        PostStatement postStatement = new PostStatement(context);
        postStatement.checkServerAvailability(2);
    }

    private class PostStatement extends PostRequest{
        public PostStatement(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlStatement, jsonObject);
            }else{
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try{
                JSONObject tempObject = (JSONObject) jsonArray.get(0);

                JSONArray srf = new JSONArray(tempObject.getString("srf"));
                JSONArray thrift = new JSONArray(tempObject.getString("thrift"));
                JSONArray surety = new JSONArray(tempObject.getString("surety"));
                JSONArray festival = new JSONArray(tempObject.getString("festival"));

                if(srf.length() > 0){
                    JSONObject jsonObject = (JSONObject) srf.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    srfTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < srf.length(); i++) {
                    JSONObject jsonObject = (JSONObject) srf.get(i);

                    if(i%2 != 0) {
                        srfTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        srfTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

                if(thrift.length() > 0){
                    JSONObject jsonObject = (JSONObject) thrift.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    thriftTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < thrift.length(); i++) {
                    JSONObject jsonObject = (JSONObject) thrift.get(i);

                    if(i%2 != 0) {
                        thriftTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        thriftTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

                if(surety.length() > 0){
                    JSONObject jsonObject = (JSONObject) surety.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    suretyTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < surety.length(); i++) {
                    JSONObject jsonObject = (JSONObject) surety.get(i);

                    if(i%2 != 0) {
                        suretyTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        suretyTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

                if(festival.length() > 0){
                    JSONObject jsonObject = (JSONObject) festival.get(0);
                    Iterator<String> keys = jsonObject.keys();

                    int i = 0;
                    JSONObject tableHeader = new JSONObject();
                    while(keys.hasNext()){
                        String key = keys.next();
                        tableHeader.put(String.valueOf(i), key);
                        i++;
                    }

                    festivalTable.addView(CommonUtils.createTableRow(context, Color.WHITE, getResources().getColor(R.color.c1), tableHeader, true, Gravity.CENTER));
                }

                for(int i = 0; i < festival.length(); i++) {
                    JSONObject jsonObject = (JSONObject) festival.get(i);

                    if(i%2 != 0) {
                        festivalTable.addView(CommonUtils.createTableRow(context, Color.BLACK, getResources().getColor(R.color.lightGray), jsonObject, false, Gravity.LEFT));
                    }else {
                        festivalTable.addView(CommonUtils.createTableRow(context, Color.BLACK, Color.WHITE, jsonObject, false, Gravity.LEFT));
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
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
                Intent intent1 = new Intent(StatementActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(StatementActivity.this, StatementActivity.class);
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
        Intent i = new Intent(StatementActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}