package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.Korisnik.ItemRecenzijaProfilKorisnika;
import com.mgradnja.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecenzijeAdapterProfilKorisnika extends RecyclerView.Adapter<RecenzijeAdapterProfilKorisnika.RecenzijeViewHolder> {

    private ArrayList<ItemRecenzijaProfilKorisnika> listaRecentija;

    public static class RecenzijeViewHolder extends RecyclerView.ViewHolder{

        public TextView txtIzvodjacRecenzije;
        public TextView txtDatumRecenzije;
        public TextView txtKomentarRecenzije;
        public RatingBar recenzijaProfilKorisnika;


        public RecenzijeViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIzvodjacRecenzije = itemView.findViewById(R.id.txtProfilKorisnikaNazivIzvodjaca);
            txtDatumRecenzije = itemView.findViewById(R.id.txtDatumRecenzijeProfilKorisnika);
            txtKomentarRecenzije = itemView.findViewById(R.id.txtKomentarRecenzijeProfilKorisnika);
            recenzijaProfilKorisnika = itemView.findViewById(R.id.ocjenaRecenzijeProfilKorisnika);
        }
    }

    public RecenzijeAdapterProfilKorisnika(ArrayList<ItemRecenzijaProfilKorisnika> listaRecentija){
        this.listaRecentija = listaRecentija;
    }

    @NonNull
    @Override
    public RecenzijeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recenzije_profil_korisnika, parent, false);
        RecenzijeViewHolder rvh = new RecenzijeViewHolder(view);
        return  rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecenzijeViewHolder holder, int position) {
        ItemRecenzijaProfilKorisnika trenutnaRecenzija = listaRecentija.get(position);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        String date = df.format(trenutnaRecenzija.getDatum());

        holder.txtIzvodjacRecenzije.setText(trenutnaRecenzija.getIzvodjac());
        holder.txtKomentarRecenzije.setText(trenutnaRecenzija.getKomentar());
        holder.txtDatumRecenzije.setText(date);
        holder.recenzijaProfilKorisnika.setRating(trenutnaRecenzija.getOcjena());
    }

    @Override
    public int getItemCount() {
        return listaRecentija.size();
    }
}
