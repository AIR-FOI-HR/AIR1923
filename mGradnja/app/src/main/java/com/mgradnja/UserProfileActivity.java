package com.mgradnja;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mgradnja.ui.main.SectionsPagerAdapterProfilKorisnik;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserProfileActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    TextView txtImeKorisnika, txtPrezimeKorisnika, txtMailKorisnika, txtTelefonKorisnika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SectionsPagerAdapterProfilKorisnik sectionsPagerAdapter = new SectionsPagerAdapterProfilKorisnik(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        connectionClass = new ConnectionClass();
        txtImeKorisnika = findViewById(R.id.txtImeKorisnika);
        txtPrezimeKorisnika = findViewById(R.id.txtPrezimeKorisnika);
        txtMailKorisnika = findViewById(R.id.txtProfilKorisnikMailIspis);
        txtTelefonKorisnika = findViewById(R.id.txtProfilKorisnikTelefonIspis);


        //DOHVAT ID-A KORISNIKA IZ PRIJAŠNJE AKTIVNOSTI
        Intent intent = getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    dohvatPodatakaPorofil(ID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void dohvatPodatakaPorofil(Integer ID) throws SQLException {
        Connection con = connectionClass.CONN();

        String imeKorisnika = "SELECT Ime from Korisnik WHERE ID_korisnika=" + ID;

        Statement statmImeKorisnika = con.createStatement();

        ResultSet rsImeKorisnika = statmImeKorisnika.executeQuery(imeKorisnika);

        txtImeKorisnika.setText(rsImeKorisnika+"");
    }
}