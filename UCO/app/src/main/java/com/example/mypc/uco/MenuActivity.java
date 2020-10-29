package com.example.mypc.uco;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    String message;
    String memberNo;
    String memberName;
    NotificationManager notificationManager;
    String urlGetMessage = CommonUtils.IP + "/UCO/ucoandroid/get_notification.php";
    Context context = this;
    PendingIntent pendingIntent;
    Intent notificationIntent;
    ProgressDialog progressDialog;
    JsonObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        final Intent intent=getIntent();
        memberNo = intent.getStringExtra("memberNo");
        memberName = intent.getStringExtra("memberName");
        boolean isLogin = Boolean.parseBoolean(intent.getStringExtra("isLogin"));

        TextView displayUserName = findViewById(R.id.displayUserName);
        displayUserName.setText("WELCOME, " + memberName);

        if(isLogin) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jsonObject = new JsonObject();
            jsonObject.addProperty("memberNo", memberNo);

            PostNotification postNotification = new PostNotification(this);
            postNotification.checkServerAvailability(2);
        }

        LinearLayout recentActivity = findViewById(R.id.recentActivity);
        recentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RecentActivityActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
            }
        });

        LinearLayout personalDetails = findViewById(R.id.personalDetails);
        personalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PersonalActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout assets = findViewById(R.id.assets);
        assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LedgerActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                intent.putExtra("isSubLedger", "false");
                intent.putExtra("jsonData", new JsonObject().toString());
                startActivity(intent);
                finish();
            }
        });

        LinearLayout loans = findViewById(R.id.loans);
        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoanActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout demand = findViewById(R.id.demand);
        demand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, DemandActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout suretyDetails = findViewById(R.id.suretyDetails);
        suretyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SuretyDetailsActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout fixedDeposit = findViewById(R.id.fixedDeposit);
        fixedDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, FixedDepositActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout interestCertificate = findViewById(R.id.interestCertificate);
        interestCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, InterestCertificateActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout rateOfInterest = findViewById(R.id.rateOfInterest);
        rateOfInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RateOfInterestActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout memberContactInfo = findViewById(R.id.memberContactInfo);
        memberContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this, EmployeeDetailsActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
            }
        });

        LinearLayout changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ChangePasswordActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
            }
        });

    }

    private class PostNotification extends PostRequest {
        public PostNotification(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlGetMessage, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    message = obj.getString("message");
                    notificationIntent.putExtra("message", message);
                    pendingIntent = PendingIntent.getActivity(context, obj.getInt("id"), notificationIntent, 0);
                    Notification notification = null;
                    notification = new Notification.Builder(context)
                            .setContentTitle("New Notification")
                            .setContentText(obj.get("message").toString())
                            .setSmallIcon(R.drawable.ic_notifications)
                            .setContentIntent(pendingIntent)
                            .getNotification();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(obj.getInt("id"), notification);
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
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(i);
    }

}