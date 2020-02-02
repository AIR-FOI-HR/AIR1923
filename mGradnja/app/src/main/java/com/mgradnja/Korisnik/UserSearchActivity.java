package com.mgradnja.Korisnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.mgradnja.ConnectionClass;
import com.mgradnja.Korisnik.GlavniIzbornikKorisnik;
import com.mgradnja.Korisnik.IzvodjacInfoActivity;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity {
    Integer ID;
    ListView listView;
    EditText upisaniNaziv;
    private ArrayAdapter<String> adapter;
    public ArrayList<String> izvodjaci = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_korisnika", 0);

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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String nazivIzvodjaca = (String) listView.getItemAtPosition(position);
            OpenIzvodjacInfoActivity(nazivIzvodjaca);
        });
    }

    public void dohvatiIzvodjace(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String sql = "SELECT * FROM Izvodjac ORDER BY Naziv ASC";

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

    private void OpenIzvodjacInfoActivity(String nazivIzvodjaca){
        Intent intent = new Intent(this, IzvodjacInfoActivity.class);
        intent.putExtra("ID_korisnika", ID);
        intent.putExtra("nazivIzvodjaca", nazivIzvodjaca);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
                intent.putExtra("ID_korisnika", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
