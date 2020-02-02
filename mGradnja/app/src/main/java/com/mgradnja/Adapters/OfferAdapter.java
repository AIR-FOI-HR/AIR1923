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

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>{
    private ArrayList<JobAtributes> mListaPoslova;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class OfferViewHolder extends RecyclerView.ViewHolder {

        public TextView mNaziv;
        public TextView mOpis;
        public TextView mIzvodjac;
        public TextView mZavrsetak;
        public TextView mPocetak;
        public TextView mCijena;
        public ImageView mSlika;


        public OfferViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mCijena = itemView.findViewById(R.id.txtCijena);
            mOpis = itemView.findViewById(R.id.txtOpisPosla);
            mIzvodjac = itemView.findViewById(R.id.txtImeIzvodjaca);
            mPocetak = itemView.findViewById(R.id.txtPočetakPosla);
            mZavrsetak = itemView.findViewById(R.id.txtZavrsetskPosla);
            mNaziv = itemView.findViewById(R.id.textNazivUpita);
            mSlika = itemView.findViewById(R.id.Slika);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public OfferAdapter(ArrayList<JobAtributes> ListaPoslova){
        mListaPoslova = ListaPoslova;
    }
    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerlist_layout, parent, false);
        OfferAdapter.OfferViewHolder jvh = new OfferViewHolder(v, mListener);

        return jvh;
    }


    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.OfferViewHolder holder, int position) {

        int naziv = 0;
        JobAtributes JA = mListaPoslova.get(position);

        holder.mNaziv.setText(JA.getmNazivPosla());
        holder.mZavrsetak.setText("Završetak: " + JA.getMkrajPosla().toString());
        holder.mPocetak.setText(" Početak: " + JA.getmPočetakPosla().toString());
        holder.mCijena.setText(String.valueOf(JA.getmCijena())+ " Kn");
        holder.mOpis.setText(JA.getmOpisPosla());
        holder.mIzvodjac.setText(String.valueOf(JA.getmNazivIzvodjaca()));
        holder.mSlika.setImageResource(JA.getMbrojSlike());


    }



    @Override
    public int getItemCount() {
        return mListaPoslova.size();
    }

}
