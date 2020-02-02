package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mgradnja.Adapters.JobAdapterIzvodjac;
import com.mgradnja.ConnectionClass;
import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class JobListActivityIzvodjac extends AppCompatActivity {
    ConnectionClass connectionClass;
    String hh = "Poslovi";
    private Integer ID;
    private int poz = 0;

    public String[] poslovi = new String[]{"Trenutni poslovi", "Prošli poslovi", "Budući poslovi"};

    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaDjelatnostID = new ArrayList<Integer>();
    public ArrayList<String> ListaIme = new ArrayList<String>();
    public ArrayList<String> ListaPrezime = new ArrayList<String>();
    public ArrayList<Float> ListaCijena = new ArrayList<Float>();
    public ArrayList<Date> ListaPocetka = new ArrayList<Date>();
    public ArrayList<Date> ListaZavrsetks = new ArrayList<Date>();
    public ArrayList<String> ListaOpisa = new ArrayList<String>();
    public ArrayList<String> ListaNaziva = new ArrayList<String>();


    private int brojUpita;
    private String Ime;
    private  String Prezime;
    private float cijena;
    private String OpisPosla;
    private String NazivPosla;
    private Date PocetakPosla;
    private Date krajPosla;
    private int brojDjelatnosti;

    Button Izaberi;
    public ArrayList<JobAtributes> ListaTrenutnihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaProslihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaBuducihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaSvihPoslova = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list_izvodjac);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Popis poslova");
        }

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);

        Spinner spinPos = findViewById(R.id.spinPoslovi);
        ArrayAdapter<String> adapterPoslovi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, poslovi);
        spinPos.setAdapter(adapterPoslovi);

        Izaberi = findViewById(R.id.btbIzaberiPoslove);




        DohvatiTrenutnePoslove();
        DohvatiProslePoslove();
        DohvatiBuducePoslove();
        DohvatiSvePoslove();
        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(JobListActivityIzvodjac.this);
        mAdapter = new JobAdapterIzvodjac(ListaSvihPoslova);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        spinPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){

                    case 0:




                        poz = 1;

                        break;

                    case 1:



                        poz = 2;

                        break;
                    case 2:
                        poz = 3;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Izaberi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mRecyclerView.setVisibility(View.INVISIBLE);
                if(poz == 1)
                {
                    if(ListaTrenutnihPoslova.size()==0)  Toast.makeText(JobListActivityIzvodjac.this , "Trenutno nemate poslova koji se izvode!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapterIzvodjac(ListaTrenutnihPoslova);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                }
                if(poz == 2)
                {
                    if(ListaProslihPoslova.size()==0)  Toast.makeText(JobListActivityIzvodjac.this , "Nemate prošle poslove!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapterIzvodjac(ListaProslihPoslova);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                }
                if(poz == 3){
                    if(ListaBuducihPoslova.size()==0)  Toast.makeText(JobListActivityIzvodjac.this , "Nemate buduće poslove!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapterIzvodjac(ListaBuducihPoslova);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }

            }
        });



    }
    public void DohvatiTrenutnePoslove()
    {
        Connection con = connectionClass.CONN();

        String query = "select u.ID_djelatnosti, u.ID_upita, k.Ime, k.Prezime,  u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from  korisnik k inner join upit u  on k.ID_korisnika = u.ID_korisnika inner join ponuda p  on u.ID_upita = p.ID_upita  where p.Status = 1 and p.ID_izvodjaca = '" + ID + "'and p.Datum_pocetka <= convert(date,GETDATE()) and p.Datum_zavrsetka >= convert(date,GETDATE())";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){
                // Broj upit, naziv, opis
                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv_upita"));
                ListaCijena.add(rs.getFloat("Cijena"));
                ListaPocetka.add(rs.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs.getDate("Datum_zavrsetka"));
                ListaIme.add(rs.getString("Ime"));
                ListaPrezime.add(rs.getString("Prezime"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            Ime = ListaIme.get(i);
            Prezime = ListaPrezime.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov, Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol, Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.bravaa, Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar,Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika, Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.soboslikar,Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar, Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp,Ime, Prezime);
                    ListaTrenutnihPoslova.add(JA);

                    break;
            }



        }

        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIme.clear();
        ListaPrezime.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

    }
    public void DohvatiProslePoslove(){
        Connection con = connectionClass.CONN();
        int seL = 0;
        String query = "select u.ID_djelatnosti, u.ID_upita, k.Ime, k.Prezime,  u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from  korisnik k inner join upit u  on k.ID_korisnika = u.ID_korisnika inner join ponuda p  on u.ID_upita = p.ID_upita  where p.Status = 1 and p.ID_izvodjaca = '" + ID + "'and p.Datum_zavrsetka < convert(date,GETDATE())";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){
                // Broj upit, naziv, opis
                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv_upita"));
                ListaCijena.add(rs.getFloat("Cijena"));
                ListaPocetka.add(rs.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs.getDate("Datum_zavrsetka"));
                ListaIme.add(rs.getString("Ime"));
                ListaPrezime.add(rs.getString("Prezime"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            Ime = ListaIme.get(i);
            Prezime = ListaPrezime.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);


            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov, Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol, Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla," ", R.drawable.bravaa, Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar, Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika,Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla," ", R.drawable.soboslikar,Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar,Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp,Ime, Prezime);
                    ListaProslihPoslova.add(JA);

                    break;
            }





        }
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIme.clear();
        ListaPrezime.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

    }
    public void DohvatiSvePoslove(){
        Connection con = connectionClass.CONN();

        String query = "select u.ID_djelatnosti, u.ID_upita, k.Ime, k.Prezime,  u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from  korisnik k inner join upit u  on k.ID_korisnika = u.ID_korisnika inner join ponuda p  on u.ID_upita = p.ID_upita  where p.Status = 1 and p.ID_izvodjaca = '" + ID + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){
                // Broj upit, naziv, opis
                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv_upita"));
                ListaCijena.add(rs.getFloat("Cijena"));
                ListaPocetka.add(rs.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs.getDate("Datum_zavrsetka"));
                ListaIme.add(rs.getString("Ime"));
                ListaPrezime.add(rs.getString("Prezime"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            Ime = ListaIme.get(i);
            Prezime = ListaPrezime.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.bravaa, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.soboslikar, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar, Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp,Ime, Prezime);
                    ListaSvihPoslova.add(JA);

                    break;
            }




        }


        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIme.clear();
        ListaPrezime.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();


    }

    public void DohvatiBuducePoslove(){
        Connection con = connectionClass.CONN();

        String query = "select u.ID_djelatnosti, u.ID_upita, k.Ime, k.Prezime,  u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from  korisnik k inner join upit u  on k.ID_korisnika = u.ID_korisnika inner join ponuda p  on u.ID_upita = p.ID_upita  where p.Status = 1 and p.ID_izvodjaca = '" + ID + "'and p.Datum_pocetka > convert(date,GETDATE())";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){
                // Broj upit, naziv, opis
                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv_upita"));
                ListaCijena.add(rs.getFloat("Cijena"));
                ListaPocetka.add(rs.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs.getDate("Datum_zavrsetka"));
                ListaIme.add(rs.getString("Ime"));
                ListaPrezime.add(rs.getString("Prezime"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            Ime = ListaIme.get(i);
            Prezime = ListaPrezime.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov, Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol, Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.bravaa, Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar,Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika, Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.soboslikar,Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar, Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(1,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp,Ime, Prezime);
                    ListaBuducihPoslova.add(JA);

                    break;
            }



        }

        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIme.clear();
        ListaPrezime.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

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
                intent.putExtra("ID_izvodjaca", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
