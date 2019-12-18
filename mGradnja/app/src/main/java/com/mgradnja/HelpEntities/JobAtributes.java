package com.mgradnja.HelpEntities;

import java.util.Date;

public class JobAtributes {
    private int mbrojPosla;
    private int mbrojUpita;
    private int mbrojSlike;
    private float mCijena;
    private String mNazivIzvodjaca;
    private String mOpisPosla;
    private String mNazivPosla;
    private Date mPočetakPosla;
    private Date mkrajPosla;

    public JobAtributes( int brojUpita, float cijena, String opisPosla, String nazivPosla, Date pocetakPosla, Date krajPosla, String nazivIzvodjaca, int brojSlike){

        //mbrojPonude = brojPonude;
        mCijena = cijena;
       // mbrojPosla = brojPosla;
        mbrojUpita = brojUpita;
        mNazivPosla = nazivPosla;
        mOpisPosla = opisPosla;
        mPočetakPosla = pocetakPosla;
        mkrajPosla = krajPosla;
        mNazivIzvodjaca = nazivIzvodjaca;
        mbrojSlike = brojSlike;
    }
    public int getMbrojSlike() {return mbrojSlike;}
    public int getMbrojPosla(){
        return mbrojPosla;
    }

    public int getMbrojUpita(){
        return mbrojUpita;
    }
    public String getmOpisPosla(){
        return mOpisPosla;

    }
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