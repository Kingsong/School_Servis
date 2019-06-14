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

public class Sil extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<String>();
    ListView listView;
    EditText numara;
    Button sil_islem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sil);
        listView = findViewById(R.id.liste);
        numara = findViewById(R.id.numara);
        sil_islem = findViewById(R.id.sil);

        sil_islem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telno = numara.getText().toString().trim();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteOgr(telno);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();

                        Toast.makeText(getApplicationContext(),dr.getMsg(),Toast.LENGTH_LONG).show();
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
        Call<VeriResponse> call = RetrofitClient.getInstance().getApi().getVeri(SharedPrefManager.getInstance(Sil.this).getPlaka());

        call.enqueue(new Callback<VeriResponse>() {
            @Override
            public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                ArrayList<veri> dizi = response.body().getVeriler();
                for(int i = 0 ; i < dizi.size() ; i++)
                {
                    list.add(dizi.get(i).getAd()+" "+dizi.get(i).getSoyad()+" "+dizi.get(i).getTelefonNo()+" -> "+dizi.get(i).getOkul());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sil.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<VeriResponse> call, Throwable t) {

            }
        });
    }
}
