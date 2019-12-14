package com.mgradnja;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FragmentIzvodjacRecenzije extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    String nazivIzvodjaca;
    String ime, prezime, komentar;
    Date datum;
    Integer ocjena;

    public ArrayList<String> listaImena = new ArrayList<>();
    public ArrayList<String> listaPrezimena = new ArrayList<>();
    public ArrayList<Integer> listaOcjena = new ArrayList<>();
    public ArrayList<String> listaKomentara = new ArrayList<>();
    public ArrayList<Date> listaDatuma = new ArrayList<>();
    public ArrayList<RecenzijaEntity> listaRecenzija = new ArrayList<>();

    public RecenzijaEntity recenzijaEntity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public FragmentIzvodjacRecenzije() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_recenzije, container, false);

        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");

        dohvatiRecenzijeZaIzvodaca(nazivIzvodjaca);

        recyclerView = view.findViewById(R.id.recenzijeRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RecenzijaAdapter(listaRecenzija);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void dohvatiRecenzijeZaIzvodaca(String nazivIzvodjaca) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        Integer idIzvodjaca = null;

        try{
            String sql = "SELECT ID_izvodjaca from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                idIzvodjaca = rs.getInt("ID_izvodjaca");
            }

            String sql2 = "SELECT Ime, Prezime, Datum, Komentar, Ocjena FROM Korisnik, Recenzija WHERE Korisnik.ID_korisnika=Recenzija.ID_korisnika AND ID_izvodjaca=('" + idIzvodjaca + "') ";
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(sql2);
            while (rs2.next()){
                listaImena.add(rs2.getString("Ime"));
                listaPrezimena.add(rs2.getString("Prezime"));
                listaOcjena.add(rs2.getInt("Ocjena"));
                listaKomentara.add(rs2.getString("Komentar"));
                listaDatuma.add(rs2.getDate("Datum"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for ( int i = 0; i < listaImena.size(); i++ ){
            ime = listaImena.get(i);
            prezime = listaPrezimena.get(i);
            ocjena = listaOcjena.get(i);
            komentar = listaKomentara.get(i);
            datum = listaDatuma.get(i);

            recenzijaEntity = new RecenzijaEntity(ime, prezime, komentar, datum, ocjena);
            listaRecenzija.add(recenzijaEntity);
        }
    }


}
