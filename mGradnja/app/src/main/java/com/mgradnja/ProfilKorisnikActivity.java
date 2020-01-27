package com.mgradnja;

import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ProfilKorisnikActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    //private Toolbar toolbar;
    //private AppBarLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_korisnik);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapterProfilKorisnika viewPagerAdapterProfilKorisnika = new ViewPagerAdapterProfilKorisnika(getSupportFragmentManager());
        viewPagerAdapterProfilKorisnika.addFragment(new FragmentOpisProfilKorisnika(), "OPIS");
        viewPagerAdapterProfilKorisnika.addFragment(new FragmentRecenzijeProfilKorisnika(), "RECENZIJA");
        viewPagerAdapterProfilKorisnika.addFragment(new FragmentUpitiKorisnika(), "UPITI");

        viewPager.setAdapter(viewPagerAdapterProfilKorisnika);

    }



}