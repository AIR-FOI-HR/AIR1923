package com.mgradnja;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpitiAdapterProfilKorisnika extends RecyclerView.Adapter<UpitiAdapterProfilKorisnika.UpitiViewHolder> {

    private ArrayList<ItemUpitiProfilKorisnika> listaUpita;

    public static class UpitiViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNaziv;
        public TextView txtDatum;
        public TextView txtOpis;
        public TextView txtAdresa;
        public TextView txtGrad;
        public TextView txtZupanija;


        public UpitiViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNaziv = itemView.findViewById(R.id.txtUpitNaziv);
            txtDatum = itemView.findViewById(R.id.txtUpitDatum);
            txtOpis = itemView.findViewById(R.id.txtUpitOpis);
            txtAdresa = itemView.findViewById(R.id.txtUpitAdresa);
            txtGrad = itemView.findViewById(R.id.txtUpitGrad);
            txtZupanija = itemView.findViewById(R.id.txtUpitZupanija);
        }
    }

    public UpitiAdapterProfilKorisnika(ArrayList<ItemUpitiProfilKorisnika> listaUpitaa) {
        this.listaUpita = listaUpitaa;
    }

    @NonNull
    @Override
    public UpitiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_upiti_profil_korisnika, parent, false);
        UpitiViewHolder uvh = new UpitiViewHolder(view);
        return  uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UpitiViewHolder holder, int position) {
        ItemUpitiProfilKorisnika trenutniUpit = listaUpita.get(position);

        holder.txtOpis.setText(trenutniUpit.getOpis());
        holder.txtDatum.setText(trenutniUpit.getDatum()+"");
        holder.txtNaziv.setText(trenutniUpit.getNaziv());
        holder.txtAdresa.setText(trenutniUpit.getAdresa());
        holder.txtGrad.setText(trenutniUpit.getGrad());
        holder.txtZupanija.setText(trenutniUpit.getZupanija());

    }

    @Override
    public int getItemCount() {
        return listaUpita.size();
    }
}
