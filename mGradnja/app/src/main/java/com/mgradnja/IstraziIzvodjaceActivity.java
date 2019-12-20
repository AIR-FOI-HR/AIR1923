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
import java.util.List;

public class IstraziIzvodjaceActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    Connection connection;
    String zupanija, kategorija;
    ArrayList<String> izvodjaci;
    ArrayList<Integer> nefiltrirano;
    ListView lista;
    ListAdapter filterIzvodjaci;
    private  int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istrazi_izvodjace);

        izvodjaci = new ArrayList<>();
        nefiltrirano = new ArrayList<>();
        lista = findViewById(R.id.lstIzvodjaci);

        Intent intent = getIntent();
        ID=intent.getIntExtra("ID_korisnika", 0);
        kategorija = intent.getStringExtra("kategorija");
        zupanija = intent.getStringExtra("zupanija");

        DohvatiIzvodjace(zupanija, kategorija);

        filterIzvodjaci = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, izvodjaci);
        lista.setAdapter(filterIzvodjaci);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            String nazivIzvodjaca = (String) lista.getItemAtPosition(position);
            OpenIzvodjacInfoActivity(nazivIzvodjaca);
        });
    }

    private void OpenIzvodjacInfoActivity(String nazivIzvodjaca){
        Intent intent = new Intent(this, IzvodjacInfoActivity.class);
        intent.putExtra("nazivIzvodjaca", nazivIzvodjaca);

        startActivity(intent);
    }
    
    public void DohvatiIzvodjace(String zupanija, String kategorija){
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
        Integer idKategorije = null;

        try {
            String filter = "select * from Djelatnost where Naziv='" + kategorija + "' ";
            Statement stmtFilter = connection.createStatement();
            ResultSet rsFilter = stmtFilter.executeQuery(filter);
            while (rsFilter.next()){
                if (kategorija.equals(rsFilter.getString("Naziv")))
                    idKategorije = rsFilter.getInt("ID_djelatnosti");
            }

            String sql = "select * from Djelatnosti_izvodjaca where ID_djelatnosti='" + idKategorije +"'";
            Statement stmtSql = connection.createStatement();
            ResultSet rsSql = stmtSql.executeQuery(sql);
            while (rsSql.next()){
                nefiltrirano.add(rsSql.getInt("ID_izvodjaca"));
            }

            String query = "select Naziv, ID_izvodjaca from Izvodjac where Zupanija='"+zupanija+"' ";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if(nefiltrirano.contains(rs.getInt("ID_izvodjaca")))
                    izvodjaci.add(rs.getString("Naziv"));
            }

            if(izvodjaci.isEmpty())
                izvodjaci.add("Nema rezultata za vašu pretragu");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
