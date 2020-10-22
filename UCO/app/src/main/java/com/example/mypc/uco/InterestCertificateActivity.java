package com.example.mypc.uco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class InterestCertificateActivity extends AppCompatActivity {
    Context context = this;

    String memberName;
    String memberNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_certificate);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) (height / 1.7);

        ImageView background = findViewById(R.id.background);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 200, height);
        background.setLayoutParams(layoutParams);

        Intent intent = getIntent();
        memberName = intent.getStringExtra("memberName");
        memberNo = intent.getStringExtra("memberNo");

        Button loan = findViewById(R.id.loan);

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestCertificateActivity.this, InterestCertificatePDFActivity.class);
                intent.putExtra("section", 1);
                intent.putExtra("memberName", memberName);
                intent.putExtra("memberNo", memberNo);
                startActivity(intent);
            }
        });

        Button fixedDeposit = findViewById(R.id.fixedDeposit);

        fixedDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestCertificateActivity.this, InterestCertificatePDFActivity.class);
                intent.putExtra("section", 2);
                intent.putExtra("memberName", memberName);
                intent.putExtra("memberNo", memberNo);
                startActivity(intent);
            }
        });

        Button thrift = findViewById(R.id.thrift);

        thrift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestCertificateActivity.this, InterestCertificatePDFActivity.class);
                intent.putExtra("section", 3);
                intent.putExtra("memberName", memberName);
                intent.putExtra("memberNo", memberNo);
                startActivity(intent);
            }
        });
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
                Intent intent1 = new Intent(InterestCertificateActivity.this, MenuActivity.class);
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
    public void onBackPressed(){
        Intent i = new Intent(InterestCertificateActivity.this, MenuActivity.class);
        i.putExtra("memberNo", memberNo);
        i.putExtra("memberName", memberName);
        i.putExtra("isLogin", "false");
        startActivity(i);
    }
}