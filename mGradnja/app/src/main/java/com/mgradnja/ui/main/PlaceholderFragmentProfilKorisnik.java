package com.mgradnja.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mgradnja.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentProfilKorisnik extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModelProfilKorisnik pageViewModel;

    public static PlaceholderFragmentProfilKorisnik newInstance(int index) {
        PlaceholderFragmentProfilKorisnik fragment = new PlaceholderFragmentProfilKorisnik();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModelProfilKorisnik.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = null;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)){
            case 1:
                root = inflater.inflate(R.layout.fragment_user_profile, container, false);
                break;

            case 2:
                root = inflater.inflate(R.layout.fragment_user_recenzije, container, false);
                break;

            case 3:
                root = inflater.inflate(R.layout.fragment_user_upiti, container, false);
        }
        return root;
    }

}