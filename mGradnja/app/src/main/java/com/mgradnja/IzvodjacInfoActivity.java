package com.mgradnja;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;



public class IzvodjacInfoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    String nazivIzvodjaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izvodjac_info);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.myViewPager);

        //setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapterIzvodjacInfo viewPagerAdapterIzvodjacInfo = new ViewPagerAdapterIzvodjacInfo(getSupportFragmentManager());
        viewPagerAdapterIzvodjacInfo.addFragment(new FragmentIzvodjacDetalji(), "DETALJI");
        viewPagerAdapterIzvodjacInfo.addFragment(new FragmentIzvodjacRecenzije(), "RECENZIJE");
        viewPagerAdapterIzvodjacInfo.addFragment(new FragmentIzvodjacKalendar(), "KALENDAR");
        viewPager.setAdapter(viewPagerAdapterIzvodjacInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, UserSearchActivity.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}