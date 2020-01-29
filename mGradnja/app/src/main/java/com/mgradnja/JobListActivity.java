package com.mgradnja;

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

    public String[] poslovi = new String[]{"Trenutni poslovi", "Prošli poslovi", "Budući poslovi"};

    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaDjelatnostID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaPlaceno = new ArrayList<Integer>();
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
    private int brojDjelatnosti;
    private int placeno;
    private int brojac;
    private int brojPlacenogUpita;
 //   private int kontrola;

    Button Izaberi;
    public ArrayList<JobAtributes> ListaTrenutnihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaProslihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaBuducihPoslova = new ArrayList<>();
    public ArrayList<JobAtributes> ListaSvihPoslova = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;
    private JobAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Popis poslova");
        }

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

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
        mLayoutManager = new LinearLayoutManager(JobListActivity.this);
        mAdapter = new JobAdapter(ListaSvihPoslova);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        OnClickPay(poz);


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
                    if(ListaTrenutnihPoslova.size()==0)  Toast.makeText(JobListActivity.this , "Trenutno nemate poslova koji se izvode!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapter(ListaTrenutnihPoslova);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    OnClickPay(poz);

                }
                if(poz == 2)
                {
                    if(ListaProslihPoslova.size()==0)  Toast.makeText(JobListActivity.this , "Nemate prošle poslove!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapter(ListaProslihPoslova);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    OnClickPay(poz);

                }
                if(poz == 3){
                    if(ListaBuducihPoslova.size()==0)  Toast.makeText(JobListActivity.this , "Nemate buduće poslove!" , Toast.LENGTH_LONG).show();
                    mAdapter = new JobAdapter(ListaBuducihPoslova);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    OnClickPay(poz);
                }

            }
        });

    }

    public void OnClickPay(int poz){
       switch (poz) {
           case 0:

               mAdapter.setOnClickListener(new JobAdapter.OnItemClickListener() {
                   @Override
                   public void OnItemClick(int position) {

                   }

                   @Override
                   public void OnlinePayClick(int position) {
                       brojac = ListaSvihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaSvihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaSvihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaSvihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniOnlinePlacanje(brojPlacenogUpita);

                       }

                   }

                   @Override
                   public void OnCashClick(int position) {
                       brojac = ListaSvihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaSvihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaSvihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaSvihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniGotovinskoPlacanje(brojPlacenogUpita);

                       }

                   }



               });
               break;
           case 1:
               mAdapter.setOnClickListener(new JobAdapter.OnItemClickListener() {
                   @Override
                   public void OnItemClick(int position) {

                   }

                   @Override
                   public void OnlinePayClick(int position) {
                       brojac = ListaTrenutnihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaTrenutnihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaTrenutnihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaTrenutnihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniOnlinePlacanje(brojPlacenogUpita);

                       }

                   }

                   @Override
                   public void OnCashClick(int position) {
                       brojac = ListaTrenutnihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaTrenutnihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaTrenutnihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaTrenutnihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniGotovinskoPlacanje(brojPlacenogUpita);

                       }

                   }



               });
               break;

           case 2:
               mAdapter.setOnClickListener(new JobAdapter.OnItemClickListener() {
                   @Override
                   public void OnItemClick(int position) {

                   }

                   @Override
                   public void OnlinePayClick(int position) {
                       brojac = ListaProslihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaProslihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaProslihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaProslihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniOnlinePlacanje(brojPlacenogUpita);

                       }

                   }

                   @Override
                   public void OnCashClick(int position) {
                       brojac = ListaProslihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaProslihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaProslihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaProslihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniGotovinskoPlacanje(brojPlacenogUpita);

                       }

                   }



               });
               break;

           case 3:
               mAdapter.setOnClickListener(new JobAdapter.OnItemClickListener() {
                   @Override
                   public void OnItemClick(int position) {

                   }

                   @Override
                   public void OnlinePayClick(int position) {
                       brojac = ListaBuducihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaBuducihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaBuducihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniOnlinePlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaBuducihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniOnlinePlacanje(brojPlacenogUpita);

                       }

                   }

                   @Override
                   public void OnCashClick(int position) {
                       brojac = ListaBuducihPoslova.size();
                       if(brojac >1)
                       {
                           if(position == 0) {

                               JobAtributes JA;
                               JA = ListaBuducihPoslova.get(position);
                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }
                           else{
                               JobAtributes JA;
                               JA = ListaBuducihPoslova.get(position);

                               brojPlacenogUpita = JA.getMbrojUpita();
                               PokreniGotovinskoPlacanje(brojPlacenogUpita);

                           }

                       }
                       else {
                           JobAtributes JA;
                           JA = ListaBuducihPoslova.get(0);

                           brojPlacenogUpita = JA.getMbrojUpita();
                           PokreniGotovinskoPlacanje(brojPlacenogUpita);

                       }

                   }



               });
               break;
       }
    }

    public void PokreniGotovinskoPlacanje(Integer BrojUpita){

        Toast.makeText(JobListActivity.this , BrojUpita.toString(), Toast.LENGTH_LONG).show();

        //diajalog boks TODO
    }
    public void PokreniOnlinePlacanje(Integer BrojUpita){
        //Toast.makeText(JobListActivity.this , BrojUpita.toString(), Toast.LENGTH_LONG).show();
        try {
            Intent intent = new Intent(JobListActivity.this, Class.forName("com.example.braintree.MainActivityBrainTree"));
            intent.putExtra("BrojUpita", BrojUpita);
            intent.putExtra("ID_korisnika", ID);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }


    }
    public void DohvatiSvePoslove(){

        Connection con = connectionClass.CONN();

        String query = "select u.ID_djelatnosti, u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'";
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
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "select po.placeno from  posao po inner join ponuda p on po.ID_upita = p.ID_upita inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'";

        try {
            Statement statement = con.createStatement();
            ResultSet rs2 = statement.executeQuery(query2);


            while(rs2.next()){
                // Broj upit, naziv, opis
                ListaPlaceno.add(rs2.getInt("placeno"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            placeno = ListaPlaceno.get(i);
            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.krov,  " ", "");
                    ListaSvihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.stol, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.bravaa, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.staklar, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.keramika, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.soboslikar, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.zidar, " ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.ic_wrench_24dp," ", " ");
                    ListaSvihPoslova.add(JA);

                    break;
            }




        }

        ListaPlaceno.clear();
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();



    }
    public void DohvatiTrenutnePoslove(){
        Connection con = connectionClass.CONN();

        String query = "select  u.ID_djelatnosti, u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from  izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and p.Datum_pocetka <= convert(date,GETDATE()) and p.Datum_zavrsetka >= convert(date,GETDATE())";

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
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "select po.placeno from  posao po inner join ponuda p on po.ID_upita = p.ID_upita inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and p.Datum_pocetka <= convert(date,GETDATE()) and p.Datum_zavrsetka >= convert(date,GETDATE())";

        try {
            Statement statement = con.createStatement();
            ResultSet rs2 = statement.executeQuery(query2);


            while(rs2.next()){
                // Broj upit, naziv, opis
                ListaPlaceno.add(rs2.getInt("placeno"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){

            placeno = ListaPlaceno.get(i);
            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(placeno, brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.krov, " ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.stol, " ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.bravaa, " ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.staklar," ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.keramika, " ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.soboslikar," ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.zidar, " ", " ");
                    ListaTrenutnihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.ic_wrench_24dp," ", "");
                    ListaTrenutnihPoslova.add(JA);

                    break;
            }



        }
        ListaPlaceno.clear();
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

    }
    public void DohvatiProslePoslove(){
        Connection con = connectionClass.CONN();
        int seL = 0;
        String query = "select u.ID_djelatnosti, u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and p.Datum_zavrsetka < convert(date,GETDATE())";
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
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "select po.placeno from  posao po inner join ponuda p on po.ID_upita = p.ID_upita inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and p.Datum_zavrsetka < convert(date,GETDATE())";

        try {
            Statement statement = con.createStatement();
            ResultSet rs2 = statement.executeQuery(query2);


            while(rs2.next()){
                // Broj upit, naziv, opis
                ListaPlaceno.add(rs2.getInt("placeno"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++){
            placeno = ListaPlaceno.get(i);
            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);


            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.krov, " ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.stol, " ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.bravaa, " ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.staklar, " ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.keramika," ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.soboslikar," ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.zidar," ", " ");
                    ListaProslihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.ic_wrench_24dp," ", "");
                    ListaProslihPoslova.add(JA);

                    break;
            }





        }
        ListaPlaceno.clear();
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

    }
    public void DohvatiBuducePoslove(){
        Connection con = connectionClass.CONN();

        String query = "select u.ID_djelatnosti, u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', u.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and  p.Datum_pocetka > convert(date,GETDATE())";
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
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "select po.placeno from  posao po inner join ponuda p on po.ID_upita = p.ID_upita inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 1 and u.ID_korisnika = '" + ID + "'and  p.Datum_pocetka > convert(date,GETDATE())";

        try {
            Statement statement = con.createStatement();
            ResultSet rs2 = statement.executeQuery(query2);


            while(rs2.next()){
                // Broj upit, naziv, opis
                ListaPlaceno.add(rs2.getInt("placeno"));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<ListaUpitID.size(); i++){
            placeno = ListaPlaceno.get(i);
            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti){
                case 1:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.krov, " ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.stol, " ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.bravaa, " ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.staklar," ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.keramika, " ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.soboslikar," ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.zidar, " ", " ");
                    ListaBuducihPoslova.add(JA);

                    break;
                default:
                    JA = new JobAtributes(placeno,brojUpita, cijena,  OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.ic_wrench_24dp," ", "");
                    ListaBuducihPoslova.add(JA);

                    break;
            }



        }
        ListaPlaceno.clear();
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
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
                Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
                intent.putExtra("ID_izvodjaca", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}