package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LedgerActivity extends AppCompatActivity {
    TextView memberNameTextView;
    TextView memberNoTextView;
    TextView shareCapital1;
    TextView thrift1;
    TextView srf1;
    TextView suretyLoan1;
    TextView festivalLoan1;
    TextView floodLoan1;
    TextView total1;
    TextView total2;
    TextView net;

    Button btnMemberClosureAmount;

    private String memberName;
    private String memberNo;

    Context context = this;

    private String urlLedger = CommonUtils.IP + "/UCO/ucoandroid/ledger.php";

    ProgressDialog progressDialog;
    JsonObject jsonObject = new JsonObject();

    Double srfPercentage;
    Double netAmount;
    Double perSrf;

    String jsonString;
    JsonObject jsonData = new JsonObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

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
        boolean isSubLedger = Boolean.parseBoolean(intent.getStringExtra("isSubLedger"));
        jsonString = intent.getStringExtra("jsonData");

        JsonParser parser = new JsonParser();
        jsonData = (JsonObject) parser.parse(jsonString);

        memberNameTextView = findViewById(R.id.memberName);
        memberNoTextView = findViewById(R.id.memberNo);

        TextView shareCapital = findViewById(R.id.shareCapital);
        shareCapital1 = findViewById(R.id.shareCapital1);

        TextView thrift = findViewById(R.id.thrift);
        thrift1 = findViewById(R.id.thrift1);

        TextView srf = findViewById(R.id.srf);
        srf1 = findViewById(R.id.srf1);

        TextView suretyLoan = findViewById(R.id.suretyLoan);
        suretyLoan1 = findViewById(R.id.suretyLoan1);

        TextView festivalLoan = findViewById(R.id.festivalLoan);
        festivalLoan1 = findViewById(R.id.festivalLoan1);

        TextView floodLoan = findViewById(R.id.floodLoan);
        floodLoan1 = findViewById(R.id.floodLoan1);

        total1 = findViewById(R.id.assetsTotal1);
        total2 = findViewById(R.id.liabilitiesTotal1);

        net = findViewById(R.id.netAmount);

        btnMemberClosureAmount = findViewById(R.id.btnMemberClosureAmount);

        btnMemberClosureAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double memClose = netAmount - perSrf;
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Member Closure amount is " + memClose + ". Closure is subject to eligibility (i.e.) members having 3 yrs membership.");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });

        shareCapital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView) v);
            }
        });

        thrift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView) v);
            }
        });

        srf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView)v);
            }
        });

        suretyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView) v);
            }
        });

        festivalLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView)v);
            }
        });

        floodLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLedger((TextView)v);
            }
        });

        if(!isSubLedger) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jsonObject = new JsonObject();
            jsonObject.addProperty("memberNo", memberNo);

            PostLedgerActivity postLedgerActivity = new PostLedgerActivity(context);
            postLedgerActivity.checkServerAvailability(2);
        }else {
            memberNameTextView.setText("Member Name : " + jsonData.get("memberName").getAsString());
            memberNoTextView.setText("Member No : " + jsonData.get("memberNo").getAsString());
            shareCapital1.setText(jsonData.get("shareCapital").getAsString());
            thrift1.setText(jsonData.get("thrift").getAsString());
            srf1.setText(jsonData.get("srf").getAsString());
            suretyLoan1.setText(jsonData.get("suretyLoan").getAsString());
            festivalLoan1.setText(jsonData.get("festivalLoan").getAsString());
            floodLoan1.setText(jsonData.get("floodLoan").getAsString());
            total1.setText(jsonData.get("assertTotal").getAsString());
            total2.setText(jsonData.get("liabilities").getAsString());
            net.setText(jsonData.get("netAmount").getAsString());

            perSrf = jsonData.get("perSrf").getAsDouble();
            netAmount = jsonData.get("netAmount").getAsDouble();
        }

    }

    private class PostLedgerActivity extends PostRequest {
        public PostLedgerActivity(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlLedger, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            memberNameTextView.setText("Member Name : " + memberName);
            memberNoTextView.setText("Member No: " + memberNo);

            jsonData.addProperty("memberName", memberName);
            jsonData.addProperty("memberNo", memberNo);

            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);

                    shareCapital1.setText(obj.get("share_capital").toString());
                    thrift1.setText(obj.get("thrift_balance").toString());
                    srf1.setText(obj.get("srf_balance").toString());

                    srfPercentage = Double.parseDouble(obj.get("surety_percentage").toString());
                    String srf = srf1.getText().toString();
                    perSrf =((Double.parseDouble(srf) / 100) * srfPercentage);

                    suretyLoan1.setText(obj.get("surety_balance").toString());
                    festivalLoan1.setText(obj.get("festival_balance").toString());
                    floodLoan1.setText(obj.get("flood_balance").toString());

                    double assertTotal = 0.00;
                    assertTotal += (Double.parseDouble(shareCapital1.getText().toString()) + Double.parseDouble(thrift1.getText().toString()) + Double.parseDouble(srf1.getText().toString()));
                    total1.setText(new CommonUtils().StringToRound(Double.toString(assertTotal)));

                    double liabilitiesTotal = 0.00;
                    liabilitiesTotal += (Double.parseDouble(suretyLoan1.getText().toString()) + Double.parseDouble(festivalLoan1.getText().toString()) + Double.parseDouble(floodLoan1.getText().toString()));
                    total2.setText(new CommonUtils().StringToRound(Double.toString(liabilitiesTotal)));

                    netAmount = 0.00;
                    netAmount = assertTotal - liabilitiesTotal;
                    net.setText(new CommonUtils().StringToRound(Double.toString(netAmount)));

                    jsonData.addProperty("shareCapital", shareCapital1.getText().toString());
                    jsonData.addProperty("thrift", thrift1.getText().toString());
                    jsonData.addProperty("srf", srf1.getText().toString());
                    jsonData.addProperty("suretyLoan", suretyLoan1.getText().toString());
                    jsonData.addProperty("festivalLoan", festivalLoan1.getText().toString());
                    jsonData.addProperty("floodLoan", floodLoan1.getText().toString());
                    jsonData.addProperty("assertTotal", total1.getText().toString());
                    jsonData.addProperty("liabilities", total2.getText().toString());
                    jsonData.addProperty("netAmount", net.getText().toString());
                    jsonData.addProperty("perSrf", perSrf);
                    jsonData.addProperty("netAmount", netAmount);
                }
            }catch(JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON format invalid", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

    }

    public void subLedger(TextView txt) {
        Intent i = new Intent(LedgerActivity.this, SubLedgerActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("title", txt.getText().toString());
        i.putExtra("jsonData", jsonData.toString());
        startActivity(i);
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
                Intent intent1 = new Intent(LedgerActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(LedgerActivity.this, LedgerActivity.class);
                intent2.putExtra("memberNo", memberNo);
                intent2.putExtra("memberName", memberName);
                intent2.putExtra("isSubLedger", "false");
                intent2.putExtra("jsonData", new JsonObject().toString());
                startActivity(intent2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(LedgerActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }

}