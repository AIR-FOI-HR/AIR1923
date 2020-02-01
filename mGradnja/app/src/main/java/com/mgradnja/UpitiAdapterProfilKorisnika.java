package com.mgradnja;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.Adapters.OfferAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.OnLongClick;

public class UpitiAdapterProfilKorisnika extends RecyclerView.Adapter<UpitiAdapterProfilKorisnika.UpitiViewHolder> {

    private ArrayList<ItemUpitiProfilKorisnika> listaUpita;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void DeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class UpitiViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNaziv;
        public TextView txtDatum;
        public TextView txtOpis;
        public TextView txtAdresa;
        public TextView txtGrad;
        public TextView txtZupanija;
        public Button btnIzbrisi;


        public UpitiViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            txtNaziv = itemView.findViewById(R.id.txtUpitNaziv);
            txtDatum = itemView.findViewById(R.id.txtUpitDatum);
            txtOpis = itemView.findViewById(R.id.txtUpitOpis);
            txtAdresa = itemView.findViewById(R.id.txtUpitAdresa);
            txtGrad = itemView.findViewById(R.id.txtUpitGrad);
            txtZupanija = itemView.findViewById(R.id.txtUpitZupanija);
            btnIzbrisi = itemView.findViewById(R.id.btnIzbrišiUpit);

            btnIzbrisi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.DeleteItemClick(position);
                        }
                    }
                }
            });
        }




    }

    public UpitiAdapterProfilKorisnika(ArrayList<ItemUpitiProfilKorisnika> listaUpitaa) {
        this.listaUpita = listaUpitaa;
    }

    @NonNull
    @Override
    public UpitiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_upiti_profil_korisnika, parent, false);
        UpitiViewHolder uvh = new UpitiViewHolder(view, mListener);
        return  uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UpitiViewHolder holder, int position) {
        ItemUpitiProfilKorisnika trenutniUpit = listaUpita.get(position);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        String date = df.format(trenutniUpit.getDatum());

        holder.txtOpis.setText(trenutniUpit.getOpis());
        holder.txtDatum.setText(date);
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
