package com.example.wn10.bproje.Retrofit.api;

import com.example.wn10.bproje.Retrofit.models.DefaultResponse;
import com.example.wn10.bproje.Retrofit.models.TekResponse;
import com.example.wn10.bproje.Retrofit.models.VeriResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("lat") double lat,
            @Field("lng") double lng,
            @Field("okul") String okul,
            @Field("vakit") String vakit,
            @Field("ad") String ad,
            @Field("soyad") String soyad,
            @Field("telefon") String telefon,
            @Field("plaka") String plaka
    );

    @GET("allveri/{plaka}")
    Call<VeriResponse> getVeri(
            @Path("plaka") String plaka
    );


    @GET("getbilgi/{okul}/{vakit}/{plaka}")
    Call<VeriResponse> getBilgi(
            @Path("okul") String okul,
            @Path("vakit") String vakit,
            @Path("plaka") String plaka
    );

    @GET("getbirogr/{telefonno}/{plaka}")
    Call<TekResponse> getBirOgr(
            @Path("telefonno") String telefonno,
            @Path("plaka") String plaka
    );

    @FormUrlEncoded
    @PUT("updateogr/{telefonnum}")
    Call<DefaultResponse> updateOgr(
            @Path("telefonnum") String telefonnum,
            @Field("okul") String okul,
            @Field("vakit") String vakit,
            @Field("ad") String ad,
            @Field("soyad") String soyad,
            @Field("telefon") String telefon
    );

    @GET("getokul/{plaka}")
    Call<VeriResponse> getOkul(
      @Path("plaka") String plaka
    );

    @GET("getvakit/{okul}")
    Call<VeriResponse> getVakit(
            @Path("okul") String okul
    );

    @DELETE("deleteogr/{telefon}")
    Call<DefaultResponse> deleteOgr(@Path("telefon") String telefon);

    @FormUrlEncoded
    @POST("createhesap")
    Call<DefaultResponse> createHesap(
            @Field("plaka") String plaka,
            @Field("sifre") String sifre
    );



}
