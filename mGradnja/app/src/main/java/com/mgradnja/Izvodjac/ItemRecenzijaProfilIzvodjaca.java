package com.mgradnja.Izvodjac;

import java.util.Date;

public class ItemRecenzijaProfilIzvodjaca {

    private String imeKorisnika, prezimeKorisnika;
    private Date datum;
    private String komentar;
    private Integer ocjena;

    public ItemRecenzijaProfilIzvodjaca (String imeKorisnika, String prezimeKorisnika, Date datum, String komentar, Integer ocjena){
        this.imeKorisnika = imeKorisnika;
        this.prezimeKorisnika = prezimeKorisnika;
        this.datum = datum;
        this.komentar = komentar;
        this.ocjena = ocjena;
    }

    public String getImeKorisnika(){
        return this.imeKorisnika;
    }

    public String getPrezimeKorisnika(){
        return this.prezimeKorisnika;
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
}
