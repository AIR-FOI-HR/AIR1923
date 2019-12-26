package com.mgradnja;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class QueryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ConnectionClass connectionClass;
    Button potvrdi;
    ImageButton odaberiDatum;
    EditText txtNazivRadova, txtDatumRadova, txtOpisRadova,txtGrad,txtAdresa;
    public Spinner spinnerKategorije, spinnerZupanije;
    String datumRadova;
    Integer ID;
    int idKorisnika;
    public ArrayList<String> kategorije = new ArrayList<String>();
    public ArrayList<Integer> idKategorije=new ArrayList<Integer>();

    public String[] zupanije = new String[]{"Zagrebacka", "Krapinsko-zagorska", "Sisacko-moslavacka",
            "Karlovacka", "Varazdinska", "Koprivnicko-krizevacka", "Bjelovarsko-bilogorska", "Primorsko-goranska",
            "Licko-senjska", "Viroviticko-podravska", "Pozesko-slavnoska", "Brodsko-posavska", "Zadarska",
            "Osjecko-baranjska", "Sibensko-kninska", "Vukovarsko-srijemska", "Splitsko-dalmatinska", "Istarska",
            "Dubrovacko-neretvanska", "Medjimurska", "Grad Zagreb"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Intent intent=getIntent();
        ID=intent.getIntExtra("ID_korisnika", 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        connectionClass = new ConnectionClass();

        txtNazivRadova = findViewById(R.id.txtNazivRadova);
        txtOpisRadova = findViewById(R.id.txtOpisRadova);
        txtGrad=findViewById(R.id.txtGrad);
        txtAdresa=findViewById(R.id.txtAdresa);


        potvrdi=findViewById(R.id.btnPotvrdi);
        odaberiDatum=findViewById(R.id.btnDatum);

        spinnerKategorije = findViewById(R.id.spinKategorije);
        spinnerZupanije  = findViewById(R.id.spinZupanije);

        dohvatiKategorije();
        dohvatiIdKategorija();
        ArrayAdapter<String> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);
        ArrayAdapter<String> adapterZupanije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zupanije);

        spinnerKategorije.setAdapter(adapterKategorije);
        spinnerZupanije.setAdapter(adapterZupanije);

        odaberiDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewQuery();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        int godina=c.get(Calendar.YEAR);
        int mjesec=c.get(Calendar.MONTH)+1;
        int dan=c.get(Calendar.DAY_OF_MONTH);
        datumRadova=godina+"-"+mjesec+"-"+dan;
        String currentDate= DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        TextView datum=findViewById(R.id.txtDatumPocetkaRadova);
        datum.setText(currentDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, GlavniIzbornikKorisnik.class);
                intent.putExtra("ID_korisnika", ID);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dohvatiKategorije() {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select Naziv from Djelatnost";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                kategorije.add(rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void dohvatiIdKategorija() {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query2 = "select ID_djelatnosti from Djelatnost";
        try {
            Statement statement = con.createStatement();
            ResultSet as = statement.executeQuery(query2);

            while (as.next()) {
                idKategorije.add(as.getInt("ID_djelatnosti"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeNewQuery(){



        String naziv=txtNazivRadova.getText().toString();
        String opis=txtOpisRadova.getText().toString();
        String grad=txtGrad.getText().toString();
        String adresa=txtAdresa.getText().toString();
        String zupanija=spinnerZupanije.getSelectedItem().toString();
        int idDjelatnost=idKategorije.get(spinnerKategorije.getSelectedItemPosition());
        idKorisnika=ID;



        if (naziv.equals("") || datumRadova.equals("") || opis.equals("") || grad.equals("") || adresa.equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }


        else {
            try {
                Connection con = connectionClass.CONN();

                if (con == null){
                    Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Statement st = con.createStatement();

                    String queryReg = "INSERT INTO Upit(Naziv, Datum, Adresa, Grad, Zupanija, Opis, ID_korisnika, ID_djelatnosti) " +
                            "VALUES(('"+ naziv +"'), ('"+ datumRadova +"'), ('"+ adresa +"'), ('"+ grad +"'), ('"+ zupanija +"'), ('"+ opis +"'), ('"+ idKorisnika +"'), ('"+ idDjelatnost +"'))";



                    if (st.executeUpdate(queryReg) == 1){
                        Toast.makeText(getApplicationContext(), "Upit izrađen!", Toast.LENGTH_LONG).show();

                        txtNazivRadova.setText("");
                        txtGrad.setText("");
                        txtAdresa.setText("");
                        //txtDatumRadova.setText("");
                        txtOpisRadova.setText("");


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Greška prilikom izrade upita, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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


}
