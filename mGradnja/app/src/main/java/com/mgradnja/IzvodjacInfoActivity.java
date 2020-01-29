package com.mgradnja;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class IzvodjacInfoActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    String nazivIzvodjaca;
    String kontaktIzvodjaca;
    ConnectionClass connectionClass;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izvodjac_info);

        Intent intent = getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");
        DohvatiKontaktIzvodjaca(nazivIzvodjaca);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(nazivIzvodjaca);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.myViewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void DohvatiKontaktIzvodjaca(String nazivIzvodjaca) {

        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        try{
            String sql = "SELECT Telefon from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                kontaktIzvodjaca = rs.getString("Telefon");
            }
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.poziv:
                //Toast.makeText(getApplicationContext(), "Poziv", Toast.LENGTH_LONG).show();
                //Intent pozivIntent = new Intent(Intent.ACTION_CALL);
                //pozivIntent.setData(Uri.parse("tel:"+kontaktIzvodjaca));
                //pozivIntent.setData(Uri.parse("tel:"+8802177));
                if (isPermissionGranted()){
                    call_action();
                }
                //startActivity(pozivIntent);
                break;
            case R.id.poruka:
                Toast.makeText(getApplicationContext(), "Poruka", Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                //Intent intent = new Intent(this, UserSearchActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //this.startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void call_action(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + kontaktIzvodjaca));
        startActivity(callIntent);
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else {
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Pristup pozivanju iz aplikacije odbijen", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}