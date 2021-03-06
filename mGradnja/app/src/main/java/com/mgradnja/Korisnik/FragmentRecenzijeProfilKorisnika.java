package com.mgradnja.Korisnik;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;
import com.mgradnja.Adapters.RecenzijeAdapterProfilKorisnika;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


public class FragmentRecenzijeProfilKorisnika extends Fragment {

    ConnectionClass connectionClass;
    Connection connection;

    public ArrayList<String> listaNazivaIzvodjaca = new ArrayList<>();
    public ArrayList<Date> listaDatumaRecenzija = new ArrayList<>();
    public ArrayList<String> listaKomentaraRecenzija = new ArrayList<>();
    public ArrayList<Integer> listaOcjenaRecenzija = new ArrayList<>();

    private String nazivIzvodjaca, komentar;
    private Date datumRecenzije;
    private Integer ocjenaRecenzije;

    public ArrayList<ItemRecenzijaProfilKorisnika> listaRecenzija = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemRecenzijaProfilKorisnika RA;

    TextView txtPoruka;


    public FragmentRecenzijeProfilKorisnika() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_recenzije_profil_korisnika, container, false);

        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        txtPoruka = (TextView) view.findViewById(R.id.txtPorukaRecenzije);

        dohvatRecenzija(ID);

        mRecyclerView = view.findViewById(R.id.recenzije_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecenzijeAdapterProfilKorisnika(listaRecenzija);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


    private void dohvatRecenzija(Integer ID) {

        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();

        String sql = "SELECT Datum, Komentar, Ocjena, Naziv FROM Recenzija r, Izvodjac i WHERE r.ID_korisnika = ('" + ID + "') AND r.ID_izvodjaca=i.ID_izvodjaca";
        //String sql = "SELECT r.Datum, r.Komentar, r.Ocjena, i.Naziv, u.Naziv as 'Naziv upita' FROM Izvodjac i, Recenzija r, Korisnik k, Upit u WHERE r.ID_korisnika=('" + ID + "') AND r.ID_izvodjaca= i.ID_izvodjaca AND r.ID_korisnika=k.ID_korisnika AND k.ID_korisnika=u.ID_korisnika";


        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {


                listaNazivaIzvodjaca.add(rs.getString("Naziv"));
                listaKomentaraRecenzija.add(rs.getString("Komentar"));
                listaDatumaRecenzija.add(rs.getDate("Datum"));
                listaOcjenaRecenzija.add(rs.getInt("Ocjena"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaRecenzija.clear();

        for (int i = 0; i < listaNazivaIzvodjaca.size(); i++) {

            nazivIzvodjaca = listaNazivaIzvodjaca.get(i);
            datumRecenzije = listaDatumaRecenzija.get(i);
            komentar = listaKomentaraRecenzija.get(i);
            ocjenaRecenzije = listaOcjenaRecenzija.get(i);

            RA = new ItemRecenzijaProfilKorisnika("Izvođač: " + nazivIzvodjaca, datumRecenzije, komentar, ocjenaRecenzije);
            listaRecenzija.add(RA);
        }

        listaNazivaIzvodjaca.clear();
        listaOcjenaRecenzija.clear();
        listaDatumaRecenzija.clear();
        listaKomentaraRecenzija.clear();

        if (listaRecenzija.isEmpty()){
            txtPoruka.setText("Trenutno nema recenzija...");
        }

    }
}
