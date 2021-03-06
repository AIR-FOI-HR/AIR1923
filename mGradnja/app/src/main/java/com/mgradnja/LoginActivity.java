package com.mgradnja;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mgradnja.Izvodjac.GlavniIzbornikIzvodjac;
import com.mgradnja.Korisnik.GlavniIzbornikKorisnik;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText mail, loz;
    Button prijava, registracija;
    ProgressBar progressBar;
    Integer ID;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionClass = new ConnectionClass();
        mail = findViewById(R.id.txtMail);
        loz = findViewById(R.id.txtLozinka);
        prijava = findViewById(R.id.btnPrijaviSe);
        registracija = findViewById(R.id.btnRegistracijaIzPrijave);
        progressBar = findViewById(R.id.progressBar);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        registracija.setOnClickListener(v -> openRegistrationActivity());

        prijava.setOnClickListener(v -> {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute();
        });

        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationActivity();
            }
        });

        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute();
            }

        });
    }

    public class DoLogin extends AsyncTask<String,String,String>
    {
        String poruka = "";
        Boolean uspjeh = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if(uspjeh)
            {
                Toast.makeText(LoginActivity.this , "Prijava uspješna!" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }

        String mail1 = mail.getText().toString();
        String lozinka = loz.getText().toString();


        @Override
        protected String doInBackground(String... params) {
            if(mail1.trim().equals("")|| lozinka.trim().equals(""))
                poruka = "Unesite mail i lozinku!";
            else
            {
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null) {
                        poruka = "Greška sa spajanjem na SQL server";
                    } else {

                        String queryKorisnik = "select * from Korisnik where Mail='" + mail1 + "' and Lozinka='" + lozinka + "'";
                        String queryObrtnik = "select * from Izvodjac where Mail='" + mail1 + "' and Lozinka='" + lozinka + "'";

                        Statement stmtKorisnik = con.createStatement();
                        Statement stmtObrtnik = con.createStatement();

                        ResultSet rsKorisnik = stmtKorisnik.executeQuery(queryKorisnik);
                        ResultSet rsObrtnik = stmtObrtnik.executeQuery(queryObrtnik);

                        if(rsKorisnik.next())
                        {
                            ID = rsKorisnik.getInt("ID_korisnika");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("id", ID);
                            editor.putString("korisnik", "korisnik");
                            editor.commit();
                            poruka = "Prijava uspješna!";
                            uspjeh =true;
                            OpenGlavniIzbornikActivity(ID);

                        }

                        else if (rsObrtnik.next())
                        {
                            ID = rsObrtnik.getInt("ID_izvodjaca");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("id", ID);
                            editor.putString("izvodjac", "izvodjac");
                            editor.commit();
                            poruka = "Prijava uspješna!";
                            uspjeh = true;
                            OpenGlavniIzbornikIzvodjac(ID);

                        }
                        else
                        {
                            poruka = "Nepostojeći podaci";
                            uspjeh = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    uspjeh = false;
                    poruka = ex.getMessage();
                }
            }
            return poruka;
        }
    }

    private void OpenGlavniIzbornikIzvodjac(Integer id) {
        Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ID_izvodjaca", id);
        startActivity(intent);
        finish();
    }

    private void OpenGlavniIzbornikActivity(Integer ID) {
        Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
        intent.putExtra("ID_korisnika", ID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void openRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}




