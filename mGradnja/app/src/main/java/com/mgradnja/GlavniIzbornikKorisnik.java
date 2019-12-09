package com.mgradnja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GlavniIzbornikKorisnik extends AppCompatActivity {

    public Spinner spinnerZupanije;
    public Spinner spinnerKategorije;
    public TextView textView;

    public String[] zupanije = new String[]{"Zagrebacka", "Krapinsko-zagorska", "Sisacko-moslavacka",
    "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
    "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
    "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
    "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};

    public ArrayList<String> kategorije = new ArrayList<String>();
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavni_izbornik_korisnik);

        //DOHVAT ID-A KORISNIKA IZ PRIJAŠNJE AKTIVNOSTI
        Intent intent = getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        //textView = findViewById(R.id.txtID);
        //textView.setText(ID.toString());

        spinnerKategorije = findViewById(R.id.spinKategorije);
        spinnerZupanije  = findViewById(R.id.spinZupanije);

        dohvatiKategorije();

        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanije);
        ArrayAdapter<String> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);

        spinnerKategorije.setAdapter(adapterKategorije);
        spinnerZupanije.setAdapter(adapterZupanije);

        ImageView img = (ImageView) findViewById(R.id.imgWrench);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GlavniIzbornikKorisnik.this, JobListActivity.class);
                i.putExtra("ID_korisnika", ID);
                startActivity(i);
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(this, UserSearchActivity.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }








}
