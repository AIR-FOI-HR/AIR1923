package com.mgradnja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private Button btnPrijava;
    private Button btnRegistracija;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrijava = findViewById(R.id.btnPrijava);
        btnRegistracija = findViewById(R.id.btnRegistracija);
    }
}
