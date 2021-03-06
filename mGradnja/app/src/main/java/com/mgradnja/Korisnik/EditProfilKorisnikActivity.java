package com.mgradnja.Korisnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfilKorisnikActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText txtImeEditKorisnika, txtPrezimeEditKorisnika, txtTelefonEditKorisnik, txtMailEditKorisnik, txtLozinkaEditKorisnik, txtPlozinkaEditKorisnik;
    Button btnSpremiEditKorisnika;

    Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_korisnik);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Korisnički račun");
        }

        connectionClass = new ConnectionClass();

        txtImeEditKorisnika = (EditText) findViewById(R.id.txtEditKorisnikIme);
        txtPrezimeEditKorisnika = (EditText) findViewById(R.id.txtEditKorisnikPrezime);
        txtMailEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikMail);
        txtTelefonEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikTelefon);
        txtLozinkaEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikLozinka);
        txtPlozinkaEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikPlozinka);
        btnSpremiEditKorisnika = (Button) findViewById(R.id.btnEditKorisnikSpremi);


        //DOHVAT ID-A KORISNIKA IZ PRIJAŠNJE AKTIVNOSTI
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

        dohvatPodatakaEditKorisnik(ID);


        btnSpremiEditKorisnika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiEditKorisnik(ID);
                //finish();
            }
        });

    }

    public void dohvatPodatakaEditKorisnik(Integer ID){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();


        String sql = "SELECT * from Korisnik WHERE ID_korisnika=('" + ID + "')";

        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                txtImeEditKorisnika.setText(rs.getString("Ime"));
                txtPrezimeEditKorisnika.setText(rs.getString("Prezime"));
                txtMailEditKorisnik.setText(rs.getString("Mail"));
                txtTelefonEditKorisnik.setText(rs.getString("Telefon"));
                txtLozinkaEditKorisnik.setText(rs.getString("Lozinka"));
                txtPlozinkaEditKorisnik.setText(rs.getString("Lozinka"));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void spremiEditKorisnik(Integer ID){

        String ime = txtImeEditKorisnika.getText().toString();
        String prezime = txtPrezimeEditKorisnika.getText().toString();
        String mail = txtMailEditKorisnik.getText().toString();
        String telefon = txtTelefonEditKorisnik.getText().toString();
        String lozinka = txtLozinkaEditKorisnik.getText().toString();
        String pLozinka = txtPlozinkaEditKorisnik.getText().toString();

        if (ime.equals("") || prezime.equals("") || mail.equals("") || telefon.equals("") || lozinka.equals("") || pLozinka.equals("")){
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


                        String queriEditKorisnik = "UPDATE Korisnik SET Ime = ('"+ ime +"'), Prezime  = ('"+ prezime +"'), Telefon = ('"+ telefon +"'), Mail = ('"+ mail +"'), Lozinka = ('"+ lozinka +"') WHERE ID_korisnika=('" + ID + "')";

                        if (st.executeUpdate(queriEditKorisnik) == 1){
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
                Toast.makeText(getApplicationContext(), "Lozinka i ponovljena lozinka se ne poklapaju!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //Intent intent = new Intent(this, ProfilKorisnikActivity.class);
                //intent.putExtra("ID_korisnika", ID);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
