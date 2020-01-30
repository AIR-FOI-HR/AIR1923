package com.mgradnja.Izvodjac;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfilaIzvodjacaActivity extends AppCompatActivity {

    public String[] zupanijeIzvodjac = new String[]{"Odaberite županiju","Zagrebacka", "Krapinsko-zagorska", "Sisacko-moslavacka",
            "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
            "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
            "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
            "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};

    public Spinner spinnerZupanijeIzvodjac;
    EditText txtNazivIzvodjaca, txtAdresaIzvodjaca, txtGradIzvodjaca, txtTelefonIzvodjaca, txtMailIzvodjaca, txtLozinkaIzvodjaca, txtPlozinkaIzvodjaca, txtBrojRacunaIzvodjaca;
    Button btnSpremi;

    Integer ID;


    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profila_izvodjaca);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Korisnički račun");
        }

        connectionClass = new ConnectionClass();

        txtNazivIzvodjaca = (EditText) findViewById(R.id.txtEditNazivIzvodjaca);
        txtAdresaIzvodjaca = (EditText) findViewById(R.id.txtEditAdresaIzvodjaca);
        txtGradIzvodjaca = (EditText) findViewById(R.id.txtEditGradIzvodjaca);
        txtTelefonIzvodjaca = (EditText) findViewById(R.id.txtEditTelefonIzvodjaca);
        txtMailIzvodjaca = (EditText) findViewById(R.id.txtEditMailIzvodjaca);
        txtLozinkaIzvodjaca = (EditText) findViewById(R.id.txtEditLozinkaIzvodjaca);
        txtPlozinkaIzvodjaca = (EditText) findViewById(R.id.txtEditPlozinkaIzvodjaca);
        txtBrojRacunaIzvodjaca = (EditText) findViewById(R.id.txtEditBrojRacunaIzvodjaca);
        btnSpremi = (Button) findViewById(R.id.btnEditIzvodjacSpremi);
        spinnerZupanijeIzvodjac = (Spinner) findViewById(R.id.spinProfilIzvodjacaZupanije);


        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_izvodjaca", 0);

        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanijeIzvodjac);
        spinnerZupanijeIzvodjac.setAdapter(adapterZupanije);

        dohvatPodatakaIzvodjac(ID);

        btnSpremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiEditIzvodjaca(ID);
                //finish();
            }
        });
    }


    public void dohvatPodatakaIzvodjac(Integer ID){

        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();


        String sql = "SELECT * from Izvodjac WHERE ID_izvodjaca=('" + ID + "')";

        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                txtNazivIzvodjaca.setText(rs.getString("Naziv"));
                txtAdresaIzvodjaca.setText(rs.getString("Adresa"));
                txtGradIzvodjaca.setText(rs.getString("Grad"));;
                txtTelefonIzvodjaca.setText(rs.getString("Telefon"));
                txtLozinkaIzvodjaca.setText(rs.getString("Lozinka"));
                txtPlozinkaIzvodjaca.setText(rs.getString("Lozinka"));
                txtBrojRacunaIzvodjaca.setText(rs.getString("Broj_racuna"));
                txtMailIzvodjaca.setText(rs.getString("Mail"));
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void spremiEditIzvodjaca(Integer ID){

        String naziv = txtNazivIzvodjaca.getText().toString();
        String adresa = txtAdresaIzvodjaca.getText().toString();
        String grad = txtGradIzvodjaca.getText().toString();
        String zupanija = spinnerZupanijeIzvodjac.getSelectedItem().toString();
        String telefon = txtTelefonIzvodjaca.getText().toString();
        String mail = txtMailIzvodjaca.getText().toString();
        String lozinka = txtLozinkaIzvodjaca.getText().toString();
        String pLozinka = txtPlozinkaIzvodjaca.getText().toString();
        String brojRacuna = txtBrojRacunaIzvodjaca.getText().toString();

        if (naziv.equals("") || adresa.equals("") || mail.equals("") || telefon.equals("") || lozinka.equals("") || pLozinka.equals("") || grad.equals("") || zupanija.equals("Odaberite županiju") || brojRacuna.equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }
        else{
            if (lozinka.equals(pLozinka)){
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null){
                        Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Statement st = con.createStatement();


                        String queriEditIzvodjac = "UPDATE Izvodjac SET Naziv = ('"+ naziv +"'), Adresa  = ('"+ adresa +"'), Telefon = ('"+ telefon +"'), Mail = ('"+ mail +"'), Lozinka = ('"+ lozinka +"'), Grad = ('"+ grad +"'), Zupanija = ('"+ zupanija +"'), Broj_racuna = ('"+ brojRacuna +"') WHERE ID_izvodjaca=('" + ID + "')";

                        if (st.executeUpdate(queriEditIzvodjac) == 1){
                            Toast.makeText(getApplicationContext(), "Podaci su uspješno promijenjeni!", Toast.LENGTH_LONG).show();
                            con.close();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Greška prilikom pohrane, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Lozinka i ponovljena lozinka nisu iste!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
