package com.komsi.lab.kjurseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.komsi.lab.kjurseller.adapter.LocationTodayCSAdapter;
import com.komsi.lab.kjurseller.model.LocationToday;
import com.komsi.lab.kjurseller.model.LocationTodayResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocCloseStoreActivity extends AppCompatActivity {
    private ArrayList<LocationToday> locationTodayList;
    private RecyclerView recyclerView;
    private LocationTodayCSAdapter lAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_close_store);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lAdapter != null) {
                    lAdapter.refreshEvents(locationTodayList);
                }
                locToday();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(LocCloseStoreActivity.this, null, getString(R.string.please_wait), true, false);
        locToday();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LocCloseStoreActivity.this, DailySaleActivity.class);
        LocCloseStoreActivity.this.finish();
        startActivity(i);
    }

    private void locToday() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<LocationTodayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .locToday("Bearer " + token);

        call.enqueue(new Callback<LocationTodayResponse>() {

            @Override
            public void onResponse(Call<LocationTodayResponse> call, Response<LocationTodayResponse> response) {
                loading.dismiss();
                LocationTodayResponse locationTodayResponse = response.body();
                if (response.isSuccessful()) {
                    if (locationTodayResponse.getStatus().equals("success")) {
                        locationTodayList = locationTodayResponse.getLocationToday();
                        recyclerView = (RecyclerView) findViewById(R.id.rvLocToday);
                        lAdapter = new LocationTodayCSAdapter(locationTodayList, LocCloseStoreActivity.this);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(lAdapter);
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LocationTodayResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(LocCloseStoreActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}
