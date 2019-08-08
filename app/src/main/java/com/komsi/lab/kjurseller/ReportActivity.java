package com.komsi.lab.kjurseller;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsi.lab.kjurseller.adapter.ReportAdapter;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.model.ReportList;
import com.komsi.lab.kjurseller.model.ReportListResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private TextView dateFrom, dateUntil, tvRStock, tvRSold, tvRRemain, tvRLocation, tvRItem, tvRIncome;
    private LinearLayout layoutResult;
    private int pYear, pMonth, pDay;
    private ArrayList<ReportList> reportList;
    private RecyclerView recyclerView;
    private ReportAdapter rAdapter;
    Context mContext;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        dateFrom = findViewById(R.id.dateFrom);
        dateUntil = findViewById(R.id.dateUntil);
        tvRLocation = findViewById(R.id.tvRLocation);
        tvRStock = findViewById(R.id.tvRStock);
        tvRSold = findViewById(R.id.tvRSold);
        tvRRemain = findViewById(R.id.tvRRemain);
        tvRItem = findViewById(R.id.tvRItem);
        tvRIncome = findViewById(R.id.tvRIncome);

        layoutResult = findViewById(R.id.layoutResult);

        recyclerView = findViewById(R.id.rvReport);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date().getTime());

        dateFrom.setText(currentDate);
        dateUntil.setText(currentDate);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                pYear = c.get(Calendar.YEAR);
                pMonth = c.get(Calendar.MONTH);
                pDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        pYear = year;
                        pMonth = monthOfYear+1;
                        pDay = dayOfMonth;

                        String fm = "" + pMonth;
                        String fd = "" + pDay;
                        if(pMonth < 10){
                            fm = "0" + pMonth;
                        }
                        if (pDay < 10){
                            fd = "0" + pDay;
                        }

                        dateFrom.setText(pYear+"-"+fm+"-"+fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        dateUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                pYear = c.get(Calendar.YEAR);
                pMonth = c.get(Calendar.MONTH);
                pDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        pYear = year;
                        pMonth = monthOfYear+1;
                        pDay = dayOfMonth;

                        String fm = "" + pMonth;
                        String fd = "" + pDay;
                        if(pMonth < 10){
                            fm = "0" + pMonth;
                        }
                        if (pDay < 10){
                            fd = "0" + pDay;
                        }

                        dateUntil.setText(pYear+"-"+fm+"-"+fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                String strDate = dateFrom.getText().toString();
                SimpleDateFormat dateFromFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFromFormat.parse(strDate);
                    long mills = date.getTime();
                    dialog.getDatePicker().setMinDate(mills);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*
                int dateUntilChangedYear = Integer.parseInt(dateUntil.getText().toString().substring(0,4));
                int dateUntilChangedMonth = Integer.parseInt(dateUntil.getText().toString().substring(5,7));
                int dateUntilChangedDay = Integer.parseInt(dateUntil.getText().toString().substring(8,10));
                dialog.getDatePicker().updateDate(dateUntilChangedYear, dateUntilChangedMonth, dateUntilChangedDay);
                */
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                reportList();
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (rAdapter != null) {
                    rAdapter.refreshEvents(reportList);
                }
                reportList();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    private void reportList() {
        String fromDate = dateFrom.getText().toString();
        String untilDate = dateUntil.getText().toString();

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ReportListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .reportList("Bearer " + token, fromDate, untilDate);

        call.enqueue(new Callback<ReportListResponse>() {

            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ReportListResponse reportListResponse = response.body();
                    if (reportListResponse.getStatus().equals("success")) {
                        layoutResult.setVisibility(View.VISIBLE);

                        tvRStock.setText(reportListResponse.getStock());
                        tvRSold.setText(String.valueOf(reportListResponse.getSold()));
                        tvRRemain.setText(String.valueOf(reportListResponse.getRemain()));
                        tvRLocation.setText(String.valueOf(reportListResponse.getLocation()));
                        tvRItem.setText(String.valueOf(reportListResponse.getItem()));
                        tvRIncome.setText(String.valueOf(reportListResponse.getIncome()));
                        reportList = reportListResponse.getReportList();
                        rAdapter = new ReportAdapter(reportList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(rAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                layoutResult.setVisibility(View.GONE);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
