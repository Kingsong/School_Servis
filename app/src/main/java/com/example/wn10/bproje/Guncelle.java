package com.example.wn10.bproje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wn10.bproje.Retrofit.api.RetrofitClient;
import com.example.wn10.bproje.Retrofit.models.DefaultResponse;
import com.example.wn10.bproje.Retrofit.models.VeriResponse;
import com.example.wn10.bproje.Storage.SharedPrefManager;
import com.example.wn10.bproje.Storage.veri;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Guncelle extends AppCompatActivity {
    EditText eski_telefon,yeni_telefon,yeni_ad,yeni_soyad,yeni_okul,yeni_vakit;
    Button guncelleme;
    ArrayList<String> list = new ArrayList<String>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelle);
        eski_telefon = findViewById(R.id.TELEFONNUM);
        yeni_telefon = findViewById(R.id.TELEFON);
        yeni_ad = findViewById(R.id.AD);
        yeni_soyad = findViewById(R.id.SOYAD);
        yeni_okul = findViewById(R.id.OKUL);
        yeni_vakit = findViewById(R.id.VAKIT);
        guncelleme = findViewById(R.id.guncelleme);
        listView = findViewById(R.id.listviewer);

        guncelleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_tel= eski_telefon.getText().toString().trim();
                String new_tel= yeni_telefon.getText().toString().trim();
                String new_ad= yeni_ad.getText().toString().trim();
                String new_soyad= yeni_soyad.getText().toString().trim();
                String new_okul= yeni_okul.getText().toString().trim();
                String new_vakit= yeni_vakit.getText().toString().trim();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateOgr(old_tel,new_okul,new_vakit,new_ad,new_soyad,new_tel);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(Guncelle.this, dr.getMsg(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });



            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Call<VeriResponse> call = RetrofitClient.getInstance().getApi().getVeri(SharedPrefManager.getInstance(Guncelle.this).getPlaka());

        call.enqueue(new Callback<VeriResponse>() {
            @Override
            public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                ArrayList<veri> dizi = response.body().getVeriler();
                for(int i = 0 ; i < dizi.size() ; i++)
                {
                    list.add(dizi.get(i).getAd()+" "+dizi.get(i).getSoyad()+" "+dizi.get(i).getTelefonNo()+" -> "+dizi.get(i).getOkul());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Guncelle.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<VeriResponse> call, Throwable t) {

            }
        });
    }
}
