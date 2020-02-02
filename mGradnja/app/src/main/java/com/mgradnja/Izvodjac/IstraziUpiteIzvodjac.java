package com.mgradnja.Izvodjac;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class IstraziUpiteIzvodjac extends AppCompatActivity {

    Integer ID_upita, ID_izvodjaca;
    ConnectionClass connectionClass;
    Connection connection;
    String zupanija, kategorija;
    ArrayList<String> upiti;
    ArrayList<Integer> sifre;
    ListView lista;
    ListAdapter filterUpiti;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istrazi_upite_izvodjac);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        upiti = new ArrayList<>();
        sifre = new ArrayList<>();
        ID_upita = null;
        lista = findViewById(R.id.lstUpiti);

        Intent intent = getIntent();
        ID_izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);
        kategorija = intent.getStringExtra("kategorija");
        zupanija = intent.getStringExtra("zupanija");

        DohvatiUpite(zupanija, kategorija);

        filterUpiti = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, upiti);
        lista.setAdapter(filterUpiti);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0; i<sifre.size(); i++){
                    if(i == position){
                        //Toast.makeText(IstraziUpiteIzvodjac.this, "ID upita je: "+ sifre.get(i) + " ", Toast.LENGTH_LONG ).show();
                        Intent novi = new Intent(IstraziUpiteIzvodjac.this, DetaljiUpita.class);
                        novi.putExtra("ID_upita", sifre.get(i));
                        novi.putExtra("ID_izvodjaca", ID_izvodjaca);
                        startActivity(novi);
                    }
                }
            }
        });
    }

    private void DohvatiUpite(String zupanija, String kategorija) {
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
        Integer idKategorije = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(Calendar.getInstance().getTime());
        try {
            String filter = "select * from Djelatnost where Naziv='" + kategorija + "' ";
            Statement stmtFilter = connection.createStatement();
            ResultSet rsFilter = stmtFilter.executeQuery(filter);
            while (rsFilter.next()){
                if (kategorija.equals(rsFilter.getString("Naziv")))
                    idKategorije = rsFilter.getInt("ID_djelatnosti");
            }

            String sql = "select * from Upit where Zupanija='" + zupanija + "' and ID_djelatnosti='" + idKategorije +"' and Datum >= '" + currentDate +"' " +
                    " and Upit.ID_upita not in (select Upit.ID_upita from Upit, Posao where Upit.ID_upita = Posao.ID_upita)  ";
            Statement stmtSql = connection.createStatement();
            ResultSet rsSql = stmtSql.executeQuery(sql);
            while (rsSql.next()){
                upiti.add(rsSql.getString("Naziv"));
                sifre.add(rsSql.getInt("ID_upita"));
            }

            if(upiti.isEmpty())
                upiti.add("Nema rezultata za vašu pretragu");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
                intent.putExtra("ID_izvodjaca", ID_izvodjaca);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
