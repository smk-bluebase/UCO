package com.example.mypc.uco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.JsonObject;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class InterestCertificatePDFActivity extends AppCompatActivity {
    Context context = this;

    int section;
    String memberName;
    String memberNo;
    String destination = "";

    private static final int PERMISSION_REQUEST_CODE = 100;

    String urlLoanInterestCertificate = CommonUtils.IP + "/UCO/ucoandroid/loan_interest_certificate_details.php";
    String urlFDInterestCertificate = CommonUtils.IP + "/UCO/ucoandroid/fd_interest_certificate_details.php";
    String urlThriftInterestCertificate = CommonUtils.IP + "/UCO/ucoandroid/thrift_interest_certificate_details.php";
    String urlGetBankAddress = CommonUtils.IP + "/UCO/ucoandroid/get_branch_address.php";

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    PDFView pdfView;

    String nameStr;
    String memberNoStr;
    String fromYearStr;
    String toYearStr;

    String branch_name;
    String zone;
    AutoCompleteTextView year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_certificate_pdf);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent = getIntent();
        section = intent.getIntExtra("section", 1);
        memberName = intent.getStringExtra("memberName");
        memberNo = intent.getStringExtra("memberNo");

        String[] yearArray = {};

        if(section == 1 || section == 2) {
            for (int i = 2019; i <= new Date().getYear() + 1900; i++){
                yearArray = Arrays.copyOf(yearArray, yearArray.length + 1);
                yearArray[yearArray.length - 1] = i + " - " + (i + 1);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, yearArray);
            year = findViewById(R.id.year);
            year.setAdapter(adapter);
            year.setKeyListener(null);
        }else {
            for (int i = 2017; i <= new Date().getYear() + 1899; i++){
                yearArray = Arrays.copyOf(yearArray, yearArray.length + 1);
                yearArray[yearArray.length - 1] = i + " - " + (i + 1);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, yearArray);
            year = findViewById(R.id.year);
            year.setAdapter(adapter);
            year.setKeyListener(null);
        }

        pdfView = findViewById(R.id.pdfView);

        nameStr = memberName;
        memberNoStr = memberNo;

        Button ok = findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year.isFocused()) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    String selectedYear = year.getText().toString();
                    fromYearStr = selectedYear.substring(0, 4);
                    toYearStr = selectedYear.substring(7);

                    jsonObject = new JsonObject();
                    jsonObject.addProperty("memberNo", memberNo);
                    jsonObject.addProperty("fromYear", fromYearStr);
                    jsonObject.addProperty("toYear", toYearStr);

                    if (section == 1) {
                        PostLoanInterestCertificate postLoanInterestCertificate = new PostLoanInterestCertificate(context);
                        postLoanInterestCertificate.checkServerAvailability(2);
                    } else if (section == 2) {
                        PostFDInterestCertificate postFDInterestCertificate = new PostFDInterestCertificate(context);
                        postFDInterestCertificate.checkServerAvailability(2);
                    } else if (section == 3) {
                        PostThriftInterestCertificate postThriftInterestCertificate = new PostThriftInterestCertificate(context);
                        postThriftInterestCertificate.checkServerAvailability(2);
                    }
                }
            }
        });

        Button share = findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(destination));
                intent.setDataAndType(uri, "application/pdf");
                PackageManager pm = context.getPackageManager();
                startActivity(Intent.createChooser(intent, "Choose a File Viewer"));
            }
        });

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);

        PostGetBankAddress postGetBankAddress = new PostGetBankAddress(context);
        postGetBankAddress.checkServerAvailability(2);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(InterestCertificatePDFActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(InterestCertificatePDFActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }

    private class PostLoanInterestCertificate extends PostRequest{
        public PostLoanInterestCertificate(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlLoanInterestCertificate, jsonObject);
            }else{
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray) {
            try{
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
                String fileName = "Loan_Interest_Certificate_" + dateTimeFormat.format(new Date()) + ".pdf";

                String moneyStr = jsonObject.getString("total");

                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = originalFormat.parse(jsonObject.getString("fromDate"));
                String fromDateStr = targetFormat.format(date);
                date = originalFormat.parse(jsonObject.getString("toDate"));
                String toDateStr = targetFormat.format(date);

                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                String year = yearFormat.format(date);

                String interestRecoveredNo = jsonObject.getString("interestRecovered");
                String interestToBeRecoveredNo = jsonObject.getString("interestToBeRecovered") + "  (for the period from 01-03-" + year + " to 31-3-" + year + ")";

                try {
                    InputStream is = getAssets().open("uco.png");
                    byte[] bytes = IOUtils.toByteArray(is);
                    ImageData imageData = ImageDataFactory.create(bytes);

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            destination = sdcard + "/" + fileName;
                            File file = new File(destination);
                            try {
                                file.createNewFile();
                                if(file.exists()) {
                                    LoanInterestCertificate loanInterestCertificate = new LoanInterestCertificate();
                                    loanInterestCertificate.createPDF(pdfView, file,  destination, imageData, nameStr, memberNoStr, branch_name, zone, moneyStr, fromDateStr, toDateStr, interestRecoveredNo, interestToBeRecoveredNo);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            requestPermission();
                        }
                    } else {
                        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        destination = sdcard + "/" + fileName;
                        File file = new File(destination);
                        try {
                            file.createNewFile();
                            if(file.exists()) {
                                LoanInterestCertificate loanInterestCertificate = new LoanInterestCertificate();
                                loanInterestCertificate.createPDF(pdfView, file, destination, imageData, nameStr, memberNoStr, branch_name, zone, moneyStr, fromDateStr, toDateStr, interestRecoveredNo, interestToBeRecoveredNo);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }catch(JSONException e){
                e.printStackTrace();
            }catch(ParseException e){
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

    private class PostFDInterestCertificate extends PostRequest{
        public PostFDInterestCertificate(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlFDInterestCertificate, jsonObject);
            }else{
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray) {
            try{
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
                String fileName = "FD_Interest_Certificate_" + dateTimeFormat.format(new Date()) + ".pdf";

                InputStream is = getAssets().open("uco.png");
                byte[] bytes = IOUtils.toByteArray(is);
                ImageData imageData = ImageDataFactory.create(bytes);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        destination = sdcard + "/" + fileName;
                        File file = new File(destination);
                        try {
                            file.createNewFile();
                            if(file.exists()) {
                                FDInterestCertificate fdInterestCertificate = new FDInterestCertificate();
                                fdInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, branch_name, zone, fromYearStr, toYearStr);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    destination = sdcard + "/" + fileName;
                    File file = new File(destination);
                    try {
                        file.createNewFile();
                        if(file.exists()) {
                            FDInterestCertificate fdInterestCertificate = new FDInterestCertificate();
                            fdInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, branch_name, zone, fromYearStr, toYearStr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

    private class PostThriftInterestCertificate extends PostRequest{
        public PostThriftInterestCertificate(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlThriftInterestCertificate, jsonObject);
            }else{
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray) {
            try{
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
                String fileName = "Thrift_Interest_Certificate_" + dateTimeFormat.format(new Date()) + ".pdf";

                InputStream is = getAssets().open("uco.png");
                byte[] bytes = IOUtils.toByteArray(is);
                ImageData imageData = ImageDataFactory.create(bytes);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        destination = sdcard + "/" + fileName;
                        File file = new File(destination);
                        try {
                            file.createNewFile();
                            if(file.exists()) {
                                ThriftInterestCertificate thriftInterestCertificate = new ThriftInterestCertificate();
                                thriftInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, branch_name, zone, fromYearStr, toYearStr);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    destination = sdcard + "/" + fileName;
                    File file = new File(destination);
                    try {
                        file.createNewFile();
                        if(file.exists()) {
                            ThriftInterestCertificate thriftInterestCertificate = new ThriftInterestCertificate();
                            thriftInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, branch_name, zone, fromYearStr, toYearStr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

    private class PostGetBankAddress extends PostRequest{
        public PostGetBankAddress(Context context){
            super(context);
        }

        public void serverAvailability(boolean isServerAvailable){
            if(isServerAvailable){
                super.postRequest(urlGetBankAddress, jsonObject);
            }else {
                Toast.makeText(getApplicationContext(), "Connection to Network \nnot Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        public void onFinish(JSONArray jsonArray){
            progressDialog.dismiss();

            try{
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                branch_name = jsonObject.getString("name");
                zone = jsonObject.getString("zone");

                if(branch_name.contains(zone)){
                    zone = "";
                }

                if (branch_name.contains(",")){
                    branch_name = branch_name.replace(",", "");
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

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
                Intent intent = new Intent(InterestCertificatePDFActivity.this, InterestCertificateActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(InterestCertificatePDFActivity.this, InterestCertificateActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        startActivity(i);
    }

}