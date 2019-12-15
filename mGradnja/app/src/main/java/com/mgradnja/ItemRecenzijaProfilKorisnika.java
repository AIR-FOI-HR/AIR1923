package com.mgradnja;

import java.util.Date;

public class ItemRecenzijaProfilKorisnika {

    private String izvodjac;
    private Date datum;
    private String komentar;
    private Integer ocjena;
    private String nazivUpita;

    public ItemRecenzijaProfilKorisnika (String izvodjac, Date datum, String komentar, Integer ocjena, String nazivUpita){
        this.izvodjac = izvodjac;
        this.datum = datum;
        this.komentar = komentar;
        this.ocjena = ocjena;
        this.nazivUpita = nazivUpita;
    }

    public String getIzvodjac(){
        return this.izvodjac;
    }

    public Date getDatum(){

        return this.datum;
    }

    public String getKomentar(){
        return this.komentar;
    }

    public Integer getOcjena(){
        return this.ocjena;
    }

    public String getNazivUpita(){
        return this.nazivUpita;
    }
}
