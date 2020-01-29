package com.mgradnja;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import butterknife.OnLongClick;


public class FragmentUpitiKorisnika extends Fragment {


    private View view;

    ConnectionClass connectionClass;
    Connection connection;

    public ArrayList<Integer> listaID = new ArrayList<Integer>();
    public ArrayList<String> listaNaziva = new ArrayList<String>();
    public ArrayList<Date> listaDatuma = new ArrayList<Date>();
    public ArrayList<String> listaOpisa = new ArrayList<String >();
    public ArrayList<String> listaAdresa = new ArrayList<String>();
    public ArrayList<String> listaGradova = new ArrayList<String>();
    public ArrayList<String> listaZupanija = new ArrayList<String>();

    private Integer ID;
    private String naziv, opis, adresa, grad ,zupanija;
    private Date datum;

    public ArrayList<ItemUpitiProfilKorisnika> listaUpita = new ArrayList<ItemUpitiProfilKorisnika>();
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private UpitiAdapterProfilKorisnika mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemUpitiProfilKorisnika UA;

    TextView txtPorukaUpiti;
    Button btnIzbrisiUpit;

    private Integer idUpita = 0;
    private Integer brojacUpita = 0;


    public FragmentUpitiKorisnika() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_upiti_korisnika, container, false);


        Intent intent = getActivity().getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        txtPorukaUpiti = (TextView) view.findViewById(R.id.txtPorukaUpiti);
        btnIzbrisiUpit = (Button) view.findViewById(R.id.btnIzbrišiUpit);

        dohvatUpita(ID);

        mRecyclerView = view.findViewById(R.id.upiti_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpitiAdapterProfilKorisnika(listaUpita);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new UpitiAdapterProfilKorisnika.OnItemClickListener() {
            @Override
            public void DeleteItemClick(int position) {
                izbrisiUpit(position);

            }
        });

        return view;

    }

    public void izbrisiUpit(int position){

        brojacUpita = 0;


        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("Jeste li sigurni da želite obrisani odabrani upit?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                idUpita = listaUpita.get(position).getID();

                connectionClass = new ConnectionClass();
                connection = connectionClass.CONN();


                String sql = "SELECT * FROM Ponuda WHERE ID_upita = ('" + idUpita + "')";

                try{
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);

                    while (rs.next()){

                        brojacUpita++;

                    }

                    if (brojacUpita==0){
                        String sqlBrisanje = "DELETE FROM Upit WHERE ID_upita= ('" + idUpita + "')";
                        ResultSet rsBrisanja = statement.executeQuery(sqlBrisanje);

                    }

                }
                catch (SQLException e){
                    e.printStackTrace();
                }

                if (brojacUpita == 0){
                    Toast.makeText(getContext() , "Uspješno izbrisan upit! " , Toast.LENGTH_LONG).show();

                    listaUpita.remove(position);
                    mAdapter.notifyItemRemoved(position);

                }
                else{
                    Toast.makeText(getContext() , "Ponuda je već kreirana, ne može se izbrisati upit!" , Toast.LENGTH_LONG).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }


    public void dohvatUpita(Integer ID){
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();

        String sql = "SELECT * FROM Upit WHERE ID_korisnika = ('" + ID + "')";

        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){

                listaID.add(rs.getInt("ID_upita"));
                listaNaziva.add(rs.getString("Naziv"));
                listaOpisa.add(rs.getString("Opis"));
                listaDatuma.add(rs.getDate("Datum"));
                listaAdresa.add(rs.getString("Adresa"));
                listaGradova.add(rs.getString("Grad"));
                listaZupanija.add(rs.getString("Zupanija"));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        listaUpita.clear();

        for(int i = 0; i<listaNaziva.size(); i++){

            ID = listaID.get(i);
            naziv = listaNaziva.get(i);
            datum = listaDatuma.get(i);
            opis = listaOpisa.get(i);
            adresa = listaAdresa.get(i);
            grad = listaGradova.get(i);
            zupanija = listaZupanija.get(i);

            UA = new ItemUpitiProfilKorisnika(ID, "Naziv: "+naziv, datum,"Opis: " + opis, "Adresa: " + adresa, "Grad: " + grad, "Županija: " + zupanija);
            listaUpita.add(UA);
        }
        listaID.clear();
        listaNaziva.clear();
        listaOpisa.clear();
        listaDatuma.clear();
        listaAdresa.clear();
        listaGradova.clear();
        listaZupanija.clear();

        if (listaUpita.isEmpty()){
            txtPorukaUpiti.setText("Nemate upita");
        }

        }

}
