package com.mgradnja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.Adapters.OfferAdapter;
import com.mgradnja.HelpEntities.JobAtributes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class OfferListActivity extends AppCompatActivity {

    ConnectionClass connectionClass;

    private Integer ID;
    private TextView tv;
    private int ID_upit = 0;

    private Context context = OfferListActivity.this;



    public String[] ponude;

    public ArrayList<Integer> ListaUpitIDSvi = new ArrayList<Integer>();
    public ArrayList<Integer> ListaUpitID = new ArrayList<Integer>();
    public ArrayList<Integer> ListaUpitIDProsli = new ArrayList<Integer>();
    public ArrayList<Integer> ListaDjelatnostID = new ArrayList<Integer>();
    public ArrayList<String> ListaIzvodjacID = new ArrayList<String>();
    public ArrayList<Float> ListaCijena = new ArrayList<Float>();
    public ArrayList<Date> ListaPocetka = new ArrayList<Date>();
    public ArrayList<Date> ListaZavrsetks = new ArrayList<Date>();
    public ArrayList<String> ListaOpisa = new ArrayList<String>();
    public ArrayList<String> ListaNaziva = new ArrayList<String>();
    public ArrayList<String> ListaSvihUpita = new ArrayList<String>();
    public ArrayList<String> ListaProslihUpita = new ArrayList<String>();


    private int brojUpita;
    private String NazivIzvodjaca;
    private float cijena;
    private String OpisPosla;
    private String NazivPosla;
    private Date PocetakPosla;
    private Date krajPosla;
    private int brojDjelatnosti;
    private int poz = 0;
    private String Ime = "";
    private int DohvaceniIDupita = 0;
    private String DohvaceniIDIzvodjaca = " ";

    Button Izaberi;
    public ArrayList<JobAtributes> ListaSvihPonuda = new ArrayList<>();
    public JobAtributes JA;
    private RecyclerView mRecyclerView;
    private OfferAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        connectionClass = new ConnectionClass();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);
        DohvatiTrenutneUpite();
        /*
        Spinner spinPon = findViewById(R.id.spinPonude);
        ArrayAdapter<String> adapterPonude = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ponude);
        spinPon.setAdapter(adapterPonude);
        */

        Izaberi = findViewById(R.id.btbIzaberiUpite);

        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(OfferListActivity.this);

        PozoviSpinner();

        Izaberi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DohvatiPonude(ID_upit);
                if(ListaSvihPonuda.size()==0)  Toast.makeText(OfferListActivity.this , "Trenutno nemate ponuda za izabrani upit!" , Toast.LENGTH_LONG).show();
                mAdapter = new OfferAdapter(ListaSvihPonuda);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


                mAdapter.setOnClickListener(new OfferAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        DohvaceniIDupita = ListaSvihPonuda.get(position).getMbrojUpita();
                        DohvaceniIDIzvodjaca = ListaSvihPonuda.get(position).getmNazivIzvodjaca();

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        PrihvatiPonudu(DohvaceniIDIzvodjaca, DohvaceniIDupita);

                                        PozoviSpinner();


                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Jeste li sigurni da želite prihvatiti izabranu ponudu?").setPositiveButton("DA", dialogClickListener).setNegativeButton("NE", dialogClickListener).show();



                    }

                });
            }



        });


    }


    public void PronadiNazivUpita(String Ime, int ID){
        Connection con = connectionClass.CONN();

        String query = "select ID_upita from Upit where Naziv = '"+ Ime + "' and ID_korisnika = '"+ID+"'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            ListaUpitID.clear();
            while(rs.next()){

                ID_upit=rs.getInt("ID_upita");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void DohvatiTrenutneUpite(){
        Connection con = connectionClass.CONN();

        String query = "select Naziv, ID_upita from Upit where ID_korisnika = '" + ID + "'";
        String query2 = "select u.Naziv, u.ID_upita from Upit u inner join Ponuda p on p.ID_upita = u.ID_upita where p.Status = 1 and u.ID_korisnika = '" + ID + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                ListaSvihUpita.add(rs.getString("Naziv"));
                ListaUpitIDSvi.add(rs.getInt("ID_upita"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(query2);
            while(rs2.next()){
                ListaProslihUpita.add(rs2.getString("Naziv"));
                ListaUpitIDProsli.add(rs2.getInt("ID_upita"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        for (int i = 0;i<ListaSvihUpita.size();i++){
            for(int j = 0;j<ListaProslihUpita.size();j++){
                if(ListaSvihUpita.get(i).equals(ListaProslihUpita.get(j)) )
                {
                    ListaSvihUpita.remove(i);
                    ListaUpitIDSvi.remove(i);
                }
            }
        }


        ponude = ListaSvihUpita.toArray(new String[0]);
    }

    public void DohvatiPonude(int ID){
        Connection con = connectionClass.CONN();
        ListaSvihPonuda.clear();
        String query = "select u.ID_djelatnosti, u.ID_upita, i.Naziv as 'Naziv_izvodjaca', u.Naziv as 'Naziv_upita', p.Opis, p.Cijena, p.Datum_pocetka, p.Datum_zavrsetka from izvodjac i inner join ponuda p on i.ID_izvodjaca = p.ID_izvodjaca inner join upit u  on u.ID_upita = p.ID_upita  where p.Status = 0 and u.ID_upita = '" + ID + "'";
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
                ListaIzvodjacID.add(rs.getString("Naziv_izvodjaca"));
                ListaDjelatnostID.add(rs.getInt("ID_djelatnosti"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<ListaUpitID.size(); i++) {

            brojUpita = ListaUpitID.get(i);
            OpisPosla = ListaOpisa.get(i);
            NazivIzvodjaca = ListaIzvodjacID.get(i);
            NazivPosla = ListaNaziva.get(i);
            PocetakPosla = ListaPocetka.get(i);
            krajPosla = ListaZavrsetks.get(i);
            cijena = ListaCijena.get(i);
            brojDjelatnosti = ListaDjelatnostID.get(i);

            switch (brojDjelatnosti) {
                case 1:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.krov, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 2:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.stol, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 3:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.bravaa, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 4:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.staklar, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 5:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.keramika, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 6:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.soboslikar, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                case 7:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.zidar, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
                default:
                    JA = new JobAtributes(brojUpita, cijena, OpisPosla, NazivPosla, PocetakPosla, krajPosla, NazivIzvodjaca, R.drawable.ic_wrench_24dp, " ", " ");
                    ListaSvihPonuda.add(JA);

                    break;
            }
        }
        ListaOpisa.clear();
        ListaUpitID.clear();
        ListaNaziva.clear();
        ListaIzvodjacID.clear();
        ListaPocetka.clear();
        ListaZavrsetks.clear();
        ListaCijena.clear();
        ListaDjelatnostID.clear();

    }

    public void PrihvatiPonudu(String NazivIzvodjaca, int ID_Upita) {

        Connection con = connectionClass.CONN();

        int IzvodjacID = 0;
        int BrojUpita = 0;
        String query = "Select ID_izvodjaca from Izvodjac where Naziv = '" + NazivIzvodjaca + "'";


        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);


            ListaUpitID.clear();
            while (rs.next()) {

                IzvodjacID = rs.getInt("ID_izvodjaca");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query3 = "Select ID_upita from Ponuda where Status = 1 and ID_upita = '" + ID_Upita + "'";
        try {
            Statement statement3 = con.createStatement();
            ResultSet rs3 = statement3.executeQuery(query3);


            ListaUpitID.clear();
            while (rs3.next()) {

                BrojUpita++;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (BrojUpita == 0) {

            String query2 = "Update Ponuda set Status = 1 where ID_upita = '" + ID_Upita + "' and ID_izvodjaca = '" + IzvodjacID + "'";

            try {
                Statement statement2 = con.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);


            } catch (SQLException e) {
                e.printStackTrace();
            }
            String query4 = "Insert into Posao VALUES(('"+ ID_Upita +"'), ('"+ IzvodjacID +"'))";

            try {
                Statement statement4 = con.createStatement();
             //   ResultSet rs4 = statement4.executeQuery(query4);


            } catch (SQLException e) {
                e.printStackTrace();
            }
            Toast.makeText(OfferListActivity.this , "Ponuda uspjesno prihvacena!" , Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(OfferListActivity.this , "Vec ste prihvatili ponudu za ovaj upit!" , Toast.LENGTH_LONG).show();
        }



    }
    public void PozoviSpinner(){
        Spinner spinPon = findViewById(R.id.spinPonude);
        ArrayAdapter<String> adapterPonude = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ponude);
        spinPon.setAdapter(adapterPonude);
        spinPon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < ListaSvihUpita.size();i++){
                    if(position == i) {
                        Ime = ListaSvihUpita.get(i);

                    }

                }

                PronadiNazivUpita(Ime, ID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
