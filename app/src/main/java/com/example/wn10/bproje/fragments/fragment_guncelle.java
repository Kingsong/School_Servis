package com.example.wn10.bproje.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wn10.bproje.Guncelle;
import com.example.wn10.bproje.R;
import com.example.wn10.bproje.Retrofit.api.RetrofitClient;
import com.example.wn10.bproje.Retrofit.models.DefaultResponse;
import com.example.wn10.bproje.Retrofit.models.TekResponse;
import com.example.wn10.bproje.Retrofit.models.VeriResponse;
import com.example.wn10.bproje.Storage.SharedPrefManager;
import com.example.wn10.bproje.Storage.veri;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_guncelle extends Fragment {
    EditText eski_telefon,yeni_telefon,yeni_ad,yeni_soyad,yeni_okul,yeni_vakit;
    Button guncelleme,Cagir;
    ArrayList<String> list = new ArrayList<String>();
    ListView listView;
    ArrayList<veri> dizi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guncellefragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eski_telefon = view.findViewById(R.id.TELEFONNUM);
        yeni_telefon = view.findViewById(R.id.TELEFON);
        yeni_ad = view.findViewById(R.id.AD);
        yeni_soyad = view.findViewById(R.id.SOYAD);
        yeni_okul = view.findViewById(R.id.OKUL);
        yeni_vakit = view.findViewById(R.id.VAKIT);
        guncelleme = view.findViewById(R.id.guncelleme);
        listView = view.findViewById(R.id.listviewer);
        Cagir = view.findViewById(R.id.cagir);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                eski_telefon.setText(dizi.get(i).getTelefonNo());

            }
        });

        Cagir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_tel= eski_telefon.getText().toString().trim();
                if(!old_tel.isEmpty() && old_tel.length() == 11)
                {
                    Call<TekResponse> call=RetrofitClient.getInstance().getApi().getBirOgr(old_tel,SharedPrefManager.getInstance(getActivity()).getPlaka());

                    call.enqueue(new Callback<TekResponse>() {
                        @Override
                        public void onResponse(Call<TekResponse> call, Response<TekResponse> response) {
                            yeni_ad.setText("");
                            yeni_soyad.setText("");
                            yeni_okul.setText("");
                            yeni_vakit.setText("");
                            veri dizi = response.body().getVeri();
                            //eski_telefon.setText(veri.getTelefonNo());
                            yeni_ad.setText(dizi.getAd());
                            yeni_soyad.setText(dizi.getSoyad());
                            yeni_okul.setText(dizi.getOkul());
                            yeni_vakit.setText(dizi.getVakit());
                        }

                        @Override
                        public void onFailure(Call<TekResponse> call, Throwable t) {
                            Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        guncelleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_tel= eski_telefon.getText().toString().trim();
                String new_tel= yeni_telefon.getText().toString().trim();
                String new_ad= yeni_ad.getText().toString().trim();
                String new_soyad= yeni_soyad.getText().toString().trim();
                String new_okul= yeni_okul.getText().toString().trim();
                String new_vakit= yeni_vakit.getText().toString().trim();
                if(old_tel.isEmpty())
                {
                    eski_telefon.setError("Boş Bıraklıdı");
                    eski_telefon.requestFocus();
                    return;
                }
                if(new_tel.isEmpty())
                {
                    yeni_telefon.setError("Boş Bıraklıdı");
                    yeni_telefon.requestFocus();
                    return;
                }
                if(new_ad.isEmpty())
                {
                    yeni_ad.setError("Boş Bıraklıdı");
                    yeni_ad.requestFocus();
                    return;
                }
                if(new_soyad.isEmpty())
                {
                    yeni_soyad.setError("Boş Bıraklıdı");
                    yeni_soyad.requestFocus();
                    return;
                }
                if(new_okul.isEmpty())
                {
                    yeni_okul.setError("Boş Bıraklıdı");
                    yeni_okul.requestFocus();
                    return;
                }
                if(new_vakit.isEmpty())
                {
                    yeni_vakit.setError("Boş Bıraklıdı");
                    yeni_vakit.requestFocus();
                    return;
                }

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateOgr(old_tel,new_okul,new_vakit,new_ad,new_soyad,new_tel);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(getActivity(), dr.getMsg(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });



            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Call<VeriResponse> call = RetrofitClient.getInstance().getApi().getVeri(SharedPrefManager.getInstance(getActivity()).getPlaka());

        call.enqueue(new Callback<VeriResponse>() {
            @Override
            public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                dizi = response.body().getVeriler();
                for(int i = 0 ; i < dizi.size() ; i++)
                {
                    list.add(dizi.get(i).getAd()+" "+dizi.get(i).getSoyad()+" "+dizi.get(i).getTelefonNo()+" -> "+dizi.get(i).getOkul());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<VeriResponse> call, Throwable t) {

            }
        });
    }
}
