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
import android.widget.TextView;

import com.komsi.lab.kjurseller.adapter.ProductLogHistoryAdapter;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.model.ProductLogHistory;
import com.komsi.lab.kjurseller.model.ProductLogResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductLogStockActivity extends AppCompatActivity {
    private ArrayList<ProductLogHistory> productLogHistoryList;
    private RecyclerView recyclerView;
    private ProductLogHistoryAdapter plhAdapter;
    private TextView tvProductName, tvProductStockNow;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_log_stock);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvProductName = findViewById(R.id.tvProductName);
        tvProductStockNow = findViewById(R.id.tvProductStockNow);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productLog();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(ProductLogStockActivity.this, null, getString(R.string.please_wait), true, false);
        productLog();
    }

    private void productLog() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productLog("Bearer " + token, productId);

        call.enqueue(new Callback<ProductLogResponse>() {

            @Override
            public void onResponse(Call<ProductLogResponse> call, Response<ProductLogResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ProductLogResponse productLogResponse = response.body();
                    if (productLogResponse.getStatus().equals("success")) {
                        tvProductName.setText(productLogResponse.getProductLogStock().getProductName());
                        tvProductStockNow.setText(String.valueOf(productLogResponse.getProductLogStock().getProductStock()));
                        productLogHistoryList = productLogResponse.getProductLogHistory();
                        recyclerView = (RecyclerView) findViewById(R.id.rvProductLog);
                        plhAdapter = new ProductLogHistoryAdapter(productLogHistoryList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(plhAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductLogResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
