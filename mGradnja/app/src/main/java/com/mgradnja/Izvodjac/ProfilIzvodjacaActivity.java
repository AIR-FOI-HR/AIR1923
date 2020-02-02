package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.mgradnja.MainActivity;
import com.mgradnja.R;

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
                Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
                intent.putExtra("ID_izvodjaca", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
            case R.id.logout:
                dialogBox();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
        intent.putExtra("ID_izvodjaca", ID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();
    }

    private void dialogBox(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();

                        Intent intentOut = new Intent(getApplicationContext(), MainActivity.class);
                        intentOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentOut);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        //Toast.makeText(getApplicationContext(), "Ne", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Jeste li sigurni da se želite odjaviti?")
                .setPositiveButton("Odjavite se", dialogClickListener)
                .setNeutralButton("Odustani", dialogClickListener).show();

    }
}
