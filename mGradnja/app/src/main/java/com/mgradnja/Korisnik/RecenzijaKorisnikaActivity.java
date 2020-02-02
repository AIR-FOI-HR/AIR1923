package com.mgradnja.Korisnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecenzijaKorisnikaActivity extends AppCompatActivity {

    String nazivIzvodjaca;
    EditText komentar;
    Integer ID_korisnika, ID_izvodjaca;
    Button recenziraj, odaberiOcjenu;
    Integer ocjena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzija_korisnika);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Recenziranje izvođača radova");
        }

        Intent intent = getIntent();
        ID_korisnika = intent.getIntExtra("ID_korisnika",0);
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");
        dohvatiIdIzvodjaca(nazivIzvodjaca);

        recenziraj = findViewById(R.id.btnRecenziraj);
        komentar = findViewById(R.id.tekstKomentara);
        odaberiOcjenu = findViewById(R.id.btnOdabirOcjene);

        odaberiOcjenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecenzijaKorisnikaActivity.this);
                builder.setTitle("Odaberite ocjenu");

                String[] animals = {"1", "2", "3", "4", "5"};
                int checkedItem = 0;
                builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ocjena = which+1;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recenziraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiRecenziju(ID_korisnika, ID_izvodjaca);
            }
        });
    }

    private void spremiRecenziju(Integer ID_korisnika, Integer ID_izvodjaca){
        ConnectionClass connectionClass = new ConnectionClass();

        String tekstKomentara = komentar.getText().toString();
        String datum = dohvatiDatum();

        if (tekstKomentara.equals("")){
            Toast.makeText(getApplicationContext(), "Niste napisali komentar!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Connection con = connectionClass.CONN();
                if (con == null){
                    Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Statement st = con.createStatement();
                    String queryReg = "INSERT INTO Recenzija(Datum, Komentar, Ocjena, ID_korisnika, ID_izvodjaca) " +
                            "VALUES(('"+ datum +"'), ('"+ tekstKomentara +"'), ("+ ocjena +"), ("+ ID_korisnika +"), ("+ ID_izvodjaca +"))";

                    if (st.executeUpdate(queryReg) == 1){
                        Toast.makeText(getApplicationContext(), "Recenzija spremljena!", Toast.LENGTH_LONG).show();
                        komentar.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Greška prilikom recenziranja, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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
    }

    private void dohvatiIdIzvodjaca(String nazivIzvodjaca){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();
        String query = "SELECT ID_izvodjaca FROM Izvodjac WHERE Naziv = ('"+nazivIzvodjaca+"')";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                ID_izvodjaca = rs.getInt("ID_izvodjaca");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String dohvatiDatum() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(calendar.getTime());
        return strDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent recenzija = new Intent(getApplicationContext(), IzvodjacInfoActivity.class);
                recenzija.putExtra("nazivIzvodjaca", nazivIzvodjaca);
                recenzija.putExtra("ID_korisnika", ID_korisnika);
                startActivity(recenzija);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent recenzija = new Intent(getApplicationContext(), IzvodjacInfoActivity.class);
        recenzija.putExtra("nazivIzvodjaca", nazivIzvodjaca);
        recenzija.putExtra("ID_korisnika", ID_korisnika);
        startActivity(recenzija);
        finish();
    }

}
