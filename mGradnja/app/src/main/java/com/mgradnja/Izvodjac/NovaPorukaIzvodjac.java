package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NovaPorukaIzvodjac extends AppCompatActivity {


    ConnectionClass connectionClass;

    EditText txtPoruka;
    Integer idPosla,ID;
    Button posalji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_poruka_izvodjac);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Nova poruka");
        }

        Intent intent=getIntent();
        idPosla=intent.getIntExtra("ID_posla",0);
        ID=intent.getIntExtra("ID_izvodjaca", 0);

        posalji=findViewById(R.id.btnPosalji);
        txtPoruka=findViewById(R.id.txtTekstPoruke);

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unosNovePoruke();
                finish();
            }
        });

    }

    void unosNovePoruke(){
        connectionClass = new ConnectionClass();
        String poruka=txtPoruka.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        //Toast.makeText(getApplicationContext(), currentDateandTime.toString(), Toast.LENGTH_LONG).show();

        if(poruka.equals("")) {
            Toast.makeText(getApplicationContext(), "Niste napisali poruku!", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                Connection con = connectionClass.CONN();

                if (con == null){
                    Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Statement st = con.createStatement();

                    String queryReg = "INSERT INTO Komentar(Sadrzaj, Vrijeme, ID_posla) " +
                            "VALUES(('"+ poruka +"'), ('"+ currentDateandTime +"'), ('"+ idPosla +"'))";

                    //String queryReg = "INSERT INTO Komentar(Sadrzaj, Vrijeme, ID_posla) " +
                    //"VALUES(('Gotovi zidovi'), ('2020-01-03 15:14:13'), ('20')";


                    if (st.executeUpdate(queryReg) == 1){
                        Toast.makeText(getApplicationContext(), "Poruka poslana!", Toast.LENGTH_LONG).show();

                        txtPoruka.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Greška prilikom slanja poruke, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {return true;}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
                //intent.putExtra("ID_izvodjaca", ID);
                //this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
