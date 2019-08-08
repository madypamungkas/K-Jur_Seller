package com.komsi.lab.kjurseller.api;

import com.komsi.lab.kjurseller.model.BankListResponse;
import com.komsi.lab.kjurseller.model.ChangePassUserResponse;
import com.komsi.lab.kjurseller.model.CloseStoreResponse;
import com.komsi.lab.kjurseller.model.DetailUserResponse;
import com.komsi.lab.kjurseller.model.EditBankResponse;
import com.komsi.lab.kjurseller.model.EditUserResponse;
import com.komsi.lab.kjurseller.model.ForgotPasswordResponse;
import com.komsi.lab.kjurseller.model.GamapayResponse;
import com.komsi.lab.kjurseller.model.LocationResponse;
import com.komsi.lab.kjurseller.model.LocationTodayResponse;
import com.komsi.lab.kjurseller.model.LoginResponse;
import com.komsi.lab.kjurseller.model.LogoutResponse;
import com.komsi.lab.kjurseller.model.NewStockResponse;
import com.komsi.lab.kjurseller.model.ProductListTodayResponse;
import com.komsi.lab.kjurseller.model.ProductListAllResponse;
import com.komsi.lab.kjurseller.model.ProductLogResponse;
import com.komsi.lab.kjurseller.model.ReportListResponse;
import com.komsi.lab.kjurseller.model.ResendVerifyEmailResponse;
import com.komsi.lab.kjurseller.model.UpdateStockResponse;
import com.komsi.lab.kjurseller.model.VerifyEmailResponse;
import com.komsi.lab.kjurseller.model.WithdrawResponse;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<ForgotPasswordResponse> fpUser(
            @Header("Accept") String accept,
            @Field("email") String email);

    @GET("detail")
    Call<DetailUserResponse> detailUser(
            @Header("Authorization") String authToken,
            @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("email/verify")
    Call<VerifyEmailResponse> verifyUser(
            @Header("Authorization") String authToken,
            @Field("verification_code") String verifyCode);

    @GET("email/resend")
    Call<ResendVerifyEmailResponse> resendVerifyUser(
            @Header("Authorization") String authToken);

    @GET("logout")
    Call<LogoutResponse> logoutUser(
            @Header("Authorization") String authToken);

    @GET("lokasi")
    Call<LocationResponse> locAll(
            @Header("Authorization") String authToken);

    @GET("barang-jual/{id}")
    Call<ProductListAllResponse> productListAll(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @FormUrlEncoded
    @POST("stok-barang/create/{id}")
    Call<NewStockResponse> newStock(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("harga") int price,
            @Field("jumlah") int stock);

    @GET("lokasi-today")
    Call<LocationTodayResponse> locToday(
            @Header("Authorization") String authToken);

    @GET("stok-barang/today/{id}")
    Call<ProductListTodayResponse> productListToday(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @GET("stok-barang/history/{id}")
    Call<ProductLogResponse> productLog(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @FormUrlEncoded
    @POST("stok-barang/add-stok/{id}")
    Call<UpdateStockResponse> updateStock(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("add_stok") int stock);

    @PUT("stok-barang/close/{id}")
    Call<CloseStoreResponse> closeStore(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @PUT("stok-barang/close/{id}/stok")
    Call<CloseStoreResponse> closeItem(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("saldo/riwayat")
    Call<GamapayResponse> gamapay(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("pencairan/create")
    Call<WithdrawResponse> withdraw(
            @Header("Authorization") String authToken,
            @Field("total") int total);

    @GET("report")
    Call<ReportListResponse> reportList(
            @Header("Authorization") String authToken,
            @Query("from") String dateFrom,
            @Query("until") String dateUntil);

    @GET("bank")
    Call<BankListResponse> bankList(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @PUT("edit")
    Call<EditUserResponse> editUser(
            @Header("Authorization") String authToken,
            @Field("email") String email,
            @Field("no_telepon") String phone,
            @Field("tanggal_lahir") String birthDate);

    @FormUrlEncoded
    @PUT("edit-bank")
    Call<EditBankResponse> editBank(
            @Header("Authorization") String authToken,
            @Field("bank") int bankName,
            @Field("owner") String bankHolder,
            @Field("account_number") BigInteger bankAccNumber);

    @FormUrlEncoded
    @PUT("edit-password")
    Call<ChangePassUserResponse> changePassUser(
            @Header("Authorization") String authToken,
            @Field("password_current") String passwordCurrent,
            @Field("password") String passwordNew,
            @Field("password_confirmation") String passwordNewConfirm);
}
