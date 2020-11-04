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

import androidx.appcompat.app.AppCompatActivity;

public class RateOfInterestActivity extends AppCompatActivity {
    TextView thriftCapital;
    TextView fixedDeposit;
    TextView suretyLoan1;
    TextView suretyLoan2;
    TextView festivalLoan1;
    TextView festivalLoan2;

    private String urlRateOfInterest = CommonUtils.IP + "/UCO/ucoandroid/rate_of_interest.php";

    String memberNo;
    String memberName;

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_of_interest);

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

        thriftCapital = findViewById(R.id.thriftCapital1);
        fixedDeposit = findViewById(R.id.fixedDeposit1);
        suretyLoan1 = findViewById(R.id.suretyLoan1);
        suretyLoan2 = findViewById(R.id.suretyLoan2);
        festivalLoan1 = findViewById(R.id.festivalLoan1);
        festivalLoan2 = findViewById(R.id.festivalLoan2);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);

        PostRateOfInterest postRateOfInterest = new PostRateOfInterest(this);
        postRateOfInterest.checkServerAvailability(2);
    }

    private class PostRateOfInterest extends PostRequest{
        public PostRateOfInterest(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlRateOfInterest, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try{
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = (JSONObject) jsonArray.get(i);

                    thriftCapital.setText(obj.get("thrift_interest").toString().concat("%"));
                    fixedDeposit.setText(obj.get("fd_interest").toString().concat("%"));
                    suretyLoan1.setText(obj.get("surety_loan_interest").toString().concat("%"));
                    suretyLoan2.setText(obj.get("surety_loan_od_interest").toString().concat("%"));
                    festivalLoan1.setText(obj.get("festival_loan_interest").toString().concat("%"));
                    festivalLoan2.setText(obj.get("festival_loan_od_interest").toString().concat("%"));
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
                Intent intent1 = new Intent(RateOfInterestActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(RateOfInterestActivity.this, RateOfInterestActivity.class);
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
        Intent i = new Intent(RateOfInterestActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}