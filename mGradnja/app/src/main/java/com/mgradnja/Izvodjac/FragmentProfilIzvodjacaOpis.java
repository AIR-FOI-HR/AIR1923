package com.mgradnja.Izvodjac;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FragmentProfilIzvodjacaOpis extends Fragment {

    TextView txtNazivIzvodjaca, txtAdresaIzvodjaca, txtGradIzvodjaca, txtZupanijaIzvodjaca, txtMailIzvodjaca, txtTelefonIzvodjaca, txtBrojRacunaIzvodjaca, txtOIBIzvodjaca;
    Button btnUrediRacunIzvodjaca;
    RatingBar prosjecnaOcjena;

    public FragmentProfilIzvodjacaOpis(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opis_profil_izvodjaca, container, false);

        txtNazivIzvodjaca = (TextView) view.findViewById(R.id.txtNazivIzvodjacaProfil);
        txtAdresaIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaAdresaIspis);
        txtGradIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaGradIspis);
        txtZupanijaIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaZupanijaIspis);
        txtBrojRacunaIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaBrojRacunaIspis);
        txtMailIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaMailIspis);
        txtTelefonIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaTelefonIspis);
        txtOIBIzvodjaca = (TextView) view.findViewById(R.id.txtProfilIzvodjacaOIBIspis);
        btnUrediRacunIzvodjaca = (Button) view.findViewById(R.id.btnUrediRacunIzvodjaca);
        prosjecnaOcjena = (RatingBar) view.findViewById(R.id.ratingProsjecnaOcjena);

        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_izvodjaca", 0);

        dohvatiPodatkeIzvodjaca(ID);

        btnUrediRacunIzvodjaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uredjivanjeProfila(ID);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_izvodjaca", 0);


        dohvatiPodatkeIzvodjaca(ID);
    }


    public void dohvatiPodatkeIzvodjaca(Integer ID){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        Float avgOcjena = null;



        try{
            String sql = "SELECT * FROM Izvodjac WHERE ID_izvodjaca=('" + ID + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){

                txtNazivIzvodjaca.setText(rs.getString("Naziv"));
                txtOIBIzvodjaca.setText(rs.getString("OIB"));
                txtAdresaIzvodjaca.setText(rs.getString("Adresa"));
                txtGradIzvodjaca.setText(rs.getString("Grad"));
                txtZupanijaIzvodjaca.setText(rs.getString("Zupanija"));
                txtTelefonIzvodjaca.setText(rs.getString("Telefon"));
                txtMailIzvodjaca.setText(rs.getString("Mail"));
                txtBrojRacunaIzvodjaca.setText(rs.getString("Broj_racuna"));

            }

            String sql2 = "select avg(ocjena) as rating from Recenzija where ID_izvodjaca=('" + ID + "') ";
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(sql2);
            while (rs2.next()){
                avgOcjena = rs2.getFloat("rating");
                prosjecnaOcjena.setRating(avgOcjena);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void uredjivanjeProfila(Integer ID) {
        Intent intentProfil = new Intent(getActivity(), EditProfilaIzvodjacaActivity.class);
        intentProfil.putExtra("ID_izvodjaca", ID);
        startActivity(intentProfil);
    }
}
