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

import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.model.UpdateStockResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductUpdateStockActivity extends AppCompatActivity {
    private TextView tvProductName, tvCurrentStock;
    private EditText etStock;
    private Button btnConfirm;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update_stock);

        mContext = this;

        tvProductName = findViewById(R.id.tvProductName);
        tvCurrentStock = findViewById(R.id.tvCurrentStock);
        etStock = findViewById(R.id.etStock);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        int productStock = intent.getIntExtra("productStock", 0);

        tvProductName.setText(productName);
        tvCurrentStock.setText(String.valueOf(productStock));

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

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                updateStock();
            }
        });
    }

    private void updateStock() {
        String stockET = etStock.getText().toString().trim();

        if (stockET.isEmpty()) {
            loading.dismiss();
            etStock.setError("Stock is required");
            etStock.requestFocus();
            return;
        }

        int stock = Integer.parseInt(stockET);
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<UpdateStockResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateStock("Bearer " + token, productId, stock);

        call.enqueue(new Callback<UpdateStockResponse>() {

            @Override
            public void onResponse(Call<UpdateStockResponse> call, Response<UpdateStockResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    UpdateStockResponse updateStockResponse = response.body();
                    if (updateStockResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Stock added", Toast.LENGTH_LONG).show();
                        ProductUpdateStockActivity.this.finish();
                    } else {
                        Toast.makeText(mContext, updateStockResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateStockResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
