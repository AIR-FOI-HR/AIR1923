package com.mgradnja.HelpEntities;

import java.util.Date;

public class JobAtributes {
    private int mbrojPosla;
    private int mbrojUpita;
    private int mbrojSlike;
    private int mPlaceno;
    private float mCijena;
    private String mNazivIzvodjaca;
    private String mOpisPosla;
    private String mIme;
    private String mPrezime;
    private String mNazivPosla;
    private Date mPočetakPosla;
    private Date mkrajPosla;

    public JobAtributes(int placeno, int brojUpita, float cijena, String opisPosla, String nazivPosla, Date pocetakPosla, Date krajPosla, String nazivIzvodjaca, int brojSlike, String ime, String prezime){


        mCijena = cijena;
        mPlaceno = placeno;
        mbrojUpita = brojUpita;
        mNazivPosla = nazivPosla;
        mOpisPosla = opisPosla;
        mPočetakPosla = pocetakPosla;
        mkrajPosla = krajPosla;
        mNazivIzvodjaca = nazivIzvodjaca;
        mbrojSlike = brojSlike;
        mIme = ime;
        mPrezime = prezime;
    }
    public int getMbrojSlike() {return mbrojSlike;}
    public int getMbrojPosla(){
        return mbrojPosla;
    }
    public String getmIme() {return mIme;}
    public String getmPrezime() {return mPrezime;}
    public int getMbrojUpita(){
        return mbrojUpita;
    }
    public String getmOpisPosla(){
        return mOpisPosla;

    }
    public int getmPlaceno(){return mPlaceno;}
    public String getmNazivPosla(){
        return mNazivPosla;
    }
    public Date getmPočetakPosla(){
        return mPočetakPosla;
    }
    public Date getMkrajPosla(){
        return mkrajPosla;
    }
    public float getmCijena(){
        return mCijena;
    }
    public String getmNazivIzvodjaca(){
        return mNazivIzvodjaca;
    }
}
