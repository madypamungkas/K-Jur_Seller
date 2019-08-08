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
import android.widget.Toast;

import com.komsi.lab.kjurseller.adapter.ProductListOSAdapter;
import com.komsi.lab.kjurseller.model.ProductListAllResponse;
import com.komsi.lab.kjurseller.model.ProductAll;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductOpenStoreActivity extends AppCompatActivity {
    private ArrayList<ProductAll> productOpenStoreList;
    private RecyclerView recyclerView;
    private ProductListOSAdapter plAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_open_store);

        mContext = this;

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
                if (plAdapter != null) {
                    plAdapter.refreshEvents(productOpenStoreList);
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
        loading = ProgressDialog.show(ProductOpenStoreActivity.this, null, getString(R.string.please_wait), true, false);
        productList();
    }

    private void productList() {
        Intent intent = getIntent();
        int locAllId = intent.getIntExtra("locAllId", 0);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductListAllResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productListAll("Bearer " + token, locAllId);

        call.enqueue(new Callback<ProductListAllResponse>() {

            @Override
            public void onResponse(Call<ProductListAllResponse> call, Response<ProductListAllResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    productOpenStoreList = response.body().getProduct();
                    recyclerView = (RecyclerView) findViewById(R.id.rvProduct);
                    plAdapter = new ProductListOSAdapter(productOpenStoreList, mContext);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(plAdapter);
                    if( eLayoutManager.getItemCount() == 0 ){
                        //Do something
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListAllResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(ProductOpenStoreActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
