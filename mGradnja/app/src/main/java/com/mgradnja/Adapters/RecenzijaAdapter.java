package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.R;
import com.mgradnja.HelpEntities.RecenzijaEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecenzijaAdapter extends RecyclerView.Adapter<RecenzijaAdapter.RecenzijaViewHolder> {

    private ArrayList<RecenzijaEntity> listaRecenzija;

    public class RecenzijaViewHolder extends RecyclerView.ViewHolder {

        public TextView imeRecenzenta;
        public TextView prezimeRecenzenta;
        public TextView komentar;
        public RatingBar recenzija;
        public TextView datum;

        public RecenzijaViewHolder(@NonNull View itemView) {
            super(itemView);
            imeRecenzenta = itemView.findViewById(R.id.txtImeRecenzenta);
            prezimeRecenzenta = itemView.findViewById(R.id.txtPrezimeRecenzenta);
            komentar = itemView.findViewById(R.id.txtKomentarRecenzenta);
            recenzija = itemView.findViewById(R.id.ocjenaRecenzenta);
            datum = itemView.findViewById(R.id.txtDatumRecenzije);
        }
    }

    public RecenzijaAdapter(ArrayList<RecenzijaEntity> listaRecenzija){
        this.listaRecenzija = listaRecenzija;
    }

    @NonNull
    @Override
    public RecenzijaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recenzije_layout, parent, false);
        RecenzijaViewHolder rvh = new RecenzijaViewHolder(view);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecenzijaViewHolder holder, int position) {

        RecenzijaEntity recenzijaEntity = listaRecenzija.get(position);

        holder.imeRecenzenta.setText(recenzijaEntity.getImeKorisnika());
        holder.prezimeRecenzenta.setText(recenzijaEntity.getPrezimeKorisnika());
        holder.recenzija.setRating(recenzijaEntity.getOcjena());
        holder.komentar.setText(recenzijaEntity.getKomentar());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        holder.datum.setText(sdf.format(recenzijaEntity.getDatum()));
    }

    @Override
    public int getItemCount() {
        return listaRecenzija.size();
    }
}
