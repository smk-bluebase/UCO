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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPasswordTextView;
    EditText newPasswordTextView;
    EditText confirmPasswordTextView;

    String memberNo;
    String memberName;

    private String urlChangePassword = CommonUtils.IP + "/UCO/ucoandroid/change_password.php";

    String status;
    JsonObject jsonObject;
    ProgressDialog progressDialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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

        currentPasswordTextView = findViewById(R.id.currentPassword);
        newPasswordTextView = findViewById(R.id.newPassword1);
        confirmPasswordTextView = findViewById(R.id.newPassword2);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordTextView.getText().toString();
                String newPassword = newPasswordTextView.getText().toString();
                String confirmPassword = confirmPasswordTextView.getText().toString();

                if(!currentPassword.isEmpty()) {
                    if(!newPassword.isEmpty()) {
                        if(!confirmPassword.isEmpty()) {
                            if(newPassword.equals(confirmPassword)) {
                                progressDialog.show();

                                jsonObject = new JsonObject();
                                jsonObject.addProperty("memberNo", memberNo);
                                jsonObject.addProperty("currentPassword", currentPassword);
                                jsonObject.addProperty("newPassword", newPassword);

                                PostChangePassword postChangePassword = new PostChangePassword(context);
                                postChangePassword.checkServerAvailability(2);
                            } else {
                                Toast.makeText(getApplicationContext(), "New Password and Retype \nPassword are not same", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Please enter the New \nPassword again to confirm", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Please enter New Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Current Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class PostChangePassword extends PostRequest {
        public PostChangePassword(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlChangePassword, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
            }
        }

        public void onFinish(JSONArray jsonArray){
              try {
                JSONObject jsonObject =(JSONObject) jsonArray.get(0);
                status = jsonObject.get("status").toString();
                AlertDialog.Builder alert=new AlertDialog.Builder(ChangePasswordActivity.this);
                alert.setMessage(status);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
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
                Intent intent1 = new Intent(ChangePasswordActivity.this, MenuActivity.class);
                intent1.putExtra("memberNo", memberNo);
                intent1.putExtra("memberName", memberName);
                intent1.putExtra("isLogin", "false");
                startActivity(intent1);
                finish();
                return true;

            case R.id.reconnect:
                Intent intent2 = new Intent(ChangePasswordActivity.this, ChangePasswordActivity.class);
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
    public void onBackPressed() {
        Intent i = new Intent(ChangePasswordActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}