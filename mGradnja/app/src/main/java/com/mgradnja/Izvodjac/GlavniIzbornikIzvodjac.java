package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mgradnja.ConnectionClass;
import com.mgradnja.Izvodjac.IstraziUpiteIzvodjac;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GlavniIzbornikIzvodjac extends AppCompatActivity {

    public Spinner spinnerZupanije;
    public Spinner spinnerKategorije;
    public int ID;
    private static ImageView imgAdd;
    public TextView textView;
    public String odabranaZupanija;
    public String odabranaKategorija;
    public Button btnIstrazi;
    public String[] zupanije = new String[]{"Zagrebacka", "Krapinsko-zagorska", "Sisacko-moslavacka",
            "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
            "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
            "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
            "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};
    public ArrayList<String> kategorije = new ArrayList<String>();
    private Integer BrojRadova = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavni_izbornik_izvodjac);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);

        spinnerKategorije = findViewById(R.id.spinKategorijeIzvodjac);
        spinnerZupanije  = findViewById(R.id.spinZupanijeIzvodjac);

        dohvatiKategorije();

        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanije);
        ArrayAdapter<String> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);

        spinnerKategorije.setAdapter(adapterKategorije);
        spinnerZupanije.setAdapter(adapterZupanije);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_izvodjac);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        btnIstrazi = findViewById(R.id.btnIzvodjacIstrazi);
        btnIstrazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabranaKategorija = spinnerKategorije.getSelectedItem().toString();
                odabranaZupanija = spinnerZupanije.getSelectedItem().toString();
                OpenIstraziUpiteActivity(odabranaKategorija, odabranaZupanija);
            }
        });

    }

    private void OpenIstraziUpiteActivity(String odabranaKategorija, String odabranaZupanija) {

        Intent intent = new Intent(this, IstraziUpiteIzvodjac.class);
        intent.putExtra("kategorija", odabranaKategorija);
        intent.putExtra("zupanija", odabranaZupanija);

        startActivity(intent);
    }

    public void dohvatiKategorije(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select Naziv from Djelatnost";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                kategorije.add(rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO NAVIGACIJA
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()){
                        case R.id.nav_home:

                            break;
                        case R.id.nav_wrench:

                            break;
                        case R.id.nav_calendar:

                            break;
                        case R.id.nav_notifications:

                            break;
                        case R.id.nav_profile:

                            break;
                    }

                    return  true;
                }
            };

}
