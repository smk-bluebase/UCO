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

public class LoanActivity extends AppCompatActivity {
    TextView suretyNameTextView;
    TextView suretyNoTextView;
    TextView dorTextView;
    TextView suretyRetireDateTextView;
    TextView lastLoanDateTextView;
    TextView lastLoanAmountTextView;
    TextView nextLoanDateTextView;

    SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-mm-yyyy");
    String str_loan_ap_date,status;
    Context context = this;
    int loan_amount_request = 0;

    String memberName;
    String memberNo;
    JsonObject jsonObject;

    private String urlLoan = CommonUtils.IP + "/UCO/ucoandroid/loan.php";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

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

        suretyNameTextView = findViewById(R.id.suretyName);
        suretyNoTextView = findViewById(R.id.suretyNo);
        dorTextView = findViewById(R.id.dor);
        suretyRetireDateTextView = findViewById(R.id.suretyRetireDate);
        lastLoanDateTextView = findViewById(R.id.lastLoanDate);
        lastLoanAmountTextView = findViewById(R.id.lastLoanAmount);
        nextLoanDateTextView = findViewById(R.id.nextLoanDate);

        if (!memberNo.isEmpty()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jsonObject = new JsonObject();
            jsonObject.addProperty("memberNo", memberNo);

            PostLoanActivity postLoanActivity = new PostLoanActivity(this);
            postLoanActivity.checkServerAvailability(2);
        }

    }

    private class PostLoanActivity extends PostRequest{
        public PostLoanActivity(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlLoan, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            String suretyName = "";
            String suretyNo = "";
            String dor = "";
            String suretyRetireDate = "";
            String lastLoanAmount = "";
            String lastLoanDate = "";
            String nextLoanDate = "";

            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if(!jsonObject.get("suretyName").toString().equals("")) suretyName = jsonObject.get("suretyName").toString();
                    if(!jsonObject.get("suretyNo").toString().equals("")) suretyNo = jsonObject.get("suretyNo").toString();
                    if(!jsonObject.get("DOR").toString().equals("")){
                        Date oldDate = oldDateFormat.parse(jsonObject.get("DOR").toString());
                        dor = newDateFormat.format(oldDate);
                    }
                    if(!jsonObject.get("suretyRetireDate").toString().equals("")){
                        Date oldDate = oldDateFormat.parse(jsonObject.get("suretyRetireDate").toString());
                        suretyRetireDate = newDateFormat.format(oldDate);
                    }
                    if(!jsonObject.get("lastLoanAmount").toString().equals("")) lastLoanAmount = jsonObject.get("lastLoanAmount").toString();
                    if(!jsonObject.get("lastLoanDate").toString().equals("")){
                        Date oldDate = oldDateFormat.parse(jsonObject.get("lastLoanDate").toString());
                        lastLoanDate = newDateFormat.format(oldDate);
                    }
                    if(!jsonObject.get("nextLoanDate").toString().equals("")){
                        Date oldDate = oldDateFormat.parse(jsonObject.get("nextLoanDate").toString());
                        nextLoanDate = newDateFormat.format(oldDate);
                    }
                }
            }catch(ParseException e){
                e.printStackTrace();
            }
            catch(JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON format invalid",Toast.LENGTH_SHORT).show();
            }

            suretyNameTextView.setText(suretyName);
            suretyNoTextView.setText(suretyNo);
            dorTextView.setText(dor);
            suretyRetireDateTextView.setText(suretyRetireDate);
            lastLoanAmountTextView.setText(lastLoanAmount);
            lastLoanDateTextView.setText(lastLoanDate);
            nextLoanDateTextView.setText(nextLoanDate);

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
                Intent intent1 = new Intent(LoanActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(LoanActivity.this, LoanActivity.class);
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
        Intent i = new Intent(LoanActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}