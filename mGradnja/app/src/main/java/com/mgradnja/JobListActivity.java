package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    public String[] poslovi = new String[]{"Trenutni poslovi", "Prošli poslovi"};

    public ArrayList<Integer> ListaPosaoID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaIzvodjacID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaPonudaID = new ArrayList<Integer>();
    public ArrayList<Float> ListaCijena = new ArrayList<Float>();
    public ArrayList<Date> ListaPocetka = new ArrayList<Date>();
    public ArrayList<Date> ListaZavrsetks = new ArrayList<Date>();
    public ArrayList<String> ListaOpisa = new ArrayList<String>();
    public ArrayList<String> ListaNaziva = new ArrayList<String>();

    private int brojPosla;
    private int brojUpita;
    private int brojPonude;
    private int brojIzvodjaca;
    private float cijena;
    private String OpisPosla;
    private String NazivPosla;
    private Date PocetakPosla;
    private Date krajPosla;

    Button Izaberi;
    public ArrayList<JobAtributes> ListaPoslova = new ArrayList<>();
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



        DohvatiPoslove();

        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(JobListActivity.this);
        mAdapter = new JobAdapter(ListaPoslova);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        Izaberi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });



    }

    public void DohvatiPoslove(){

        int seL = 0;
    //    ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        tv = findViewById(R.id.txtRadovi);
        String query = "select u.ID_upita, u.Naziv, u.Opis from posao p inner join upit u on p.ID_upita = u.ID_upita where u.ID_korisnika = '" +  ID +"' ";
        String query2 = "select  p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from ponuda p inner join upit u on p.ID_upita = u.ID_upita where u.ID_korisnika = '" +  ID +"' ";
        String query3 = "select * from upit u inner join posao p on u.ID_upita = p.ID_upita where u.ID_korisnika = +"+ID +"' and p.ID_posla is not null'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){
                // Broj upit, naziv, opis
                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv"));
                seL++;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {



            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(query2);


            while(rs2.next()) {

                //Cijena, pocetak, kraj
                ListaCijena.add(rs2.getFloat("Cijena"));
                ListaPocetka.add(rs2.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs2.getDate("Datum_zavrsetka"));
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaNaziva.size(); i++){
            brojUpita = ListaUpitID.get(i);
           // brojIzvodjaca = ListaIzvodjacID.get(i);
            brojIzvodjaca = 0;
            OpisPosla = ListaOpisa.get(i);

            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);


            JA = new JobAtributes(brojUpita, cijena, brojIzvodjaca, OpisPosla, NazivPosla, PocetakPosla, krajPosla);
            ListaPoslova.add(JA);


            //Toast.makeText(JobListActivity.this, String.valueOf(ListaPoslova.size()), Toast.LENGTH_LONG);
        }


        int vel = ListaOpisa.size();


        hh =  String.valueOf(seL);
        tv.setText(hh);


    }


}