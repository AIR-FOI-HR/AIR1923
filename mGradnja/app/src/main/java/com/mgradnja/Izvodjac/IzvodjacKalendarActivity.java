package com.mgradnja.Izvodjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgradnja.ConnectionClass;
import com.mgradnja.Izvodjac.GlavniIzbornikIzvodjac;
import com.mgradnja.R;
import com.squareup.timessquare.CalendarPickerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IzvodjacKalendarActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    Connection con;
    public int ID_izvodjaca;
    CalendarPickerView calendar;
    public ArrayList<Date> datumi = new ArrayList<>();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izvodjac_kalendar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        ID_izvodjaca = intent.getIntExtra("ID_izvodjaca", 0);

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = findViewById(R.id.izvodjacevKalendar);
        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date());

        dohvatiDatumePoslova(ID_izvodjaca);
        calendar.highlightDates(datumi);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + "." + (calSelected.get(Calendar.MONTH) + 1)
                        + "." + calSelected.get(Calendar.YEAR) + ".";

                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

    }


    private void dohvatiDatumePoslova(int idIzvodjaca){
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        try {
            String sql = "SELECT Ponuda.Datum_pocetka AS dp, Ponuda.Datum_zavrsetka AS dz FROM Izvodjac, Posao, Ponuda WHERE Posao.ID_izvodjaca = Izvodjac.ID_izvodjaca AND Izvodjac.ID_izvodjaca = Ponuda.ID_izvodjaca AND Posao.ID_upita = Ponuda.ID_upita AND Izvodjac.ID_izvodjaca=('" + idIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                try {
                    Date startDate = dateformat.parse( dateformat.format(rs.getDate("dp")));
                    Date endDate = dateformat.parse( dateformat.format(rs.getDate("dz")));

                    long startTime = startDate.getTime();
                    long endTime = endDate.getTime();
                    long interval = 24 * 1000 * 60 * 60;

                    while (startTime <= endTime) {
                        datumi.add(new Date(startTime));
                        startTime += interval;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
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
                Intent intent = new Intent(this, GlavniIzbornikIzvodjac.class);
                intent.putExtra("ID_izvodjaca", ID_izvodjaca);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
