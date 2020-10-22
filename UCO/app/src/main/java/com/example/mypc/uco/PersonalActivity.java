package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalActivity extends AppCompatActivity {
    TextView dob;
    TextView dor;
    TextView branch;
    TextView email;
    TextView mobileNo;
    TextView panNo;
    TextView accountNo;
    TextView nominee;
    TextView relationship;
    TextView address;

    Context context = this;

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    private String memberNo,memberName;
    private String urlPersonalDetails = CommonUtils.IP + "/UCO/ucoandroid/personal_details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent=getIntent();
        memberNo = intent.getStringExtra("memberNo");
        memberName = intent.getStringExtra("memberName");

        TextView memberNameTextView = findViewById(R.id.memberName);
        memberNameTextView.setText(memberName);
        
        TextView memberNoTextView = findViewById(R.id.memberNo);
        memberNoTextView.setText(memberNo);
        
        dob = findViewById(R.id.dob);
        dor = findViewById(R.id.dor);
        branch = findViewById(R.id.branch);
        email = findViewById(R.id.email);
        mobileNo = findViewById(R.id.mobileNo);
        panNo = findViewById(R.id.pan);
        accountNo = findViewById(R.id.accountNo);
        nominee = findViewById(R.id.nominee);
        relationship = findViewById(R.id.relationship);
        address = findViewById(R.id.address);

        if (!memberNo.isEmpty()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jsonObject = new JsonObject();
            jsonObject.addProperty("memberNo", memberNo);

            PostPersonalActivity postPersonalActivity = new PostPersonalActivity(this);
            postPersonalActivity.checkServerAvailability(2);

        }
    }

    private class PostPersonalActivity extends PostRequest {
        public PostPersonalActivity(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlPersonalDetails, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try{
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj=(JSONObject) jsonArray.get(i);
                    branch.setText(obj.get("branch_code").toString()+" - "+obj.get("branch_name").toString());
                    mobileNo.setText(obj.get("mobile_no").toString());
                    email.setText(obj.get("email").toString());
                    accountNo.setText(obj.get("account_no").toString());
                    panNo.setText(obj.get("pancard_no").toString());
                    nominee.setText(obj.get("nominee_name").toString());
                    relationship.setText(obj.get("nominee_relationship").toString());
                    address.setText(obj.get("address").toString());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-mm-yyyy");
                    try {
                        Date date_of_birthobj = dateFormat.parse(obj.get("dob").toString());
                        Date date_of_retobj = dateFormat.parse(obj.get("ret_date").toString());

                        dob.setText(newDateFormat.format(date_of_birthobj));
                        dor.setText(newDateFormat.format(date_of_retobj));
                    }catch(ParseException e) {
                        Toast.makeText(getApplicationContext(),"date parsing error",Toast.LENGTH_LONG).show();
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
                Intent intent1 = new Intent(PersonalActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(PersonalActivity.this, PersonalActivity.class);
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
        Intent i = new Intent(PersonalActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }

}