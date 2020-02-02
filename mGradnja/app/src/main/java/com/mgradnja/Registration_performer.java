package com.mgradnja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Registration_performer extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button registracija, natragNaPrijavu, btnDjelatnost;
    EditText txtOIB, txtNaziv, txtAdresa, txtGrad, txtZupanija, txtTelefon, txtMail, txtBrRacuna, txtLozinka, txtPlozinka;
    Spinner idPravniStatus;
    ArrayList<String> listaDjelatnosti = new ArrayList<>();
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> odabraneDjelatnosti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_performer);

        connectionClass = new ConnectionClass();
        btnDjelatnost = findViewById(R.id.btnOdabirDjelatnosti);

        dohvatiDjelatnosti();
        listItems = new String[listaDjelatnosti.size()];
        listItems = listaDjelatnosti.toArray(listItems);
        checkedItems = new boolean[listItems.length];

        btnDjelatnost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registration_performer.this);
                builder.setTitle("Vaše djelatnosti:");
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            odabraneDjelatnosti.add(position);
                        }
                        else {
                            odabraneDjelatnosti.remove(Integer.valueOf(position));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < odabraneDjelatnosti.size(); i++) {
                            item = item + (odabraneDjelatnosti.get(i)+1);
                            if (i != odabraneDjelatnosti.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNeutralButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            odabraneDjelatnosti.clear();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        Spinner pravniStatus = findViewById(R.id.pravniStatus);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Registration_performer.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pravniStatusi));
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pravniStatus.setAdapter(statusAdapter);

        idPravniStatus = findViewById(R.id.pravniStatus);
        txtOIB = findViewById(R.id.txtOib);
        txtNaziv = findViewById(R.id.txtNaziv);
        txtAdresa = findViewById(R.id.txtAdresa);
        txtGrad = findViewById(R.id.txtCijenaPonude);
        txtZupanija = findViewById(R.id.txtZupanija);
        txtTelefon = findViewById(R.id.txtTelefon);
        txtMail = findViewById(R.id.txtMail);
        txtBrRacuna = findViewById(R.id.txtBrojRacuna);
        txtLozinka = findViewById(R.id.txtLozinka);
        txtPlozinka = findViewById(R.id.txtPlozinka);
        registracija = findViewById(R.id.btnRegistracija);
        natragNaPrijavu = findViewById(R.id.btnNatragNaPrijavu);

        registracija.setOnClickListener(v -> doRegistration());
        natragNaPrijavu.setOnClickListener(v -> backToLogin());

    }


    public void dohvatiDjelatnosti(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        try {
            String query = "select Naziv from Djelatnost";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                listaDjelatnosti.add(rs.getString("Naziv"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int dohvatiNoviId(String oib){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();
        int ID = 0;

        try {
            String query = "SELECT ID_izvodjaca FROM Izvodjac WHERE OIB = ('"+ oib+ "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                ID = rs.getInt("ID_izvodjaca");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ID;
    }


    public void doRegistration(){
        Long idPravnogStatusa = idPravniStatus.getSelectedItemId();
        String oib = txtOIB.getText().toString();
        String naziv = txtNaziv.getText().toString();
        String adresa = txtAdresa.getText().toString();
        String grad = txtGrad.getText().toString();
        String zupanija = txtZupanija.getText().toString();
        String telefon = txtTelefon.getText().toString();
        String mail = txtMail.getText().toString();
        String brRacuna = txtBrRacuna.getText().toString();
        String lozinka = txtLozinka.getText().toString();
        String plozinka = txtPlozinka.getText().toString();


        if (idPravnogStatusa == 0 || oib.equals("") || naziv.equals("") || adresa.equals("") || grad.equals("") || zupanija.equals("") || telefon.equals("") || mail.equals("") || brRacuna.equals("") || lozinka.equals("") || plozinka.equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (lozinka.equals(plozinka)){
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null){
                        Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Statement st = con.createStatement();
                        String queryReg = "INSERT INTO Izvodjac(OIB, Naziv, Adresa, Grad, Zupanija, Telefon, Mail, Lozinka, Broj_racuna, ID_pravnog_statusa) " +
                                "VALUES(('"+ oib +"'), ('"+ naziv +"'), ('"+ adresa +"'), ('"+ grad +"'), ('"+ zupanija +"'), ('"+ telefon +"'), ('"+ mail +"'), ('"+ lozinka +"'), ('"+ brRacuna +"'), ('"+ idPravnogStatusa +"'))";

                        if (st.executeUpdate(queryReg) == 1){
                            int noviID = dohvatiNoviId(oib);

                            for (int i = 0; i < odabraneDjelatnosti.size(); i++){
                                Statement st2 = con.createStatement();
                                String queryReg2 = "INSERT INTO Djelatnosti_izvodjaca(ID_djelatnosti, ID_izvodjaca) VALUES ( ("+(odabraneDjelatnosti.get(i)+1)+"), ("+noviID+") )";
                                st2.executeUpdate(queryReg2);
                            }

                            Toast.makeText(getApplicationContext(), "Registracija uspješna, možete se prijaviti!", Toast.LENGTH_LONG).show();
                            idPravniStatus.setSelection(0);
                            txtOIB.getText().clear();
                            txtNaziv.getText().clear();
                            txtMail.getText().clear();
                            txtBrRacuna.getText().clear();
                            txtLozinka.getText().clear();
                            txtPlozinka.getText().clear();
                            txtAdresa.getText().clear();
                            txtGrad.getText().clear();
                            txtZupanija.getText().clear();
                            txtTelefon.getText().clear();
                            for (int i = 0; i < checkedItems.length; i++) {
                                checkedItems[i] = false;
                                odabraneDjelatnosti.clear();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Greška prilikom registracije, pokušajte ponovno!", Toast.LENGTH_LONG).show();
                        }

                        con.close();

                    }
                }
                catch (Exception ex){
                    String porukaIznimke;
                    porukaIznimke = ex.getMessage();
                    Toast.makeText(getApplicationContext(), porukaIznimke, Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(getApplicationContext(), "Lozinka i potvrda lozinke se ne poklapaju!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
