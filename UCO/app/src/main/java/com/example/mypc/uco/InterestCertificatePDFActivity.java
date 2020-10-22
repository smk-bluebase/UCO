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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

    JsonObject jsonObject;
    ProgressDialog progressDialog;

    PDFView pdfView;

    String nameStr;
    String memberNoStr;
    String fromYearStr;
    String toYearStr;

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

        Spinner year = findViewById(R.id.year);

        if(section == 1 || section == 2) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.financialYear, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year.setAdapter(adapter);
        }else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.thriftInterest, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year.setAdapter(adapter);
        }

        pdfView = findViewById(R.id.pdfView);

        nameStr = memberName;
        memberNoStr = memberNo;

        Button ok = findViewById(R.id.ok);

        jsonObject = new JsonObject();
        jsonObject.addProperty("memberNo", memberNo);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String selectedYear = year.getSelectedItem().toString();

                if(section == 1 || section == 2) {
                    if (selectedYear.equals("2019 - 2020")) {
                        jsonObject.addProperty("year", "2020");
                        fromYearStr = "2019";
                        toYearStr = "2020";
                    } else if (selectedYear.equals("2020 - 2021")) {
                        jsonObject.addProperty("year", "2021");
                        fromYearStr = "2020";
                        toYearStr = "2021";
                    }
                }else {
                    if (selectedYear.equals("2017 - 2018")){
                        jsonObject.addProperty("year", "2018");
                        fromYearStr = "2017";
                        toYearStr = "2018";
                    } else if (selectedYear.equals("2018 - 2019")) {
                        jsonObject.addProperty("year", "2019");
                        fromYearStr = "2018";
                        toYearStr = "2019";
                    } else if (selectedYear.equals("2019 - 2020")) {
                        jsonObject.addProperty("year", "2020");
                        fromYearStr = "2019";
                        toYearStr = "2020";
                    }
                }

                if(section == 1){
                    PostLoanInterestCertificate postLoanInterestCertificate = new PostLoanInterestCertificate(context);
                    postLoanInterestCertificate.checkServerAvailability(2);
                }else if(section == 2){
                    PostFDInterestCertificate postFDInterestCertificate = new PostFDInterestCertificate(context);
                    postFDInterestCertificate.checkServerAvailability(2);
                }else if(section == 3){
                    PostThriftInterestCertificate postThriftInterestCertificate = new PostThriftInterestCertificate(context);
                    postThriftInterestCertificate.checkServerAvailability(2);
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
                                    loanInterestCertificate.createPDF(pdfView, file,  destination, imageData, nameStr, memberNoStr, moneyStr, fromDateStr, toDateStr, interestRecoveredNo, interestToBeRecoveredNo);
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
                                loanInterestCertificate.createPDF(pdfView, file, destination, imageData, nameStr, memberNoStr, moneyStr, fromDateStr, toDateStr, interestRecoveredNo, interestToBeRecoveredNo);
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
                                fdInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, fromYearStr, toYearStr);
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
                            fdInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, fromYearStr, toYearStr);
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
                                thriftInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, fromYearStr, toYearStr);
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
                            thriftInterestCertificate.createPDF(jsonArray, pdfView, file, destination, imageData, nameStr, memberNoStr, fromYearStr, toYearStr);
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