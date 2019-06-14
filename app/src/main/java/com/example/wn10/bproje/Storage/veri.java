package com.example.wn10.bproje.Storage;

public class veri {
    double lat;
    double lng;
    String Okul;
    String Vakit;
    String Ad;
    String Soyad;
    String TelefonNo;
    String Plaka;


    public veri( double lat, double lng, String okul, String vakit, String ad, String soyad, String telefonNo, String plaka) {
        this.lat = lat;
        this.lng = lng;
        Okul = okul;
        Vakit = vakit;
        Ad = ad;
        Soyad = soyad;
        TelefonNo = telefonNo;
        Plaka = plaka;
    }
    

    public veri()
    {

    }

    public String getOkul() {
        return Okul;
    }

    public void setOkul(String okul) {
        Okul = okul;
    }

    public String getVakit() {
        return Vakit;
    }

    public void setVakit(String vakit) {
        Vakit = vakit;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }

    public String getTelefonNo() {
        return TelefonNo;
    }

    public void setTelefonNo(String telefonNo) {
        TelefonNo = telefonNo;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaka() {
        return Plaka;
    }

    public void setPlaka(String plaka) {
        Plaka = plaka;
    }
}
