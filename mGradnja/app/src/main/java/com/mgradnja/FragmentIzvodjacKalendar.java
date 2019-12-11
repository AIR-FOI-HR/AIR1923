package com.mgradnja;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


public class FragmentIzvodjacKalendar extends Fragment {


    CalendarView kalendar;

    public FragmentIzvodjacKalendar() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_kalendar, container, false);

        kalendar = view.findViewById(R.id.kalendar);





        return view;
    }

}
