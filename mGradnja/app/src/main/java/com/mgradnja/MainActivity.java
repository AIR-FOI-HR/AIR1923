package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mgradnja.Izvodjac.GlavniIzbornikIzvodjac;
import com.mgradnja.Korisnik.GlavniIzbornikKorisnik;


public class MainActivity extends AppCompatActivity {

    private Button btnPrijava;
    private Button btnRegistracija;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (sp.contains("id") && sp.contains("korisnik")){
            OpenGlavniIzbornikActivity(sp.getInt("id", 0));
        }
        else if (sp.contains("id") && sp.contains("izvodjac")){
            OpenGlavniIzbornikIzvodjac(sp.getInt("id", 0));
        }

        btnPrijava = findViewById(R.id.btnPrijava);
        btnRegistracija = findViewById(R.id.btnRegistracija);

        btnPrijava.setOnClickListener(v -> openLoginActivity());
        btnRegistracija.setOnClickListener(v -> openRegistrationActivity(v));
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void openRegistrationActivity(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void OpenGlavniIzbornikIzvodjac(Integer id) {
        Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
        intent.putExtra("ID_izvodjaca", id);
        startActivity(intent);
        finish();
    }

    private void OpenGlavniIzbornikActivity(Integer ID) {
        Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
        intent.putExtra("ID_korisnika", ID);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}


