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

public class DemandActivity extends AppCompatActivity {
    TextView memberNameTextView;
    TextView memberNoTextView;
    TextView srf;
    TextView srfOD;
    TextView thrift;
    TextView thriftOD;
    TextView suretyPrinciple;
    TextView suretyPrincipleOD;
    TextView suretyInterest;
    TextView suretyInterestOD;
    TextView festivalLoan;
    TextView festivalLoanOD;
    TextView floodLoan;
    TextView floodLoanOD;
    TextView total;

    String memberNo;
    String memberName;

    String urlGetDemand = CommonUtils.IP + "/UCO/ucoandroid/get_demand.php";

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    String[] months = {"January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand);

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

        srf = findViewById(R.id.srf1);
        srfOD = findViewById(R.id.srfOD1);
        thrift = findViewById(R.id.thrift1);
        thriftOD = findViewById(R.id.thriftOD1);
        suretyPrinciple = findViewById(R.id.suretyPrinciple1);
        suretyPrincipleOD = findViewById(R.id.suretyPrincipleOD1);
        suretyInterest = findViewById(R.id.suretyInterest1);
        suretyInterestOD = findViewById(R.id.suretyInterestOD1);
        festivalLoan = findViewById(R.id.festivalLoan1);
        festivalLoanOD = findViewById(R.id.festivalLoanOD1);
        floodLoan = findViewById(R.id.floodLoan1);
        floodLoanOD = findViewById(R.id.floodLoanOD1);
        total = findViewById(R.id.total1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);

        PostDemandActivity postDemandActivity = new PostDemandActivity(this);
        postDemandActivity.checkServerAvailability(2);
    }

    private class PostDemandActivity extends PostRequest{
        public PostDemandActivity(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlGetDemand, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    String demandMonthRaisedStr = jsonObject.getString("demandMonthNo");
                    String month = months[Integer.parseInt(demandMonthRaisedStr.substring(0, 2)) - 1] + ", " + Integer.parseInt(demandMonthRaisedStr.substring(2));

                    TextView demandRaisedMonth = findViewById(R.id.demandRaisedMonth);
                    demandRaisedMonth.setText("Demand raised on : " + month);

                    srf.setText(jsonObject.getString("srf"));
                    srfOD.setText(jsonObject.getString("srfOD"));
                    thrift.setText(jsonObject.getString("thrift"));
                    thriftOD.setText(jsonObject.getString("thriftOD"));
                    suretyPrinciple.setText(jsonObject.getString("suretyPrinciple"));
                    suretyPrincipleOD.setText(jsonObject.getString("suretyODPrinciple"));
                    suretyInterest.setText(jsonObject.getString("suretyInterest"));
                    suretyInterestOD.setText(jsonObject.getString("suretyODInterest"));
                    festivalLoan.setText(jsonObject.getString("festivalPrinciple"));
                    festivalLoanOD.setText(jsonObject.getString("festivalODPrinciple"));
                    floodLoan.setText(jsonObject.getString("floodPrinciple"));
                    floodLoanOD.setText(jsonObject.getString("floodODPrinciple"));
                    total.setText(jsonObject.getString("demandAmount"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON format invalid", Toast.LENGTH_SHORT).show();
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
                Intent intent1 = new Intent(DemandActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(DemandActivity.this, DemandActivity.class);
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
        Intent i = new Intent(DemandActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}