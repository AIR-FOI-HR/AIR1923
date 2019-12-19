package com.mgradnja;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FragmentIzvodjacKalendar extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    String nazivIzvodjaca;

    CalendarPickerView calendar;
    public ArrayList<Date> datumi = new ArrayList<>();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

    public FragmentIzvodjacKalendar() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_kalendar, container, false);

        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = view.findViewById(R.id.kalendarIzvodjaca);
        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date());

        dohvatiDatumePoslova(nazivIzvodjaca);
        calendar.highlightDates(datumi);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + "." + (calSelected.get(Calendar.MONTH) + 1)
                        + "." + calSelected.get(Calendar.YEAR) + ".";

                Toast.makeText(getContext(), selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        return view;
    }

    private void dohvatiDatumePoslova(String nazivIzvodjaca){
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        Integer idIzvodjaca = null;

        try {
            String sql = "SELECT ID_izvodjaca from Izvodjac WHERE Naziv=('" + nazivIzvodjaca + "')";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                idIzvodjaca = rs.getInt("ID_izvodjaca");
            }

            String sql2 = "SELECT Ponuda.Datum_pocetka AS dp, Ponuda.Datum_zavrsetka AS dz FROM Izvodjac, Posao, Ponuda WHERE Posao.ID_izvodjaca = Izvodjac.ID_izvodjaca AND Izvodjac.ID_izvodjaca = Ponuda.ID_izvodjaca AND Posao.ID_upita = Ponuda.ID_upita AND Izvodjac.ID_izvodjaca=('" + idIzvodjaca + "')";
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(sql2);
            while (rs2.next()){
                try {
                    //datumi.add(dateformat.parse( dateformat.format(rs2.getDate("dp"))));
                    //datumi.add(dateformat.parse( dateformat.format(rs2.getDate("dz"))));
                    Date startDate = dateformat.parse( dateformat.format(rs2.getDate("dp")));
                    Date endDate = dateformat.parse( dateformat.format(rs2.getDate("dz")));

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


}
