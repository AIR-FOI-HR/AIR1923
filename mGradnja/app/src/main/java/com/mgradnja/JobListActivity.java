package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.Adapters.JobAdapter;
import com.mgradnja.HelpEntities.JobAtributes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class JobListActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    String hh = "Poslovi";
    private Integer ID;
    private TextView tv;
    private int poz = 0;

    public String[] poslovi = new String[]{"Trenutni poslovi", "Prošli poslovi"};

    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<String> ListaIzvodjacID = new ArrayList<String>();
    public ArrayList<Float> ListaCijena = new ArrayList<Float>();
    public ArrayList<Date> ListaPocetka = new ArrayList<Date>();
    public ArrayList<Date> ListaZavrsetks = new ArrayList<Date>();
    public ArrayList<String> ListaOpisa = new ArrayList<String>();
    public ArrayList<String> ListaNaziva = new ArrayList<String>();


    private int brojUpita;
    private String NazivIzvodjaca;
    private float cijena;
    private String OpisPosla;
    private String NazivPosla;
    private Date PocetakPosla;
    private Date krajPosla;

    Button Izaberi;
    public ArrayList<JobAtributes> ListaTrenutnihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaProslihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaSvihPoslova = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

        Spinner spinPos = findViewById(R.id.spinPoslovi);
        ArrayAdapter<String> adapterPoslovi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, poslovi);
        spinPos.setAdapter(adapterPoslovi);

        Izaberi = findViewById(R.id.btbIzaberiPoslove);




        DohvatiTrenutnePoslove();
        DohvatiProslePoslove();
        DohvatiSvePoslove();
        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(JobListActivity.this);
        mAdapter = new JobAdapter(ListaSvihPoslova);
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
                    if(ListaTrenutnihPoslova.size()==0)  Toast.makeText(JobListActivity.this , "Trenutno nemate poslova koji se izvode!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapter(ListaTrenutnihPoslova);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    /*
                    mRecyclerView2 = findViewById(R.id.main_recycler2);
                    mRecyclerView2.setHasFixedSize(true);
                    mLayoutManager2 = new LinearLayoutManager(JobListActivity.this);
                    mAdapter2 = new JobAdapter(ListaTrenutnihPoslova);
                    mRecyclerView2.setLayoutManager(mLayoutManager2);
                    mRecyclerView2.setAdapter(mAdapter2);
                    */
                }
                if(poz == 2)
                {
                    if(ListaProslihPoslova.size()==0)  Toast.makeText(JobListActivity.this , "Nemate prošle poslove!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapter(ListaProslihPoslova);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                   /*
                    mRecyclerView3 = findViewById(R.id.main_recycler3);
                    mRecyclerView3.setHasFixedSize(true);
                    mLayoutManager3 = new LinearLayoutManager(JobListActivity.this);
                    mAdapter3 = new JobAdapter(ListaProslihPoslova);

                    mRecyclerView3.setLayoutManager(mLayoutManager3);
                    mRecyclerView3.setAdapter(mAdapter3);
                    */
                }

            }
        });



    }


    public void DohvatiSvePoslove(){

        Connection con = connectionClass.CONN();

        String query = "select u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where u.ID_korisnika = '" + ID + "'";
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
                ListaIzvodjacID.add(rs.getString("Naziv_izvodjaca"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);


            JA = new JobAtributes(brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca);
            ListaSvihPoslova.add(JA);



        }


        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();



    }
    public void DohvatiTrenutnePoslove(){
        Connection con = connectionClass.CONN();

        String query = "select u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where u.ID_korisnika = '" + ID + "'and p.Datum_zavrsetka > convert(date,GETDATE())";
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
                ListaIzvodjacID.add(rs.getString("Naziv_izvodjaca"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);


            JA = new JobAtributes(brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca);
            ListaTrenutnihPoslova.add(JA);



        }

        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();

    }
    public void DohvatiProslePoslove(){
        Connection con = connectionClass.CONN();
        tv = findViewById(R.id.txtRadovi);
        int seL = 0;
        String query = "select u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where u.ID_korisnika = '" + ID + "'and p.Datum_zavrsetka < convert(date,GETDATE())";
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
                ListaIzvodjacID.add(rs.getString("Naziv_izvodjaca"));
               // seL++;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);


            JA = new JobAtributes(brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca);
            ListaProslihPoslova.add(JA);




        }
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();

    }


}