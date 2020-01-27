package com.mgradnja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ProfilKorisnikActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_korisnik);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Korisnički račun");
        }

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profil_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
                intent.putExtra("ID_korisnika", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
            case R.id.logout:
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                Intent intentOut = new Intent(this, MainActivity.class);
                intentOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intentOut);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
        intent.putExtra("ID_korisnika", ID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();
    }


}