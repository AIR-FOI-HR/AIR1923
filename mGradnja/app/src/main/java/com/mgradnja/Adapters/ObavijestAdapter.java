package com.mgradnja.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgradnja.HelpEntities.ObavijestAtributes;
import com.mgradnja.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ObavijestAdapter extends RecyclerView.Adapter<ObavijestAdapter.ObavijestViewHolder> {

    private ArrayList<ObavijestAtributes> mObavijestLista;

    public static class ObavijestViewHolder extends RecyclerView.ViewHolder {

        public TextView ObavijestIzvodjac;
        public TextView ObavijestObavijest;
        public TextView ObavjestVrijeme;

        public ObavijestViewHolder(@NonNull View itemView) {
            super(itemView);
            ObavijestIzvodjac=itemView.findViewById(R.id.txtObavijestImeIzvodjac);
            ObavijestObavijest=itemView.findViewById(R.id.txtSadrzajPoruke);
            ObavjestVrijeme=itemView.findViewById(R.id.txtVrijemePoruke);
        }
    }

    public ObavijestAdapter(ArrayList<ObavijestAtributes> obavijestLista) {
        mObavijestLista=obavijestLista;
    }

    @NonNull
    @Override
    public ObavijestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.obavijest_layout, parent, false);
        ObavijestViewHolder ovh=new ObavijestViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull ObavijestViewHolder holder, int position) {
        ObavijestAtributes trenutnaObavijest = mObavijestLista.get(position);

        holder.ObavijestIzvodjac.setText(trenutnaObavijest.getIzvodjaca());
        holder.ObavijestObavijest.setText(trenutnaObavijest.getObavijest());
        holder.ObavjestVrijeme.setText(trenutnaObavijest.getVrijeme());
    }

    @Override
    public int getItemCount() {
        return mObavijestLista.size();
    }
}
