package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class RecentActivityActivity extends AppCompatActivity {

    String memberNo;
    String memberName;
    ListView listRecentActivity;
    RecentActivityAdapter recentAdapter;
    ArrayList<RecentActivityObj> objects = new ArrayList<RecentActivityObj>();

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    String urlRecentActivity = CommonUtils.IP + "/UCO/ucoandroid/get_recent_activity.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activity);

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

        recentAdapter = new RecentActivityAdapter(this,objects);
        listRecentActivity = findViewById(R.id.lst_recent_activity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);
        jsonObject.addProperty("memberName", memberName);

        PostRecentActivity postRecentActivity = new PostRecentActivity(this);
        postRecentActivity.checkServerAvailability(2);
    }

    private class PostRecentActivity extends PostRequest {
        public PostRecentActivity(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlRecentActivity, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            try {
                JSONObject obj = (JSONObject) jsonArray.get(0);

                String member = obj.get("member").toString();
                if(!member.equals("")) {
                    RecentActivityObj recentObj = new RecentActivityObj(member);
                    objects.add(recentObj);
                }

                String demandMsg = obj.get("demand_msg").toString();
                if(!demandMsg.equals("")) {
                    RecentActivityObj demand = new RecentActivityObj(demandMsg);
                    objects.add(demand);
                }

                String demandCollectionMsg = obj.get("demand_collection_msg").toString();
                if(!demandCollectionMsg.equals("")) {
                    RecentActivityObj demandCollection = new RecentActivityObj(demandCollectionMsg);
                    objects.add(demandCollection);
                }

                String newLoanMsg = obj.get("new_loan_msg").toString();
                if(!newLoanMsg.equals("")) {
                    RecentActivityObj newLoan = new RecentActivityObj(newLoanMsg);
                    objects.add(newLoan);
                }

                String closedLoanMsg = obj.get("closed_loan_msg").toString();
                if(!closedLoanMsg.equals("")) {
                    RecentActivityObj closedLoan = new RecentActivityObj(closedLoanMsg);
                    objects.add(closedLoan);
                }

                String countFdMsg = obj.get("count_fd_msg").toString();
                if(!countFdMsg.equals("")) {
                    RecentActivityObj countFd = new RecentActivityObj(countFdMsg);
                    objects.add(countFd);
                }

                String interestFdMsg = obj.get("interest_fd_msg").toString();
                if(!interestFdMsg.equals("")) {
                    RecentActivityObj interestFd = new RecentActivityObj(interestFdMsg);
                    objects.add(interestFd);
                }
            }catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON format invalid", Toast.LENGTH_SHORT).show();
            }

            listRecentActivity.setAdapter(recentAdapter);
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
                Intent intent1 = new Intent(RecentActivityActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(RecentActivityActivity.this, RecentActivityActivity.class);
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
        Intent i = new Intent(RecentActivityActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }

}