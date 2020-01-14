package com.mgradnja.Izvodjac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.Adapters.JobAdapterIzvodjac;
import com.mgradnja.Adapters.OfferAdapter;
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


    Button Izaberi;
    public ArrayList<JobAtributes> ListaSvihPonuda = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;
    private JobAdapterIzvodjac mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list_acitivity_izvodjac);

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);



        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(OfferListAcitivityIzvodjac.this);
        DohvatiPonude();
        if(ListaSvihPonuda.size()==0)  Toast.makeText(OfferListAcitivityIzvodjac.this , "Trenutno nemate ponuda ni jednu ponudu!" , Toast.LENGTH_LONG).show();
        mAdapter = new JobAdapterIzvodjac(ListaSvihPonuda);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.krov,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.stol,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.bravaa,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.staklar,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.keramika,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.soboslikar,Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.zidar, Ime, Prezime);
                    ListaSvihPonuda.add(JA);

                    break;
                default:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, " ", R.drawable.ic_wrench_24dp, Ime, Prezime);
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
}
