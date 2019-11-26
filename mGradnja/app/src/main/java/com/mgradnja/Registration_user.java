package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registration_user extends AppCompatActivity {

    Button registracija, odustani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        registracija = findViewById(R.id.btnRegistracija);
        odustani = findViewById(R.id.btnOdustani);

        registracija.setOnClickListener(v -> napraviRegistraciju());
        odustani.setOnClickListener(v -> odustaniOdRegistracije());

    }

    public void napraviRegistraciju(){

    }

    public void odustaniOdRegistracije(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
