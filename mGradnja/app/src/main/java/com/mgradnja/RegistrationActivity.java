package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class RegistrationActivity extends AppCompatActivity {
    ImageView imgKorisnik;

    Button userRegistration, performerRegistration, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imgKorisnik = findViewById(R.id.imgKorisnik);
        imgKorisnik.bringToFront();

        userRegistration = findViewById(R.id.btnRegistracijaKorisnik);
        performerRegistration = findViewById(R.id.btnRegistracijaIzvodjac);
        login = findViewById(R.id.btnPrijavaIzRegistracije);

        userRegistration.setOnClickListener(v -> openUserRegistration());
        performerRegistration.setOnClickListener(v -> openPerformerRegistration());
        login.setOnClickListener(v -> openLoginActivity());
    }

    public void openUserRegistration(){
        Intent intent = new Intent(this, Registration_user.class);
        startActivity(intent);
    }

    public void openPerformerRegistration(){
        Intent intent = new Intent(this, Registration_performer.class);
        startActivity(intent);
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
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
