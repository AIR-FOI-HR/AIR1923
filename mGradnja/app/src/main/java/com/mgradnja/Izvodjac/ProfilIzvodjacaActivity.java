package com.mgradnja.Izvodjac;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mgradnja.Izvodjac.FragmentProfilIzvodjacaOpis;
import com.mgradnja.Izvodjac.FragmentProfilIzvodjacaRecenzije;
import com.mgradnja.Izvodjac.ProfilIzvodjacaActivity;
import com.mgradnja.R;
import com.mgradnja.Izvodjac.ViewPagerAdapterProfileIzvodjaca;

public class ProfilIzvodjacaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    Integer ID;
    private Context context = ProfilIzvodjacaActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_izvodjaca);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Korisnički račun");
        }

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);

        tabLayout = findViewById(R.id.tabsIzvodjac);
        viewPager = findViewById(R.id.view_pager_Izvodjac);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapterProfileIzvodjaca viewPagerAdapterProfileIzvodjaca = new ViewPagerAdapterProfileIzvodjaca(getSupportFragmentManager());
        viewPagerAdapterProfileIzvodjaca.addFragment(new FragmentProfilIzvodjacaOpis(), "OPIS");
        viewPagerAdapterProfileIzvodjaca.addFragment(new FragmentProfilIzvodjacaRecenzije(), "RECENZIJE");

        viewPager.setAdapter(viewPagerAdapterProfileIzvodjaca);


    }
}
