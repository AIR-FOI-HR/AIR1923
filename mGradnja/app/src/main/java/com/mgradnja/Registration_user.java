package com.mgradnja;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Statement;

public class Registration_user extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button registracija, natragNaPrijavu;
    EditText txtIme, txtPrezime, txtMail, txtTelefon, txtLozinka, txtPlozinka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        connectionClass = new ConnectionClass();

        txtIme = findViewById(R.id.txtIme);
        txtPrezime = findViewById(R.id.txtPrezime);
        txtMail = findViewById(R.id.txtMail);
        txtTelefon = findViewById(R.id.txtTelefon);
        txtLozinka = findViewById(R.id.txtLozinka);
        txtPlozinka = findViewById(R.id.txtPlozinka);
        registracija = findViewById(R.id.btnRegistracija);
        natragNaPrijavu = findViewById(R.id.btnNatragNaPrijavu);

        registracija.setOnClickListener(v -> doRegistration());
        natragNaPrijavu.setOnClickListener(v -> backToLogin());

    }

    public void doRegistration(){
        String ime = txtIme.getText().toString();
        String prezime = txtPrezime.getText().toString();
        String mail = txtMail.getText().toString();
        String telefon = txtTelefon.getText().toString();
        String lozinka = txtLozinka.getText().toString();
        String plozinka = txtPlozinka.getText().toString();

        if (ime.equals("") || prezime.equals("") || mail.equals("") || telefon.equals("") || lozinka.equals("") || plozinka.equals("")){
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

                        String queryReg = "INSERT INTO Korisnik(Ime, Prezime, Mail, Lozinka, Telefon) " +
                                "VALUES(('"+ ime +"'), ('"+ prezime +"'), ('"+ mail +"'), ('"+ lozinka +"'), ('"+ telefon +"'))";

                        if (st.executeUpdate(queryReg) == 1){
                            Toast.makeText(getApplicationContext(), "Registracija uspješna, možete se prijaviti!", Toast.LENGTH_LONG).show();
                            txtIme.setText("");
                            txtPrezime.setText("");
                            txtMail.setText("");
                            txtTelefon.setText("");
                            txtLozinka.setText("");
                            txtPlozinka.setText("");
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
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
