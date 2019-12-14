package com.mgradnja;

import java.util.Date;

public class RecenzijaEntity {

    private String imeKorisnika;
    private String prezimeKorisnika;
    private String komentar;
    private Date datum;
    private Integer ocjena;

    public RecenzijaEntity(String imeKorisnika, String prezimeKorisnika, String komentar, Date datum, Integer ocjena){
        this.imeKorisnika = imeKorisnika;
        this.prezimeKorisnika = prezimeKorisnika;
        this.komentar = komentar;
        this.datum = datum;
        this.ocjena = ocjena;
    }

    public String getImeKorisnika(){return this.imeKorisnika;}
    public String getPrezimeKorisnika(){return this.prezimeKorisnika;}
    public String getKomentar(){return this.komentar;}
    public Date getDatum(){return this.datum;}
    public Integer getOcjena(){return this.ocjena;}
}
