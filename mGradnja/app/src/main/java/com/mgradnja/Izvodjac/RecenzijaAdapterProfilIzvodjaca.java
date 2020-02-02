package com.mgradnja.Izvodjac;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecenzijaAdapterProfilIzvodjaca extends RecyclerView.Adapter<RecenzijaAdapterProfilIzvodjaca.RecenzijeIzvodjacaViewHolder>{

    private ArrayList<ItemRecenzijaProfilIzvodjaca> listaRecenzijaIzvodjaca;


    public static class RecenzijeIzvodjacaViewHolder extends RecyclerView.ViewHolder{

        public TextView txtImeRecenzijaIzvodjaca, txtPrezimeRecenzijaIzvodjaca;
        public TextView txtDatumRecenzijeIzvodjac;
        public TextView txtKomentarRecenzijeIzvodjac;
        public RatingBar recenzijaProfilIzvodjaca;


        public RecenzijeIzvodjacaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtImeRecenzijaIzvodjaca = itemView.findViewById(R.id.txtProfilIzvodjacaImeRecenzija);
            txtPrezimeRecenzijaIzvodjaca = itemView.findViewById(R.id.txtProfilIzvodjacaPrezimeRecenzija);
            txtDatumRecenzijeIzvodjac = itemView.findViewById(R.id.txtDatumRecenzijeProfilIzvodjaca);
            txtKomentarRecenzijeIzvodjac = itemView.findViewById(R.id.txtKomentarProfilIzvodjacaRecenzija);
            recenzijaProfilIzvodjaca = itemView.findViewById(R.id.ocjenaProfilIzvodjacaRecenzija);

        }
    }

    public RecenzijaAdapterProfilIzvodjaca(ArrayList<ItemRecenzijaProfilIzvodjaca> listaRecenzijaIzvodjaca){
        this.listaRecenzijaIzvodjaca = listaRecenzijaIzvodjaca;
    }

    @NonNull
    @Override
    public RecenzijeIzvodjacaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recenzija_profil_izvodjaca, parent, false);
        RecenzijeIzvodjacaViewHolder rvh = new RecenzijeIzvodjacaViewHolder(view);
        return  rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecenzijeIzvodjacaViewHolder holder, int position) {
        ItemRecenzijaProfilIzvodjaca trenutnaRecenzija = listaRecenzijaIzvodjaca.get(position);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        String date = df.format(trenutnaRecenzija.getDatum());

        holder.txtImeRecenzijaIzvodjaca.setText(trenutnaRecenzija.getImeKorisnika());
        holder.txtPrezimeRecenzijaIzvodjaca.setText(trenutnaRecenzija.getPrezimeKorisnika());
        holder.txtKomentarRecenzijeIzvodjac.setText(trenutnaRecenzija.getKomentar());
        holder.txtDatumRecenzijeIzvodjac.setText(date);
        holder.recenzijaProfilIzvodjaca.setRating(trenutnaRecenzija.getOcjena());

    }

    @Override
    public int getItemCount() {
        return listaRecenzijaIzvodjaca.size();
    }
}
