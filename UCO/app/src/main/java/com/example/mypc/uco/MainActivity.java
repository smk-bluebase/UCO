package com.example.mypc.uco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText userNameEditText;
    EditText passwordEditText;
    Button btnLogin;
    TextView forgotPasswordTextView;
    Dialog dialog;
    ProgressDialog progressDialog;

    Context context = this;
    Boolean[] postOptions = new Boolean[2];
    JsonObject jsonObject;
    String urlLogin = CommonUtils.IP + "/UCO/ucoandroid/login.php";
    String urlForgotPasswordGenerator = CommonUtils.IP + "/UCO/ucoandroid/forgotpasswordgenerator.php";

    EditText userNameEditText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        userNameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        btnLogin = findViewById(R.id.login);
        forgotPasswordTextView = findViewById(R.id.lbl_forget_password);

        dialog = new Dialog(this);
        dialog.setCancelable(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(!userName.isEmpty() && !password.isEmpty()){
                    progressDialog.show();

                    jsonObject = new JsonObject();
                    jsonObject.addProperty("userName", userName);
                    jsonObject.addProperty("password", password);

                    Arrays.fill(postOptions, false);
                    postOptions[0] = true;

                    LoginRequest loginRequest = new LoginRequest(context);
                    loginRequest.checkServerAvailability(2);
                }
                else {
                    Toast.makeText(MainActivity.this,"Please Enter the Credentials!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("New Password Request");
                dialog.setContentView(R.layout.forget_password);

                userNameEditText1 = dialog.findViewById(R.id.username);
                EditText emailEditText = dialog.findViewById(R.id.email);
                Button submit = dialog.findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();

                        String employeeNo = userNameEditText1.getText().toString();
                        String email = emailEditText.getText().toString();

                        if(!employeeNo.isEmpty() && !email.isEmpty()){
                            jsonObject = new JsonObject();
                            jsonObject.addProperty("userName", employeeNo);
                            jsonObject.addProperty("email", email);

                            Arrays.fill(postOptions, false);
                            postOptions[1] = true;

                            LoginRequest loginRequest = new LoginRequest(context);
                            loginRequest.checkServerAvailability(2);
                        }else {
                            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                }
        });
    }

    private class LoginRequest extends PostRequest{
        public LoginRequest(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                if(postOptions[0]) {
                    super.postRequest(urlLogin, jsonObject);
                }else if(postOptions[1]){
                    super.postRequest(urlForgotPasswordGenerator, jsonObject);
                }
            }else {
                Toast.makeText(getApplicationContext(),"Connection to network \nnot Available",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            if(postOptions[0]){
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                    if (Boolean.parseBoolean(jsonObject.get("status").toString()) == true){
                        String memberNo = jsonObject.getString("memberNo");
                        String memberName = jsonObject.getString("memberName");

                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        intent.putExtra("memberNo", memberNo);
                        intent.putExtra("memberName", memberName);
                        intent.putExtra("isLogin", "true");
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Username or Password Incorrect",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"UserName not Registered",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else if(postOptions[1]){
                dialog.cancel();
                progressDialog.dismiss();

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        if(obj.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), "Check email for OTP", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                            intent.putExtra("userName", userNameEditText1.getText().toString());
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "UserName or Email Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "JSON format invalid", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}