package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.DatePickerFragment;
import com.mgradnja.GlavniIzbornikKorisnik;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateOfferActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ConnectionClass connectionClass;
    public String NazivUpita;
    public Integer ID_Izvodjaca;
    public Integer ID_Upita;
    public  Integer Broj_Dana;
    public long BrDana;
    public String Opis;
    public String Datum;
    public String BrojDanaString;


    public Date DatumPocetkaR;
    public Date DatumKrajaR;
    public float Cijena;
    public int Status;
    public String RadoviPocetak;
    public String RadoviKraj;

    private TextView Naziv;
    private TextView Cena;
    private TextView Ooopis;
    ImageButton DatumPocetni;
    Button SpremiPromjene;
    public TextView DatumPocetkaRadova;
    public TextView BrojDanatxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Uredi ponudu");
        }

        SpremiPromjene = findViewById(R.id.btnSpremiPromjene);
        Cena = findViewById(R.id.txtCijenaPonude);
        Ooopis = findViewById(R.id.txtOpisRadova);
        Intent intent = getIntent();

        ID_Izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);
        ID_Upita = intent.getIntExtra("ID_upita", 0);
        Cijena = intent.getFloatExtra("Cijena", 0);
        NazivUpita = intent.getStringExtra("Naziv");
        Opis = intent.getStringExtra("Opis");
        DatumPocetkaR = (Date) intent.getSerializableExtra("Početak");
        DatumKrajaR = (Date) intent.getSerializableExtra("Kraj");

        Naziv = findViewById(R.id.txtNazivUpita);
        Naziv.setText(NazivUpita);
        Cena.setText(String.valueOf(Cijena));
        Ooopis.setText(Opis);

        DatumPocetni = findViewById(R.id.btnDatumPocetka);
        BrojDanatxt = findViewById(R.id.txtBrojDana);
        DatumPocetkaRadova = findViewById(R.id.txtDatumPocetkaRadova);
        DatumPocetkaRadova.setText(DatumPocetkaR.toString());

        IzracunajBrojDana(DatumPocetkaR, DatumKrajaR);


        connectionClass = new ConnectionClass();
        DohvatiDatuum();
        DatumPocetni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        SpremiPromjene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOffer();
                Intent intent1 = new Intent(getApplicationContext(), GlavniIzbornikIzvodjac.class);
                intent1.putExtra("ID_izvodjaca", ID_Izvodjaca);
                startActivity(intent1);
                finish();
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
        RadoviPocetak=godina+"-"+mjesec+"-"+dan;
        String currentDate= DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        TextView datum=findViewById(R.id.txtDatumPocetkaRadova);
        datum.setText(currentDate);

    }


    public void IzracunajBrojDana(Date Pocetak, Date Kraj)
    {
        long razlika;
        razlika =Pocetak.getTime()-Kraj.getTime();

        long mili = 1000;
        long minute = mili * 60;
        long sati = minute * 60;
        BrDana = sati * 24;
        long ProsloDana = razlika / BrDana;



        Broj_Dana = (int) ProsloDana;
        Broj_Dana = Broj_Dana*(-1);
        BrojDanatxt = findViewById(R.id.txtBrojDana);
        BrojDanatxt.setText(Broj_Dana.toString());
    }
    public void IzracunajDrugiDatum(String RadoviPočetak, int BrDana){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(dateFormat.parse(RadoviPočetak));

        }
        catch (ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DAY_OF_YEAR, BrDana);
        DatumKrajaR = new Date(c.getTimeInMillis());
        RadoviKraj = dateFormat.format(DatumKrajaR);
    }
    public void DohvatiDatuum(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select Convert(DATE,GETDATE()) as 'Datum'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                Datum = rs.getDate("Datum").toString();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public   boolean ProvjeriDatume(String d1, String d2){
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            b = dfDate.parse(d1).before(dfDate.parse(d2)) || dfDate.parse(d1).equals(dfDate.parse(d2));
        }
        catch (ParseException e){
            Toast.makeText(getApplicationContext(), "Krivi unos datuma!", Toast.LENGTH_SHORT).show();
        }
        if(!b) Toast.makeText(getApplicationContext(), "Krivi unos datuma!", Toast.LENGTH_SHORT).show();
        return b;
    }
    public void UpdateOffer(){
        BrojDanaString = BrojDanatxt.getText().toString();
        Broj_Dana = Integer.valueOf(BrojDanaString);
        if(RadoviPocetak == null) RadoviPocetak = DatumPocetkaR.toString();
        IzracunajDrugiDatum(RadoviPocetak, Broj_Dana);
        Connection con = connectionClass.CONN();

        Status = 0;
        Opis = Ooopis.getText().toString();
        if(Cena.getTextSize()==0) Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();



        if (Opis.equals("")  || Cena.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {

                Cijena = Float.valueOf(Cena.getText().toString());
                if (con == null){
                    Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Statement st = con.createStatement();
                    if(RadoviPocetak == null || RadoviKraj == null) {
                        RadoviPocetak = DatumPocetkaR.toString();
                        RadoviKraj = DatumKrajaR.toString();
                    }
                    String queryReg = "Update Ponuda set Opis = '" +Opis +"', Cijena = '"+Cijena+"',  Datum_pocetka = '"+ RadoviPocetak +"', Datum_zavrsetka = '"+ RadoviKraj +"' where ID_upita = '"+ID_Upita+"' and ID_izvodjaca = '"+ID_Izvodjaca+"'";


                    if (st.executeUpdate(queryReg) == 1){
                        Toast.makeText(getApplicationContext(), "Ponuda ažurirana!", Toast.LENGTH_LONG).show();

                        Cena.setText("");
                        Ooopis.setText("");
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Greška prilikom uređivanja ponude, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
