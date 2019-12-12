package com.mgradnja;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


public class FragmentIzvodjacKalendar extends Fragment {


    CalendarView kalendar;
    String nazivIzvodjaca;

    public FragmentIzvodjacKalendar() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_kalendar, container, false);

        kalendar = view.findViewById(R.id.kalendar);

        Intent intent = getActivity().getIntent();
        nazivIzvodjaca = intent.getStringExtra("nazivIzvodjaca");




        return view;
    }


}
