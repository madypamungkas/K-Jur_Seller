package com.komsi.lab.kjurseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class LiveReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_report);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayout llLiveSales = findViewById(R.id.llLiveSales);
        llLiveSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LiveReportActivity.this, LocLiveReportLSActivity.class);
                startActivity(i);
            }
        });

        LinearLayout llUpdateStock = findViewById(R.id.llUpdateStock);
        llUpdateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LiveReportActivity.this, LocLiveReportUSActivity.class);
                startActivity(i);
            }
        });
    }
}
