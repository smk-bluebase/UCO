package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    Context context = this;
    JsonObject jsonObject;

    String urlForgotPasswordVerifier = CommonUtils.IP + "/UCO/ucoandroid/forgotpasswordverifier.php";

    String userName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent=getIntent();
        userName = intent.getStringExtra("userName");

        EditText otp = findViewById(R.id.otp);
        EditText newPassword = findViewById(R.id.newPassword);
        EditText confirmPassword = findViewById(R.id.confirmPassword);

        Button changePassword = findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otp.getText().toString().equals("")) {
                    if(!newPassword.getText().toString().equals("")) {
                        if(!confirmPassword.getText().toString().equals("")) {
                            if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Loading...");
                                progressDialog.show();

                                jsonObject = new JsonObject();
                                jsonObject.addProperty("otp", otp.getText().toString());
                                jsonObject.addProperty("userName", userName);
                                jsonObject.addProperty("password", newPassword.getText().toString());

                                PostForgotPasswordVerifier postForgotPasswordVerifier = new PostForgotPasswordVerifier(context);
                                postForgotPasswordVerifier.checkServerAvailability(2);
                            }else{
                                Toast.makeText(getApplicationContext(),"Passwords don't match!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Retype the new Password",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Enter new Password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Enter OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class PostForgotPasswordVerifier extends PostRequest{
        public PostForgotPasswordVerifier(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable) {
            if(isServerAvailable){
                super.postRequest(urlForgotPasswordVerifier, jsonObject);
            }else{
                Toast.makeText(getApplicationContext(),"Connection to network \nnot Available",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                if(jsonObject.getBoolean("status")){
                    Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Password Change Unsuccessful",Toast.LENGTH_SHORT).show();
                }

            } catch(JSONException e){
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
                Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
        startActivity(i);
    }
}