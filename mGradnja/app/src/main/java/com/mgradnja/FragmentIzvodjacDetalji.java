package com.mgradnja;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentIzvodjacDetalji extends Fragment {


    public FragmentIzvodjacDetalji() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_izvodjac_detalji, container, false);

        return view;
    }

}
