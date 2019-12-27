package com.mgradnja.Izvodjac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetaljiUpita extends AppCompatActivity {

    Integer ID_upita, ID_izvodjaca;
    Upit upit;

    TextView naziv, opis, datum, adresa, grad, zupanija;
    Button Posalji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji_upita);

        Intent intent = getIntent();
        ID_izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);
        ID_upita = intent.getIntExtra("ID_upita", 0);

        DohvatiUpit(ID_upita);

        naziv = findViewById(R.id.nazivUpita);
        opis = findViewById(R.id.opisUpita);
        datum = findViewById(R.id.datumUpita);
        adresa = findViewById(R.id.adresaUpita);
        grad = findViewById(R.id.gradUpita);
        zupanija = findViewById(R.id.zupanijaUpita);

        naziv.setText(upit.naziv);
        opis.setText(upit.opis);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dat = df.format(upit.datum);
        datum.setText(dat);
        adresa.setText(upit.adresa);
        grad.setText(upit.grad);
        zupanija.setText(upit.zupanija);
        Intent intent9 = new Intent(this, OfferActivity.class);

        Posalji = findViewById(R.id.btnPosaljiPonudu);
        Posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent9.putExtra("ID_izvodjaca", ID_izvodjaca);
                intent9.putExtra("ID_upita", ID_upita);
                startActivity(intent9);
            }
        });
    }

    private void DohvatiUpit(Integer id_upita) {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select * from Upit where ID_upita='" + id_upita + "' ";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                upit = new Upit(rs.getInt("ID_upita"), rs.getString("Naziv"),
                        rs.getDate("Datum"), rs.getString("Adresa"),
                        rs.getString("Grad"), rs.getString("Zupanija"),
                        rs.getString("Opis"), rs.getInt("ID_korisnika") );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Početna");
        item.setIcon(R.drawable.ic_home_white_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(DetaljiUpita.this, GlavniIzbornikIzvodjac.class);
                intent.putExtra("ID_izvodjaca", ID_izvodjaca);
                startActivity(intent);

                return false;
            }
        });

        //TODO ??
        return true;
    }
}

class Upit {
    int ID_upita, ID_korisnika;
    String naziv;
    Date datum;
    String adresa, grad, zupanija, opis;

    public Upit(int ID_upita, String naziv, Date datum, String adresa, String grad, String zupanija, String opis, int ID_korisnika) {
        this.ID_upita = ID_upita;
        this.naziv = naziv;
        this.datum = datum;
        this.adresa = adresa;
        this.grad = grad;
        this.zupanija = zupanija;
        this.opis = opis;
        this.ID_korisnika = ID_korisnika;
    }
}
