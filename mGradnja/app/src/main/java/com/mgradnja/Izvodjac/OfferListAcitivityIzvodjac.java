package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.Adapters.JobAdapterIzvodjac;
import com.mgradnja.Adapters.OfferAdapter;
import com.mgradnja.Adapters.OfferAdapterIzvodjac;
import com.mgradnja.ConnectionClass;
import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.OfferListActivity;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class OfferListAcitivityIzvodjac extends AppCompatActivity {

    ConnectionClass connectionClass;

    private Integer ID;
    private TextView tv;
    private  Integer ID_upita;
    private String NazivUpita = "";

    private Context context = OfferListAcitivityIzvodjac.this;

    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<String> ListaIme = new ArrayList<String>();
    public ArrayList<String> ListaPrezime = new ArrayList<String>();
    public ArrayList<Integer> ListaDjelatnostID = new ArrayList<Integer>();
    public ArrayList<String> ListaIzvodjacID = new ArrayList<String>();
    public ArrayList<Float> ListaCijena = new ArrayList<Float>();
    public ArrayList<Date> ListaPocetka = new ArrayList<Date>();
    public ArrayList<Date> ListaZavrsetks = new ArrayList<Date>();
    public ArrayList<String> ListaOpisa = new ArrayList<String>();
    public ArrayList<String> ListaNaziva = new ArrayList<String>();



    private int brojUpita;
    private String Prezime = "";
    private float cijena;
    private String OpisPosla;
    private String NazivPosla;
    private Date PocetakPosla;
    private Date krajPosla;
    private int brojDjelatnosti;
    private int poz = 0;
    private String Ime = "";

    private int brojac = 0;

    Button Izaberi;
    public ArrayList<JobAtributes> ListaSvihPonuda = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;


    private OfferAdapterIzvodjac mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list_acitivity_izvodjac);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);


        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(OfferListAcitivityIzvodjac.this);
        DohvatiPonude();
        if(ListaSvihPonuda.size()==0)  Toast.makeText(OfferListAcitivityIzvodjac.this , "Trenutno nemate ni jednu ponudu!" , Toast.LENGTH_LONG).show();
        mAdapter = new OfferAdapterIzvodjac(ListaSvihPonuda);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        OnClickDelete();

    }

    public void OnClickDelete(){
        mAdapter.setOnClickListener(new OfferAdapterIzvodjac.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void OnDeleteClick(int position) {
                brojac = ListaSvihPonuda.size();
                if(brojac >1)
                {
                    if(position == 0) {

                        JobAtributes JA;
                        JA = ListaSvihPonuda.get(position);
                        ListaSvihPonuda.remove(position);
                        NazivUpita = JA.getmNazivPosla();
                        mAdapter.notifyItemRemoved(position);
                        IzbrisiPonudu(NazivUpita);
                        //tv = findViewById(R.id.txtPonudeIZV);
                        //tv.setText(NazivUpita);
                    }
                    else{
                        JobAtributes JA;
                        JA = ListaSvihPonuda.get(position);
                        ListaSvihPonuda.remove(position);
                        NazivUpita = JA.getmNazivPosla();
                        mAdapter.notifyItemRemoved(position);
                        IzbrisiPonudu(NazivUpita);
                        //tv = findViewById(R.id.txtPonudeIZV);
                        //tv.setText(NazivUpita);
                    }

                }
                else {
                    JobAtributes JA;
                    JA = ListaSvihPonuda.get(0);
                    ListaSvihPonuda.clear();
                    NazivUpita = JA.getmNazivPosla();
                    ID_upita = JA.getMbrojUpita();
                    mAdapter.notifyItemRemoved(position);
                    IzbrisiPonudu(NazivUpita);

                }

            }
            public void OnUpdateClick(int position)  {
                JobAtributes JA;
                JA = ListaSvihPonuda.get(position);
                String NazivUpit = JA.getmNazivPosla();
                Date Pocetak = JA.getmPočetakPosla();
                Date Kraj = JA.getMkrajPosla();
                Float Cena = JA.getmCijena();
                String Opis = JA.getmOpisPosla();
                Integer IDUpita = JA.getMbrojUpita();
                String ImeIZV = JA.getmNazivIzvodjaca();

                Intent intent9 = new Intent(OfferListAcitivityIzvodjac.this, UpdateOfferActivity.class);
                intent9.putExtra("Cijena", Cena);
                intent9.putExtra("Opis", Opis);
                intent9.putExtra("Naziv", NazivUpit);
                intent9.putExtra("Početak", Pocetak);
                intent9.putExtra("Kraj", Kraj);
                intent9.putExtra("ID_izvodjaca", ID);
                intent9.putExtra("ID_upita", IDUpita);

                startActivity(intent9);
                finish();
            }
        });
    }
    public void DohvatiPonude(){
        Connection con = connectionClass.CONN();
        ListaSvihPonuda.clear();
        String query = "select u.ID_djelatnosti, u.ID_upita, k.Ime, k.Prezime, u.Naziv as 'Naziv_upita', p.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from korisnik k inner join upit u on k.ID_korisnika = u.ID_korisnika inner join ponuda p  on u.ID_upita = p.ID_upita  where p.Status = 0 and p.ID_izvodjaca = '" + ID + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while(rs.next()){

                ListaUpitID.add(rs.getInt("ID_upita"));
                ListaOpisa.add(rs.getString("Opis"));
                ListaNaziva.add(rs.getString("Naziv_upita"));
                ListaCijena.add(rs.getFloat("Cijena"));
                ListaPocetka.add(rs.getDate("Datum_pocetka"));
                ListaZavrsetks.add(rs.getDate("Datum_zavrsetka"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));
                ListaIme.add(rs.getString("Ime"));
                ListaPrezime.add(rs.getString("Prezime"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++) {

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            Ime = ListaIme.get(i);
            Prezime = ListaPrezime.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti) {
                case 1:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.bravaa,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.soboslikar,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar, Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                default:
                    JA = new JobAtributes(1, brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp, Ime, Prezime);
                    ListaSvihPonuda.add(JA);

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

    public void IzbrisiPonudu(String Naziv){
        Connection con = connectionClass.CONN();
        String query1 = "Select ID_upita from Upit where Naziv = '" + Naziv +"'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query1);

            while (rs.next()) {
                ID_upita = rs.getInt("ID_upita");
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "Delete from Ponuda where ID_upita = '" + ID_upita +"'and ID_izvodjaca = '" + ID + "'";
        try {
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(query);



        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(OfferListAcitivityIzvodjac.this , "Ponuda Izbrisana!" , Toast.LENGTH_LONG).show();

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
