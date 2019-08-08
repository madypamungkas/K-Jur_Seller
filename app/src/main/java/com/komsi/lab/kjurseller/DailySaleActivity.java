package com.komsi.lab.kjurseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DailySaleActivity extends AppCompatActivity {
    private TextView calenderHour, calenderMinute, calenderDay, calenderMonth, calenderDate, calenderYear;
    private int hour, minute, currentDate, currentMonth, currentYear, currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sale);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        calenderHour = findViewById(R.id.calenderHour);
        calenderMinute = findViewById(R.id.calenderMinute);
        calenderDay = findViewById(R.id.calenderDay);
        calenderMonth = findViewById(R.id.calenderMonth);
        calenderDate = findViewById(R.id.calenderDate);
        calenderYear = findViewById(R.id.calenderYear);

        LinearLayout openStore = this.findViewById(R.id.btnOpenStore);
        openStore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DailySaleActivity.this, LocOpenStoreActivity.class);
                startActivity(i);
            }
        });

        LinearLayout liveReport = this.findViewById(R.id.btnLiveReport);
        liveReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DailySaleActivity.this, LiveReportActivity.class);
                startActivity(i);
            }
        });

        LinearLayout closeStore = this.findViewById(R.id.btnCloseStore);
        closeStore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DailySaleActivity.this, LocCloseStoreActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        //Date currentTime = localCalendar.getTime();
        hour = localCalendar.get(Calendar.HOUR_OF_DAY);
        minute = localCalendar.get(Calendar.MINUTE);
        currentDate = localCalendar.get(Calendar.DATE);
        currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        currentYear = localCalendar.get(Calendar.YEAR);
        currentDay = localCalendar.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdfDay.format(d);

        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM");
        String monthOfTheYear = sdfMonth.format(d);

        calenderHour.setText(String.valueOf(hour));
        calenderMinute.setText(String.valueOf(minute));
        calenderDay.setText(dayOfTheWeek);
        calenderMonth.setText(monthOfTheYear);
        calenderDate.setText(String.valueOf(currentDate));
        calenderYear.setText(String.valueOf(currentYear));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DailySaleActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
