package com.mgradnja.Korisnik;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GlavniIzbornikKorisnik extends AppCompatActivity {

    public Spinner spinnerZupanije;
    public Spinner spinnerKategorije;

    public Integer ID;

    public String odabranaZupanija;
    public String odabranaKategorija;
    public Button btnIstrazi;

    public String[] zupanije = new String[]{"Zagrebacka", "Krapinsko-zagorska", "Sisacko-moslavacka",
    "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
    "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
    "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
    "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};

    public ArrayList<String> kategorije = new ArrayList<String>();
    public ArrayList<Integer> ListaPrihvacenihPonuda = new ArrayList<Integer>();
    public ArrayList<Integer> ListaNeprihvacenihPonuda = new ArrayList<Integer>();
    private Integer BrojRadova = 0;
    private Integer BrojPonuda = 0;
    public  Integer IDUpita = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavni_izbornik_korisnik);

        //DOHVAT ID-A KORISNIKA IZ PRIJAŠNJE AKTIVNOSTI
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

        //textView = findViewById(R.id.txtID);
        //textView.setText(ID.toString());

        spinnerKategorije = findViewById(R.id.spinKategorije);
        spinnerZupanije  = findViewById(R.id.spinZupanije);

        dohvatiKategorije();

        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanije);
        ArrayAdapter<String> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);

        spinnerKategorije.setAdapter(adapterKategorije);
        spinnerZupanije.setAdapter(adapterZupanije);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_korisnik);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        btnIstrazi = findViewById(R.id.btnIstrazi);
        btnIstrazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabranaKategorija = spinnerKategorije.getSelectedItem().toString();
                odabranaZupanija = spinnerZupanije.getSelectedItem().toString();
                OpenIstraziIzvodjaceActivity(odabranaKategorija, odabranaZupanija, ID);
            }
        });

    }

    public void openQueryActivity(Integer ID){
        Intent intent = new Intent(this, QueryActivity.class);
        intent.putExtra("ID_korisnika", ID);
        startActivity(intent);
    }

    public void otvoriObavijesti(Integer ID){
        Intent intent = new Intent(this, ObavijestiKorisnik.class);
        intent.putExtra("ID_korisnika", ID);
        startActivity(intent);
    }

    private void OpenIstraziIzvodjaceActivity(String odabranaKategorija, String odabranaZupanija, int ID) {
        Intent intent = new Intent(this, IstraziIzvodjaceActivity.class);
        intent.putExtra("kategorija", odabranaKategorija);
        intent.putExtra("zupanija", odabranaZupanija);
        intent.putExtra("ID_korisnika", ID);
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

    public void ProvjeriRadove(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select * from posao p inner join upit u on u.ID_upita = p.ID_upita where u.ID_korisnika = '" + ID + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                BrojRadova++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void ProvjeriPonude() {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select * from ponuda p inner join upit u on u.ID_upita = p.ID_upita where p.status = 0 and u.ID_korisnika = '" + ID + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
              // IDUpita = rs.getInt("ID_upita");
               ListaNeprihvacenihPonuda.add(rs.getInt("ID_upita"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "select * from ponuda p inner join upit u on u.ID_upita = p.ID_upita where p.status = 1 and u.ID_korisnika = '" + ID + "'";
        try {
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(query2);

            while(rs2.next()){
                // IDUpita = rs.getInt("ID_upita");
                ListaPrihvacenihPonuda.add(rs2.getInt("ID_upita"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0;i<ListaPrihvacenihPonuda.size();i++){
            for(int j = 0;j<ListaNeprihvacenihPonuda.size();j++){
                if(ListaPrihvacenihPonuda.get(i) == ListaNeprihvacenihPonuda.get(j)) {
                    ListaNeprihvacenihPonuda.remove(j);
                }
            }

        }
        BrojPonuda = ListaNeprihvacenihPonuda.size();
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
                intent.putExtra("ID_korisnika", ID);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void otvoriProfil(Integer ID) {
        Intent intent = new Intent(this, ProfilKorisnikActivity.class);
        intent.putExtra("ID_korisnika", ID);
        startActivity(intent);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()){
                        case R.id.nav_assignment:
                            ProvjeriPonude();
                            if(BrojPonuda == 0) Toast.makeText(GlavniIzbornikKorisnik.this , "Nemate nijednu ponudu!" , Toast.LENGTH_LONG).show();
                            else{
                                Intent i = new Intent(GlavniIzbornikKorisnik.this, OfferListActivity.class);
                                i.putExtra("ID_korisnika", ID);
                                startActivity(i);
                            }


                            break;
                        case R.id.nav_wrench:

                            ProvjeriRadove();
                            if(BrojRadova == 0) Toast.makeText(GlavniIzbornikKorisnik.this , "Nemate nijedan posao!" , Toast.LENGTH_LONG).show();
                            else {
                                Intent in = new Intent(GlavniIzbornikKorisnik.this, JobListActivity.class);
                                in.putExtra("ID_korisnika", ID);
                                startActivity(in);
                            }

                            break;
                        case R.id.nav_upit:

                            openQueryActivity(ID);

                            break;
                        case R.id.nav_notifications:

                            otvoriObavijesti(ID);

                            break;
                        case R.id.nav_profile:

                            otvoriProfil(ID);

                            break;
                    }

                    return  true;
                }
            };

    @Override
    public void onBackPressed(){
        finish();
    }
}
