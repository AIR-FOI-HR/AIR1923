package com.mgradnja.Izvodjac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.DatePickerFragment;
import com.mgradnja.R;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OfferActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ConnectionClass connectionClass;
    public String NazivUpita;
    public Integer ID_Izvodjaca;
    public Integer ID_Upita;
    public Integer BrojDana;
    public String BrojDanaString;
    public String Opis;
    public String Datum;
    public Date DatumPocetka;
    public Date DatumKrajaR;
    public float Cijena;
    public int Status;
    public String RadoviPocetak;
    public String RadoviKraj;

    private TextView Naziv;
    private TextView Cena;
    private TextView Ooopis;
    public TextView BrojDanatxt;
    ImageButton DatumPocetni;
    Button Potvrdi;
    public TextView tv;
    public  TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Intent intent = getIntent();
        ID_Izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);
        ID_Upita = intent.getIntExtra("ID_upita", 0);


        Naziv = findViewById(R.id.txtNazivUpita);
        DatumPocetni = findViewById(R.id.btnDatumPocetka);
        Potvrdi = findViewById(R.id.btnSpremiPromjene);
        Cena = findViewById(R.id.txtCijenaPonude);
        Ooopis = findViewById(R.id.txtOpisRadova);
        connectionClass = new ConnectionClass();
        DohvatiNazivUpita();
        DohvatiDatuum();
        DatumPocetni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });


        Potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeNewOffer();
            }
        });

        BrojDanatxt = findViewById(R.id.txtBrojDana);


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
    public void MakeNewOffer(){
        BrojDanaString = BrojDanatxt.getText().toString();
        BrojDana = Integer.valueOf(BrojDanaString);
        IzracunajDrugiDatum(RadoviPocetak, BrojDana);
        Connection con = connectionClass.CONN();

        Status = 0;
        Opis = Ooopis.getText().toString();
        if(Cena.getTextSize()==0) Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();



        if (Opis.equals("") || RadoviPocetak.equals("") || RadoviKraj.equals("") || Cena.getText().toString().equals("")){
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

                   String queryReg = "INSERT INTO Ponuda (ID_upita, ID_izvodjaca, Datum, Status, Opis, Cijena, Procitana, Datum_pocetka, Datum_zavrsetka) VALUES(('"+ ID_Upita +"'), ('"+ ID_Izvodjaca +"'),  ('"+ Datum +"'), ('"+ Status +"'), ('"+ Opis +"'), ('"+ Cijena +"'), ('"+ Status +"'), ('"+ RadoviPocetak +"'), ('"+ RadoviKraj +"'))";


                    if (st.executeUpdate(queryReg) == 1){
                        Toast.makeText(getApplicationContext(), "Ponuda izrađena!", Toast.LENGTH_LONG).show();

                        Cena.setText("");
                        Ooopis.setText("");

                        Intent inte = new Intent(OfferActivity.this, GlavniIzbornikIzvodjac.class);
                        inte.putExtra("ID_izvodjaca", ID_Izvodjaca);
                        startActivity(inte);
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
    public void DohvatiNazivUpita(){

        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "select Naziv from Upit where ID_upita  = '" + ID_Upita + "'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                NazivUpita = rs.getString("Naziv");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Naziv.setText(NazivUpita);
    }

}
