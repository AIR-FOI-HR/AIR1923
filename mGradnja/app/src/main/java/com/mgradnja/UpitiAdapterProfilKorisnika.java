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


        public UpitiViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNaziv = itemView.findViewById(R.id.txtUpitNaziv);
            txtDatum = itemView.findViewById(R.id.txtUpitDatum);
            txtOpis = itemView.findViewById(R.id.txtUpitOpis);
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

    }

    @Override
    public int getItemCount() {
        return listaUpita.size();
    }
}
