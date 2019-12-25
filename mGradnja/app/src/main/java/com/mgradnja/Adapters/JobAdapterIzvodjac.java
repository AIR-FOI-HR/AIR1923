package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.R;

import java.util.ArrayList;

public class JobAdapterIzvodjac extends RecyclerView.Adapter<JobAdapterIzvodjac.JobViewHolder> {

    private  ArrayList<JobAtributes>  mListaPoslova;
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public TextView mNaziv;
        public TextView mOpis;
        public TextView mIme;
        public TextView mPrezime;
        public TextView mZavrsetak;
        public TextView mPocetak;
        public TextView mCijena;
        public ImageView mSlika;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            mCijena = itemView.findViewById(R.id.txtCijena);
            mOpis = itemView.findViewById(R.id.txtOpisPosla);
            mIme = itemView.findViewById(R.id.textImeKorisnika);
            mPrezime = itemView.findViewById(R.id.textPrezimeKorisnika);
            mPocetak = itemView.findViewById(R.id.txtPočetakPosla);
            mZavrsetak = itemView.findViewById(R.id.txtZavrsetskPosla);
            mNaziv = itemView.findViewById(R.id.txtNazivUpita);
            mSlika = itemView.findViewById(R.id.Slika);
        }
    }

    public JobAdapterIzvodjac(ArrayList<JobAtributes> ListaPoslova){
        mListaPoslova = ListaPoslova;
    }
    @NonNull
    @Override
    public JobAdapterIzvodjac.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_izvodjac_layout, parent, false);
        JobAdapterIzvodjac.JobViewHolder jvh = new JobAdapterIzvodjac.JobViewHolder(v);

        return jvh;
    }


    @Override
    public void onBindViewHolder(@NonNull JobAdapterIzvodjac.JobViewHolder holder, int position) {

        int naziv = 0;
        JobAtributes JA = mListaPoslova.get(position);

        holder.mNaziv.setText(JA.getmNazivPosla());
        holder.mZavrsetak.setText("Završetak: " + JA.getMkrajPosla().toString());
        holder.mPocetak.setText(" Početak: " + JA.getmPočetakPosla().toString());
        holder.mCijena.setText(String.valueOf(JA.getmCijena())+ " Kn");
        holder.mOpis.setText(JA.getmOpisPosla());
        holder.mIme.setText(String.valueOf(JA.getmIme()));
        holder.mPrezime.setText(String.valueOf(JA.getmPrezime()));
        holder.mSlika.setImageResource(JA.getMbrojSlike());


    }



    @Override
    public int getItemCount() {
        return mListaPoslova.size();
    }
}
