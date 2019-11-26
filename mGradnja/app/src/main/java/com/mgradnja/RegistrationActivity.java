package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegistrationActivity extends AppCompatActivity {
    ImageView imgKorisnik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imgKorisnik = findViewById(R.id.imgKorisnik);
        imgKorisnik.bringToFront();

    }

    public void openUserRegistration(View view){
        Intent intent = new Intent(this, Registration_user.class);
        startActivity(intent);
    }

    public void openPerformerRegistration(View view){
        Intent intent = new Intent(this, Registration_performer.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
