package com.mgradnja;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FragmentIzvodjacDetalji extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    String nazivIzvodjaca;
    TextView oib, adresa, grad, zupanija, kontakt, mail, brRacuna;
    ImageView profilna;

    public FragmentIzvodjacDetalji() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_detalji, container, false);

        profilna = view.findViewById(R.id.slikaIzvodjaca);
        oib = view.findViewById(R.id.oibIzvodjaca);
        adresa = view.findViewById(R.id.adresaIzvodjaca);
        grad = view.findViewById(R.id.gradIzvodjaca);
        zupanija = view.findViewById(R.id.zupanijaIzvodjaca);
        kontakt = view.findViewById(R.id.kontaktIzvodjaca);
        mail = view.findViewById(R.id.mailIzvodjaca);
        brRacuna = view.findViewById(R.id.brRacunaIzvodjaca);


        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");
        
        dohvatiInfoOIzvodjacu(nazivIzvodjaca);

        return view;
    }

    private void dohvatiInfoOIzvodjacu(String nazivIzvodjaca) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        String sql = "SELECT * from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";

        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                //profilna
                oib.setText(rs.getString("OIB"));
                adresa.setText(rs.getString("Adresa"));
                grad.setText(rs.getString("Grad"));
                zupanija.setText(rs.getString("Zupanija"));
                kontakt.setText(rs.getString("Telefon"));
                mail.setText(rs.getString("Mail"));
                brRacuna.setText(rs.getString("Broj_racuna"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
