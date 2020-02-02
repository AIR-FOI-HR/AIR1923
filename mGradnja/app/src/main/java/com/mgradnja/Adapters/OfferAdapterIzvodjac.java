package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgradnja.HelpEntities.JobAtributes;
import com.mgradnja.R;

import java.util.ArrayList;

public class OfferAdapterIzvodjac extends RecyclerView.Adapter<OfferAdapterIzvodjac.JobViewHolder> {

    private ArrayList<JobAtributes> mListaPoslova;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnDeleteClick(int position);
        void OnUpdateClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public TextView mNaziv;
        public TextView mOpis;
        public TextView mIme;
        public TextView mPrezime;
        public TextView mZavrsetak;
        public TextView mPocetak;
        public TextView mCijena;
        public ImageView mSlika;
        public Button  mObrisi;
        public Button mUredi;


        public JobViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mCijena = itemView.findViewById(R.id.txtCijena);
            mOpis = itemView.findViewById(R.id.txtOpisPosla);
            mIme = itemView.findViewById(R.id.textImeKorisnika);
            mPrezime = itemView.findViewById(R.id.textPrezimeKorisnika);
            mPocetak = itemView.findViewById(R.id.txtPočetakPosla);
            mZavrsetak = itemView.findViewById(R.id.txtZavrsetskPosla);
            mNaziv = itemView.findViewById(R.id.txtNazivUpita);
            mSlika = itemView.findViewById(R.id.Slika);
            mObrisi = itemView.findViewById(R.id.btnObrisi);
            mUredi = itemView.findViewById(R.id.btnUredi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnItemClick(position);

                    }
                }
            });
            mObrisi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnDeleteClick(position);

                    }
                }
            });
            mUredi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.OnUpdateClick(position);

                    }
                }
            });

            }
    }

    public OfferAdapterIzvodjac(ArrayList<JobAtributes> ListaPoslova){
        mListaPoslova = ListaPoslova;
    }
    @NonNull
    @Override
    public OfferAdapterIzvodjac.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerlist_izvodjac_layout, parent, false);
        OfferAdapterIzvodjac.JobViewHolder jvh = new OfferAdapterIzvodjac.JobViewHolder(v, mListener);

        return jvh;
    }


    @Override
    public void onBindViewHolder(@NonNull OfferAdapterIzvodjac.JobViewHolder holder, int position) {

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
