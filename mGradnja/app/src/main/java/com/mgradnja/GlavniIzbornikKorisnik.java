package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GlavniIzbornikKorisnik extends AppCompatActivity {

    public Spinner spinnerZupanije;
    public Spinner spinnerKategorije;

    public String[] zupanije = new String[]{"Zagrebačka", "Krapinsko-zagorska", "Sisacko-moslavacka",
    "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
    "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
    "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
    "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};

    public ArrayList<String> kategorije = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavni_izbornik_korisnik);

        spinnerKategorije = findViewById(R.id.spinKategorije);
        spinnerZupanije  = findViewById(R.id.spinZupanije);

        dohvatiKategorije();

        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanije);
        ArrayAdapter<String> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);

        spinnerKategorije.setAdapter(adapterKategorije);
        spinnerZupanije.setAdapter(adapterZupanije);


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
}
