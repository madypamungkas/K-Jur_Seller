package com.komsi.lab.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.komsi.lab.kjurseller.adapter.ProductListCSAdapter;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.model.CloseStoreResponse;
import com.komsi.lab.kjurseller.model.ProductListTodayResponse;
import com.komsi.lab.kjurseller.model.ProductToday;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCloseStoreActivity extends AppCompatActivity {
    private ArrayList<ProductToday> productCloseStoreList;
    private RecyclerView recyclerView;
    private ProductListCSAdapter plAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_close_store);

        mContext = this;

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvCloseAll = findViewById(R.id.tvCloseAll);
        tvCloseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeStore();
            }
        });

        recyclerView = findViewById(R.id.rvProduct);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (plAdapter != null) {
                    plAdapter.refreshEvents(productCloseStoreList);
                }
                productList();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(ProductCloseStoreActivity.this, null, getString(R.string.please_wait), true, false);
        productList();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProductCloseStoreActivity.this, LocCloseStoreActivity.class);
        ProductCloseStoreActivity.this.finish();
        startActivity(i);
    }

    private void productList() {
        Intent intent = getIntent();
        int locTodayId = intent.getIntExtra("locTodayId", 0);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductListTodayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productListToday("Bearer " + token, locTodayId);

        call.enqueue(new Callback<ProductListTodayResponse>() {

            @Override
            public void onResponse(Call<ProductListTodayResponse> call, Response<ProductListTodayResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ProductListTodayResponse productListTodayResponse = response.body();
                    if (productListTodayResponse.getStatus().equals("success")) {
                        productCloseStoreList = productListTodayResponse.getProduct();
                        plAdapter = new ProductListCSAdapter(productCloseStoreList, mContext);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(plAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListTodayResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void closeStore() {
        Intent intent = getIntent();
        int locTodayId = intent.getIntExtra("locTodayId", 0);

        User user = SharedPrefManager.getInstance(mContext).getUser();
        String token = user.getToken();
        Call<CloseStoreResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .closeStore("Bearer " + token, locTodayId);

        call.enqueue(new Callback<CloseStoreResponse>() {

            @Override
            public void onResponse(Call<CloseStoreResponse> call, Response<CloseStoreResponse> response) {
                if (response.isSuccessful()) {
                    CloseStoreResponse closeStoreResponse = response.body();
                    if (closeStoreResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, closeStoreResponse.getMessage(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<CloseStoreResponse> call, Throwable t) {
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
