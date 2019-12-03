package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class IstraziIzvodjaceActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    Connection connection;
    String zupanija, kategorija;
    ArrayList<String> izvodjaci;
    ListView lista;
    ListAdapter filterIzvodjaci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istrazi_izvodjace);

        izvodjaci = new ArrayList<>();
        lista = findViewById(R.id.lstIzvodjaci);

        Intent intent = getIntent();
        kategorija = intent.getStringExtra("kategorija");
        zupanija = intent.getStringExtra("zupanija");

        DohvatiIzvodjace(zupanija, kategorija);

        filterIzvodjaci = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, izvodjaci);
        lista.setAdapter(filterIzvodjaci);
    }
    public void DohvatiIzvodjace(String zupanija, String kategorija){
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
        String query = "select Izvodjac.Naziv " +
                "from Izvodjac, Djelatnost" +
                " where Izvodjac.Zupanija='" + zupanija + "' and Djelatnost.Naziv='" + kategorija + "'";
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                //if()
                izvodjaci.add(rs.getString("Naziv"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
