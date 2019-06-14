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

import com.example.wn10.bproje.R;
import com.example.wn10.bproje.Retrofit.api.RetrofitClient;
import com.example.wn10.bproje.Retrofit.models.DefaultResponse;
import com.example.wn10.bproje.Retrofit.models.VeriResponse;
import com.example.wn10.bproje.Sil;
import com.example.wn10.bproje.Storage.SharedPrefManager;
import com.example.wn10.bproje.Storage.veri;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_sil extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
    ListView listView;
    EditText numara;
    Button sil_islem;
    ArrayList<veri> dizi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.silfragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.liste);
        numara = view.findViewById(R.id.numara);
        sil_islem = view.findViewById(R.id.sil);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                numara.setText(dizi.get(i).getTelefonNo());

            }
        });

        sil_islem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telno = numara.getText().toString().trim();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteOgr(telno);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();

                        Toast.makeText(getActivity(),dr.getMsg(),Toast.LENGTH_LONG).show();
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
