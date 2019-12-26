package com.mgradnja.Izvodjac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mgradnja.R;

public class OfferActivity extends AppCompatActivity {

    public String NazivUpita;
    public Integer ID_Izvodjaca;
    public Integer ID_Upita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Intent intent = getIntent();
        ID_Izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);
        ID_Upita = intent.getIntExtra("ID_upita", 0);

    }
}
