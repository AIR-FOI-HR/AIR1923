package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.R;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private  ArrayList<JobAtributes>  mListaPoslova;
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public TextView mNaziv;
        public TextView mOpis;
        public TextView mIzvodjac;
        public TextView mZavrsetak;
        public TextView mPocetak;
        public TextView mCijena;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            mCijena = itemView.findViewById(R.id.txtCijena);
            mOpis = itemView.findViewById(R.id.txtOpisPosla);
            mIzvodjac = itemView.findViewById(R.id.textImeIzvodjaca);
            mPocetak = itemView.findViewById(R.id.txtPočetakPosla);
            mZavrsetak = itemView.findViewById(R.id.txtZavrsetskPosla);
            mNaziv = itemView.findViewById(R.id.txtNazivUpita);
        }
    }

    public JobAdapter(ArrayList<JobAtributes> ListaPoslova){
        mListaPoslova = ListaPoslova;
    }
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_layout, parent, false);
        JobViewHolder jvh = new JobViewHolder(v);

        return jvh;
    }


    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        JobAtributes JA = mListaPoslova.get(position);

        holder.mNaziv.setText(JA.getmNazivPosla());
        holder.mZavrsetak.setText("Završetak: " + JA.getMkrajPosla().toString());
        holder.mPocetak.setText(" Početak: " + JA.getmPočetakPosla().toString());
        holder.mCijena.setText(String.valueOf(JA.getmCijena())+ " Kn");
        holder.mOpis.setText(JA.getmOpisPosla());
        holder.mIzvodjac.setText(String.valueOf("Izvođač: "+JA.getmNazivIzvodjaca()));
    }



    @Override
    public int getItemCount() {
        return mListaPoslova.size();
    }


}
