package com.mgradnja.Korisnik;

import java.sql.Date;

public class ItemUpitiProfilKorisnika {

    private Integer ID;
    private String naziv;
    private Date datum;
    private String opis;
    private String adresa;
    private String grad;
    private String zupanija;

    public ItemUpitiProfilKorisnika(Integer ID, String naziv, Date datum, String opis, String adresa, String grad, String zupanija){
        this.ID = ID;
        this.naziv = naziv;
        this.datum = datum;
        this.opis = opis;
        this.adresa = adresa;
        this.grad = grad;
        this.zupanija = zupanija;
    }

    public Integer getID() {
        return this.ID;
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

    public String getAdresa(){
        return  this.adresa;
    }

    public String getGrad(){
        return this.grad;
    }

    public String getZupanija(){
        return this.zupanija;
    }

}
