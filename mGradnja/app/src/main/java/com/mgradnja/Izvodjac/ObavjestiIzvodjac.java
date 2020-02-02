package com.mgradnja.Izvodjac;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mgradnja.Adapters.ObavijestAdapter;
import com.mgradnja.ConnectionClass;
import com.mgradnja.HelpEntities.ObavijestAtributes;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.ListenerClass;

public class ObavjestiIzvodjac extends AppCompatActivity {

    ConnectionClass connectionClass;

    Integer ID,idPosla;
    String sadrzaj,vrijeme,imeIzvodjaca;

    public ArrayList<String> poslovi = new ArrayList<String>();
    public ArrayList<Integer> idPoslova = new ArrayList<Integer>();
    public ArrayList<ObavijestAtributes> obavijestLista = new ArrayList<>();

    Button izaberiPosao,novaPoruka;
    public Spinner spinnerPoslovi;

    private RecyclerView obavijestRecycleView;
    private RecyclerView.Adapter obavijestAdapter;
    private RecyclerView.LayoutManager obavijestLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obavjesti_izvodjac);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Obavijesti");
        }

        connectionClass=new ConnectionClass();
        Intent intent=getIntent();
        ID=intent.getIntExtra("ID_izvodjaca", 0);

        spinnerPoslovi=findViewById(R.id.spinerPoslova);
        dohvatiPoslove();
        ArrayAdapter<String> adapterPoslovi = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,poslovi);
        spinnerPoslovi.setAdapter(adapterPoslovi);
        izaberiPosao=findViewById(R.id.btnIzaberiPosao);

        izaberiPosao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziObavijesti();
            }
        });

        novaPoruka=findViewById(R.id.btnNovaPoruka);

        novaPoruka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otvoriAktivnostNovaPoruka();

            }
        });

    }
    public void otvoriAktivnostNovaPoruka(){
        idPosla = idPoslova.get(spinnerPoslovi.getSelectedItemPosition());
        Intent otvoriNovuPoruku = new Intent(ObavjestiIzvodjac.this, NovaPorukaIzvodjac.class);
        otvoriNovuPoruku.putExtra("ID_posla", idPoslova.get(spinnerPoslovi.getSelectedItemPosition()));
        otvoriNovuPoruku.putExtra("ID_izvodjaca", ID);
        startActivity(otvoriNovuPoruku);
    }

    public void dohvatiPoslove(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();


        String query = "select Naziv,ID_posla from Upit join Posao on(Upit.ID_upita=Posao.ID_upita) where Posao.ID_izvodjaca=" + ID;

        try {
            Statement statement = con.createStatement();
            ResultSet as = statement.executeQuery(query);

            while (as.next()) {
                poslovi.add(as.getString("Naziv"));
                idPoslova.add(as.getInt("ID_posla"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void prikaziObavijesti(){
        popuniRecyclerViewObavijestima();
        obavijestRecycleView=findViewById(R.id.poruka);
        obavijestRecycleView.setHasFixedSize(true);
        obavijestLayoutManager=new LinearLayoutManager(this);
        obavijestAdapter=new ObavijestAdapter(obavijestLista);

        obavijestRecycleView.setLayoutManager(obavijestLayoutManager);
        obavijestRecycleView.setAdapter(obavijestAdapter);
    }

    public void popuniRecyclerViewObavijestima(){
        obavijestLista.clear();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        idPosla=idPoslova.get(spinnerPoslovi.getSelectedItemPosition());
        String query2 = "select Sadrzaj,Vrijeme,ID_izvodjaca from Komentar join Posao on(Komentar.ID_posla=Posao.ID_posla) where Posao.ID_posla=" + idPosla;

        try {
            Statement statement = con.createStatement();
            ResultSet as = statement.executeQuery(query2);

            while (as.next()) {
                sadrzaj=as.getString("Sadrzaj");
                vrijeme=as.getString("Vrijeme");
                imeIzvodjaca=as.getString("ID_izvodjaca");
                obavijestLista.add(new ObavijestAtributes(imeIzvodjaca, sadrzaj, vrijeme));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {return true;}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
                //intent.putExtra("ID_izvodjaca", ID);
                //this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
