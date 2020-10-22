package com.example.mypc.uco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

public class EmployeeDetailsActivity extends AppCompatActivity {

    ListView employeeList;
    String urlEmployeeList = CommonUtils.IP + "/UCO/ucoandroid/employee_details.php";
    Dialog dialog;
    String memberNo;
    String memberName;
    EmployeeDetailsAdapter EmpAdapter;
    Context context = this;
    JsonObject jsonObject = new JsonObject();
    ProgressDialog progressDialog;
    ArrayList<EmployeeDetailsObj> objects = new ArrayList<EmployeeDetailsObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        EmpAdapter = new EmployeeDetailsAdapter(this, objects);

        Intent intent=getIntent();
        memberNo=intent.getStringExtra("memberNo");
        memberName=intent.getStringExtra("memberName");

        dialog = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject.addProperty("memberNo", memberNo);
        
        PostEmployeeDetails postEmployeeDetails = new PostEmployeeDetails(context);
        postEmployeeDetails.checkServerAvailability(2);

        employeeList = findViewById(R.id.contactList);

        employeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EmployeeDetailsObj employeeDetailsObj = objects.get(position);

                dialog.setTitle(memberName);
                dialog.setContentView(R.layout.popup);

                String mobileNo = employeeDetailsObj.getMobile_no();
                String mail = employeeDetailsObj.getEmail_id();

                TextView phoneNumber = dialog.findViewById(R.id.phoneNumber);
                phoneNumber.setText(mobileNo);

                TextView message = dialog.findViewById(R.id.message);
                message.setText(mobileNo);

                TextView mailId = dialog.findViewById(R.id.mail);
                mailId.setText(mail);

                RelativeLayout phoneOption = dialog.findViewById(R.id.phoneOption);
                phoneOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + mobileNo));
                        startActivity(phoneIntent);
                    }
                });

                RelativeLayout messageOption = dialog.findViewById(R.id.messageOption);
                messageOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setData(Uri.parse("smsto:" + mobileNo));
                        startActivity(smsIntent);
                    }
                });

                RelativeLayout mailOption = dialog.findViewById(R.id.mailOption);
                mailOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent=new Intent(Intent.ACTION_VIEW);
                        emailIntent.setData(Uri.parse("mailto:" + mail));
                        startActivity(emailIntent);
                    }
                });

                dialog.show();
            }
        });

    }
    
    private class PostEmployeeDetails extends PostRequest{
        public PostEmployeeDetails(Context context){
            super(context);
        }
        
        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlEmployeeList, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(),"Connection to network \nnot Available",Toast.LENGTH_SHORT).show();
            }
        }
        
        public void onFinish(JSONArray jsonArray){
            try{
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    String memberNo = "";
                    String memberName = "";
                    String branchName = "";
                    String mobileNo = "";
                    String emailId = "";

                    if(jsonObject.get("memberNo") != null) memberNo = jsonObject.get("memberNo").toString();
                    if(jsonObject.get("name") != null) memberName = jsonObject.get("name").toString();
                    if(jsonObject.get("branch") != null) branchName = jsonObject.get("branch").toString();
                    if(jsonObject.get("mobileNo") != null) mobileNo = jsonObject.get("mobileNo").toString();
                    if(jsonObject.get("email") != null) emailId = jsonObject.get("email").toString();

                    EmployeeDetailsObj EmpObj = new EmployeeDetailsObj(memberNo, memberName, branchName, mobileNo, emailId);
                    objects.add(EmpObj);
                }
            } catch(JSONException e){
                Toast.makeText(getApplicationContext(), "JSON format invalid", Toast.LENGTH_SHORT).show();
            }

            EmpAdapter.copyData();
            employeeList.setAdapter(EmpAdapter);

            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EmpAdapter.filter(newText.trim());
                employeeList.invalidate();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1 = new Intent(EmployeeDetailsActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EmployeeDetailsActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        startActivity(i);
    }

}