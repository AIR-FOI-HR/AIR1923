package com.mgradnja;


import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class FragmentUpitiKorisnika extends Fragment {


    ConnectionClass connectionClass;
    Connection connection;

    public ArrayList<String> listaNaziva = new ArrayList<String>();
    public ArrayList<Date> listaDatuma = new ArrayList<Date>();
    public ArrayList<String> listaOpisa = new ArrayList<String >();
    public ArrayList<String> listaAdresa = new ArrayList<String>();
    public ArrayList<String> listaGradova = new ArrayList<String>();
    public ArrayList<String> listaZupanija = new ArrayList<String>();

    private String naziv, opis, adresa, grad ,zupanija;
    private Date datum;

    public ArrayList<ItemUpitiProfilKorisnika> listaUpita = new ArrayList<ItemUpitiProfilKorisnika>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemUpitiProfilKorisnika UA;

    TextView txtPorukaUpiti;

    public FragmentUpitiKorisnika() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_upiti_korisnika, container, false);


        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        txtPorukaUpiti = (TextView) view.findViewById(R.id.txtPorukaUpiti);

        dohvatUpita(ID);

        mRecyclerView = view.findViewById(R.id.upiti_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpitiAdapterProfilKorisnika(listaUpita);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;

    }


    public void dohvatUpita(Integer ID){
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();

        String sql = "SELECT * FROM Upit WHERE ID_korisnika = ('" + ID + "')";

        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){


                listaNaziva.add(rs.getString("Naziv"));
                listaOpisa.add(rs.getString("Opis"));
                listaDatuma.add(rs.getDate("Datum"));
                listaAdresa.add(rs.getString("Adresa"));
                listaGradova.add(rs.getString("Grad"));
                listaZupanija.add(rs.getString("Zupanija"));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        listaUpita.clear();

        for(int i = 0; i<listaNaziva.size(); i++){

            naziv = listaNaziva.get(i);
            datum = listaDatuma.get(i);
            opis = listaOpisa.get(i);
            adresa = listaAdresa.get(i);
            grad = listaGradova.get(i);
            zupanija = listaZupanija.get(i);

            UA = new ItemUpitiProfilKorisnika("Naziv: "+naziv, datum,"Opis: " + opis, "Adresa: " + adresa, "Grad: " + grad, "Županija: " + zupanija);
            listaUpita.add(UA);
        }

        listaNaziva.clear();
        listaOpisa.clear();
        listaDatuma.clear();
        listaAdresa.clear();
        listaGradova.clear();
        listaZupanija.clear();

        if (listaUpita.isEmpty()){
            txtPorukaUpiti.setText("Nemate upita");
        }

        }


}
