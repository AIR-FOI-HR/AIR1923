package com.mgradnja;

import java.sql.Date;

public class ItemUpitiProfilKorisnika {

    private String naziv;
    private Date datum;
    //private String datum;
    private String opis;

    public ItemUpitiProfilKorisnika(String naziv, Date datum, String opis){
        this.naziv = naziv;
        this.datum = datum;
        this.opis = opis;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public Date getDatum() {
        return this.datum;
    }

    public String getOpis() {
        return this.opis;
    }
}
