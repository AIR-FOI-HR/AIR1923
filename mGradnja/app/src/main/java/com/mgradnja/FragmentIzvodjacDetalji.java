package com.mgradnja;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FragmentIzvodjacDetalji extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    String nazivIzvodjaca;
    TextView naziv, oib, adresa, grad, zupanija, kontakt, mail, brRacuna;
    RatingBar ratingBar;

    public FragmentIzvodjacDetalji() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_detalji, container, false);

        naziv = view.findViewById(R.id.nazivIzvodjaca);
        oib = view.findViewById(R.id.oibIzvodjaca);
        adresa = view.findViewById(R.id.adresaIzvodjaca);
        grad = view.findViewById(R.id.gradIzvodjaca);
        zupanija = view.findViewById(R.id.zupanijaIzvodjaca);
        kontakt = view.findViewById(R.id.kontaktIzvodjaca);
        mail = view.findViewById(R.id.mailIzvodjaca);
        brRacuna = view.findViewById(R.id.brRacunaIzvodjaca);
        ratingBar = view.findViewById(R.id.ratingBar);


        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");
        
        dohvatiInfoOIzvodjacu(nazivIzvodjaca);

        return view;
    }


    private void dohvatiInfoOIzvodjacu(String nazivIzvodjaca) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        Integer idIzvodjaca = null;
        Float rating = null;

        try{
            String sql = "SELECT * from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                idIzvodjaca = rs.getInt("ID_izvodjaca");

                naziv.setText(rs.getString("Naziv"));
                oib.setText(rs.getString("OIB"));
                adresa.setText(rs.getString("Adresa"));
                grad.setText(rs.getString("Grad"));
                zupanija.setText(rs.getString("Zupanija"));
                kontakt.setText(rs.getString("Telefon"));
                mail.setText(rs.getString("Mail"));
                brRacuna.setText(rs.getString("Broj_racuna"));
            }

            String sql2 = "select avg(ocjena) as rating from Recenzija where ID_izvodjaca=('" + idIzvodjaca + "') ";
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(sql2);
            while (rs2.next()){
                rating = rs2.getFloat("rating");
                ratingBar.setRating(rating);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }



}
