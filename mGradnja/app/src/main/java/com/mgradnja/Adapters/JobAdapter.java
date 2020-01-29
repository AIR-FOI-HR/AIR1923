package com.mgradnja.Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.R;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private  ArrayList<JobAtributes>  mListaPoslova;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnlinePayClick(int position);
        void OnCashClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public TextView mNaziv;
        public TextView mOpis;
        public TextView mIzvodjac;
        public TextView mZavrsetak;
        public TextView mPocetak;
        public TextView mCijena;
        public ImageView mSlika;
        public Button mCash;
        public Button mCard;


        public JobViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mCijena = itemView.findViewById(R.id.txtCijena);
            mOpis = itemView.findViewById(R.id.txtOpisPosla);
            mIzvodjac = itemView.findViewById(R.id.textImeIzvodjaca);
            mPocetak = itemView.findViewById(R.id.txtPočetakPosla);
            mZavrsetak = itemView.findViewById(R.id.txtZavrsetskPosla);
            mNaziv = itemView.findViewById(R.id.txtNazivUpita);
            mSlika = itemView.findViewById(R.id.Slika);
            mCash = itemView.findViewById(R.id.btnGotovina);
            mCard = itemView.findViewById(R.id.btnOnline);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnItemClick(position);


                    }
                }
            });

            mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnlinePayClick(position);


                    }
                }
            });

            mCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnCashClick(position);


                    }
                }
            });


        }

    }


    public JobAdapter(ArrayList<JobAtributes> ListaPoslova){
        mListaPoslova = ListaPoslova;
    }
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_layout, parent, false);
        JobViewHolder jvh = new JobViewHolder(v, mListener);

        return jvh;
    }


    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        int naziv = 0;
        JobAtributes JA = mListaPoslova.get(position);

        holder.mNaziv.setText(JA.getmNazivPosla());
        holder.mZavrsetak.setText("Završetak: " + JA.getMkrajPosla().toString());
        holder.mPocetak.setText(" Početak: " + JA.getmPočetakPosla().toString());
        holder.mCijena.setText(String.valueOf(JA.getmCijena())+ " Kn");
        holder.mOpis.setText(JA.getmOpisPosla());
        holder.mIzvodjac.setText(String.valueOf("Izvođač: "+JA.getmNazivIzvodjaca()));
        holder.mSlika.setImageResource(JA.getMbrojSlike());
        if(JA.getmPlaceno() == 1) {
            holder.mCash.setVisibility(View.GONE);
            holder.mCard.setVisibility(View.GONE);
        }



    }



    @Override
    public int getItemCount() {
        return mListaPoslova.size();
    }


}
