package com.example.wn10.bproje.Retrofit.models;

import com.example.wn10.bproje.Storage.veri;

public class TekResponse {
    private boolean error;
    private veri veri;

    public TekResponse(boolean error, veri veri) {
        this.error = error;
        this.veri = veri;
    }

    public boolean isError() {
        return error;
    }

    public veri getVeri() {
        return veri;
    }
}
