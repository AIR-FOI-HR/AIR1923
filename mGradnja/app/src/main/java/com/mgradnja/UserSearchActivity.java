package com.mgradnja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity {

    ListView listView;
    EditText upisaniNaziv;
    private ArrayAdapter<String> adapter;
    public ArrayList<String> izvodjaci = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.listaIzvodjaca);
        upisaniNaziv = findViewById(R.id.upisaniNaziv);

        dohvatiIzvodjace();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_izvodjaci, R.id.nazivIzvodjaca, izvodjaci);
        listView.setAdapter(adapter);

        upisaniNaziv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                adapter.getFilter().filter(cs);
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                adapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable cs) {
                adapter.getFilter().filter(cs);
            }
        });
    }

    public void dohvatiIzvodjace(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String sql = "SELECT * FROM Izvodjac";

        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                izvodjaci.add(rs.getString("Naziv"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
