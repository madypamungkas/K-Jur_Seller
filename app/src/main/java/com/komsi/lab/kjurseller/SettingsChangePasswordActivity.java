package com.komsi.lab.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.model.ChangePassUserResponse;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsChangePasswordActivity extends AppCompatActivity {
    private TextView etOldPass, etNewPass, etNewConfirmPass;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_password);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etNewConfirmPass = findViewById(R.id.etNewConfirmPass);

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                changePassUser();
            }
        });
    }

    private void changePassUser() {
        String oldPass = etOldPass.getText().toString().trim();
        String newPass = etNewPass.getText().toString().trim();
        String newConfirmPass = etNewConfirmPass.getText().toString().trim();

        if (oldPass.isEmpty()) {
            loading.dismiss();
            etOldPass.setError("Old Password is required");
            etOldPass.requestFocus();
            return;
        }

        if (oldPass.length() < 8) {
            loading.dismiss();
            etOldPass.setError("Password should be at least 8 characters long");
            etOldPass.requestFocus();
            return;
        }

        if (newPass.isEmpty()) {
            loading.dismiss();
            etNewPass.setError("New Password is required");
            etNewPass.requestFocus();
            return;
        }

        if (newPass.length() < 8) {
            loading.dismiss();
            etNewPass.setError("Password should be at least 8 characters long");
            etNewPass.requestFocus();
            return;
        }

        if (newConfirmPass.isEmpty()) {
            loading.dismiss();
            etNewConfirmPass.setError("Confirm New Password is required");
            etNewConfirmPass.requestFocus();
            return;
        }

        if (newConfirmPass.length() < 8) {
            loading.dismiss();
            etNewConfirmPass.setError("Password should be at least 8 characters long");
            etNewConfirmPass.requestFocus();
            return;
        }

        if (!newPass.equals(newConfirmPass)) {
            loading.dismiss();
            etNewConfirmPass.setError("Password not matching");
            etNewConfirmPass.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ChangePassUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .changePassUser("Bearer " + token, oldPass, newPass, newConfirmPass);

        call.enqueue(new Callback<ChangePassUserResponse>() {

            @Override
            public void onResponse(Call<ChangePassUserResponse> call, Response<ChangePassUserResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ChangePassUserResponse changePassUserResponse = response.body();
                    if (changePassUserResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Password changed", Toast.LENGTH_LONG).show();
                        SettingsChangePasswordActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePassUserResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
