package com.mgradnja;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FragmentOpisProfilKorisnika extends Fragment {


    TextView txtImeKorisnika, txtPrezimeKorisnika, txtMailKorisnika, txtTelefonKorisnika;
    ImageView imgProfilaKorisnika;
    Button editKorisnika;


    public FragmentOpisProfilKorisnika(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_opis_profil_korisnika, container, false);

        txtImeKorisnika = (TextView) view.findViewById(R.id.txtImeKorisnika);
        txtPrezimeKorisnika = (TextView) view.findViewById(R.id.txtPrezimeKorisnika);
        txtMailKorisnika = (TextView) view.findViewById(R.id.txtProfilKorisnikMailIspis);
        txtTelefonKorisnika = (TextView) view.findViewById(R.id.txtProfilKorisnikTelefonIspis);
        imgProfilaKorisnika = (ImageView) view.findViewById(R.id.imgProfilKorisnika);


        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);


        dohvatiNovo(ID);


        editKorisnika = (Button) view.findViewById(R.id.btnUrediKorisnickiRacun);
        editKorisnika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtvoriEditRacuna(ID);
            }
        });


        return view;
    }

    public void dohvatiNovo(Integer ID){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();


        String sql = "SELECT * from Korisnik WHERE ID_korisnika=('" + ID + "')";


        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                txtImeKorisnika.setText(rs.getString("Ime"));
                txtPrezimeKorisnika.setText(rs.getString("Prezime"));
                txtMailKorisnika.setText(rs.getString("Mail"));
                txtTelefonKorisnika.setText(rs.getString("Telefon"));
                /*if (rs.getString("Slika").equals("")){
                    imgProfilaKorisnika.setImageResource(R.drawable.korisnik);
                }
                else{
                    imgProfilaKorisnika.setImageResource(rs.getByte("Slika"));
                }*/
                imgProfilaKorisnika.setImageResource(rs.getByte("Slika"));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void OtvoriEditRacuna(Integer ID){
        Intent intent = new Intent(getActivity(), EditProfilKorisnikActivity.class);
        intent.putExtra("ID_korisnika", ID);
        startActivity(intent);
    }

}