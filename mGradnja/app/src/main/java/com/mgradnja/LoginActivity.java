package com.mgradnja;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText mail, loz;
    Button prijava, registracija;
    ProgressBar progressBar;


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
                        String queryObrtnik = "select * from Obrt where Mail='" + mail1 + "' and Lozinka='" + lozinka + "'";

                        Statement stmtKorisnik = con.createStatement();
                        Statement stmtObrtnik = con.createStatement();

                        ResultSet rsKorisnik = stmtKorisnik.executeQuery(queryKorisnik);
                        ResultSet rsObrtnik = stmtObrtnik.executeQuery(queryObrtnik);

                        if(rsKorisnik.next())
                        {

                            poruka = "Prijava uspješna!";
                            uspjeh =true;
                            //OpenMainActivity();

                        }

                        else if (rsObrtnik.next())
                        {
                            poruka = "Prijava uspješna!";
                            uspjeh = true;
                            //OpenMainActivity();
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
    public void OpenMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}

