package com.komsi.lab.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjurseller.model.NewStockResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductNewStockActivity extends AppCompatActivity {
    private EditText etStock, etPrice;
    private Button btnConfirm;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_stock);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        int productPrice = intent.getIntExtra("productPrice", 0);

        TextView tvProductName = this.findViewById(R.id.tvProductName);
        tvProductName.setText(productName);

        etStock = findViewById(R.id.etStock);
        etPrice = findViewById(R.id.etPrice);
        etPrice.setText(String.valueOf(productPrice));

        mContext = this;

        etStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stock = etStock.getText().toString();

                if (stock.isEmpty()) {
                    etStock.setError("Stock required");
                    btnConfirm.setEnabled(false);
                } else if (stock.length() > 0) {
                    int stockValue = Integer.parseInt(stock);
                    if (stockValue < 1) {
                        etStock.setError("Stock should be at least 1 item");
                        btnConfirm.setEnabled(false);
                    } else {
                        etStock.setError(null);
                        btnConfirm.setEnabled(true);
                    }
                } else {
                    etStock.setError(null);
                    btnConfirm.setEnabled(true);
                }
            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String price = etPrice.getText().toString();

                if (price.isEmpty()) {
                    etPrice.setError("Price required");
                    btnConfirm.setEnabled(false);
                } else if (price.length() > 0) {
                    int priceValue = Integer.parseInt(price);
                    if (priceValue < 1) {
                        etPrice.setError("Price can't be less than Rp. 100");
                        btnConfirm.setEnabled(false);
                    } else {
                        etPrice.setError(null);
                        btnConfirm.setEnabled(true);
                    }
                } else {
                    etPrice.setError(null);
                    btnConfirm.setEnabled(true);
                }
            }
        });

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                newStock();
            }
        });
    }

    private void newStock() {
        String stockET = etStock.getText().toString().trim();
        String priceET = etPrice.getText().toString().trim();

        if (stockET.isEmpty()) {
            loading.dismiss();
            etStock.setError("Stock is required");
            etStock.requestFocus();
            return;
        }

        if (priceET.isEmpty()) {
            loading.dismiss();
            etStock.setError("Price is required");
            etStock.requestFocus();
            return;
        }

        int stock = Integer.parseInt(stockET);
        int price = Integer.parseInt(priceET);
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<NewStockResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .newStock("Bearer " + token, productId, price, stock);

        call.enqueue(new Callback<NewStockResponse>() {

            @Override
            public void onResponse(Call<NewStockResponse> call, Response<NewStockResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    NewStockResponse newStockResponse = response.body();
                    if (newStockResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                        ProductNewStockActivity.this.finish();
                    } else {
                        Toast.makeText(mContext, newStockResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewStockResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}