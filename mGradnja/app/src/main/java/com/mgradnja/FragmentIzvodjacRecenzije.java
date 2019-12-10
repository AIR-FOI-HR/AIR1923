package com.mgradnja;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FragmentIzvodjacRecenzije extends Fragment {

    String nazivIzvodjaca;
    ConnectionClass connectionClass;
    Connection con;
    Integer idIzvodjaca;
    ListView listaRecenzija;
    ListAdapter adapter;
    ArrayList<String> recenzije;

    public FragmentIzvodjacRecenzije() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_recenzije, container, false);

        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");

        listaRecenzija = view.findViewById(R.id.lstRecenzija);
        recenzije = new ArrayList<>();

        dohvatiRecenzijeZaIzvodaca(nazivIzvodjaca);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recenzije);
        listaRecenzija.setAdapter(adapter);

        return view;
    }

    private void dohvatiRecenzijeZaIzvodaca(String nazivIzvodjaca) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        try{
            String sql = "SELECT ID_izvodjaca from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                idIzvodjaca = rs.getInt("ID_izvodjaca");
            }

            String sql2 = "SELECT * FROM Recenzija WHERE ID_izvodjaca=('" + idIzvodjaca + "') ";
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(sql2);
            while (rs2.next()){
                recenzije.add(rs2.getString("Komentar"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


}
