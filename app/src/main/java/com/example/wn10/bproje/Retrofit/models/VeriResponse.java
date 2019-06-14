package com.example.wn10.bproje.Retrofit.models;

import com.example.wn10.bproje.Storage.veri;

import java.util.ArrayList;

public class VeriResponse {

    private boolean error;
    private ArrayList<veri> veriler;

    public VeriResponse(boolean error, ArrayList<veri> veriler) {
        this.error = error;
        this.veriler = veriler;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<veri> getVeriler() {
        return veriler;
    }
}
