package com.mgradnja.Izvodjac;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.ConnectionClass;
import com.mgradnja.ItemRecenzijaProfilKorisnika;
import com.mgradnja.R;
import com.mgradnja.RecenzijeAdapterProfilKorisnika;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class FragmentProfilIzvodjacaRecenzije extends Fragment {

    ConnectionClass connectionClass;
    Connection connection;

    public ArrayList<String> listaImenaKorisnika = new ArrayList<>();
    public ArrayList<String> listaPrezimenaKorisnika = new ArrayList<>();
    public ArrayList<Date> listaDatuma = new ArrayList<>();
    public ArrayList<String> listaKomentaraRecenzija = new ArrayList<>();
    public ArrayList<Integer> listaOcjenaRecenzija = new ArrayList<>();

    private String imeKorisnika, prezimeKorisnika, komentar;
    private Integer ocjenaKorisnika;
    private Date datumRecenzije;

    public ArrayList<ItemRecenzijaProfilIzvodjaca> listaRecenzijaIzvodjaca = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemRecenzijaProfilIzvodjaca RI;

    TextView txtPoruka;

    public FragmentProfilIzvodjacaRecenzije(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recenzije_profil_izvodjaca, container, false);

        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_izvodjaca", 0);

        txtPoruka = (TextView) view.findViewById(R.id.txtPorukaRecenzijeIzvodjac);

        dohvatRecenzijaIzvodjaca(ID);



        mRecyclerView = view.findViewById(R.id.recenzijeIzvodjac_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecenzijaAdapterProfilIzvodjaca(listaRecenzijaIzvodjaca);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private void dohvatRecenzijaIzvodjaca(Integer ID) {

        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();

        String sql = "SELECT Ime, Prezime, Komentar, Datum, Ocjena FROM Recenzija r, Korisnik k WHERE r.ID_izvodjaca = ('" + ID + "') AND r.ID_korisnika=k.ID_korisnika";


        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                listaImenaKorisnika.add(rs.getString("Ime"));
                listaPrezimenaKorisnika.add(rs.getString("Prezime"));
                listaKomentaraRecenzija.add(rs.getString("Komentar"));
                listaDatuma.add(rs.getDate("Datum"));
                listaOcjenaRecenzija.add(rs.getInt("Ocjena"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaRecenzijaIzvodjaca.clear();

        for (int i = 0; i < listaImenaKorisnika.size(); i++) {

            imeKorisnika = listaImenaKorisnika.get(i);
            prezimeKorisnika = listaPrezimenaKorisnika.get(i);
            datumRecenzije = listaDatuma.get(i);
            komentar = listaKomentaraRecenzija.get(i);
            ocjenaKorisnika = listaOcjenaRecenzija.get(i);

            RI = new ItemRecenzijaProfilIzvodjaca( imeKorisnika,  prezimeKorisnika, datumRecenzije, komentar, ocjenaKorisnika);
            listaRecenzijaIzvodjaca.add(RI);
        }

        listaImenaKorisnika.clear();
        listaPrezimenaKorisnika.clear();
        listaOcjenaRecenzija.clear();
        listaDatuma.clear();
        listaKomentaraRecenzija.clear();

        if (listaRecenzijaIzvodjaca.isEmpty()){
            txtPoruka.setText("Trenutno nema recenzija.");
        }

    }


}
