package com.example.wn10.bproje;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.wn10.bproje.fragments.fragment_guncelle;
import com.example.wn10.bproje.fragments.fragment_kayit;
import com.example.wn10.bproje.fragments.fragment_listele;
import com.example.wn10.bproje.fragments.fragment_sil;

public class Menu_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activity);

        final int PERMISSION_REQUEST_CODE = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);

        displayFragment(new fragment_listele());
    }

    private void displayFragment(Fragment fragment)
    {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.ekran, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.menu_listele:
                fragment = new fragment_listele();
                break;
            case R.id.menu_kayit:
                fragment = new fragment_kayit();
                break;
            case R.id.menu_sil:
                fragment = new fragment_sil();
                break;
            case R.id.menu_guncelle:
                fragment = new fragment_guncelle();
                break;

        }

        if(fragment != null)
        {
            displayFragment(fragment);
        }

        return false;
    }
}
