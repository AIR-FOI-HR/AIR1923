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

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FragmentUpitiKorisnika extends Fragment {


    ConnectionClass connectionClass;
    Connection connection;

    public ArrayList<String> listaNaziva = new ArrayList<String>();
    public ArrayList<Date> listaDatuma = new ArrayList<Date>();
    public ArrayList<String> listaOpisa = new ArrayList<String >();

    private String naziv, opis;
    private Date datum;

    public ArrayList<ItemUpitiProfilKorisnika> listaUpita = new ArrayList<ItemUpitiProfilKorisnika>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemUpitiProfilKorisnika UA;

    public FragmentUpitiKorisnika() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_upiti_korisnika, container, false);



        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

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

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for(int i = 0; i<listaNaziva.size(); i++){

            naziv = listaNaziva.get(i);
            datum = listaDatuma.get(i);
            opis = listaOpisa.get(i);

            UA = new ItemUpitiProfilKorisnika("Naziv: "+naziv, datum,"Opis: " + opis);
            listaUpita.add(UA);
        }

        }

}
