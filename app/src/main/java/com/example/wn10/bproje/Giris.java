package com.example.wn10.bproje;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wn10.bproje.Storage.SharedPrefManager;

public class Giris extends AppCompatActivity {
    Button Onayla;
    EditText Plaka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        Onayla = findViewById(R.id.Onayla);
        Plaka = findViewById(R.id.Plaka);

        Onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Giris.this);
                builder.setTitle("Emin misiniz ?");
                builder.setMessage("Unutmayın Giridiğniz Plaka altında tüm Öğrenci Bilgileri Tutulacak !");
                builder.setPositiveButton("Eminim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String plaka = Plaka.getText().toString().trim().toUpperCase();
                        SharedPrefManager.getInstance(Giris.this).savePlaka(plaka);

                        Toast.makeText(getApplicationContext(),"Plaka Kayıt Edildi -> "+SharedPrefManager.getInstance(Giris.this).getPlaka(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Giris.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Değilim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }
}
