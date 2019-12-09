package com.mgradnja.HelpEntities;

import java.util.Date;

public class JobAtributes {
    private int mbrojPosla;
    private int mbrojUpita;
    private int mbrojPonude;
    private float mCijena;
    private int mbrojIzvodjaca;
    private String mOpisPosla;
    private String mNazivPosla;
    private Date mPočetakPosla;
    private Date mkrajPosla;

    public JobAtributes( int brojUpita, float cijena, int brojIzvodjaca, String opisPosla, String nazivPosla, Date pocetakPosla, Date krajPosla){

        //mbrojPonude = brojPonude;
        mCijena = cijena;
       // mbrojPosla = brojPosla;
        mbrojUpita = brojUpita;
        mNazivPosla = nazivPosla;
        mOpisPosla = opisPosla;
        mPočetakPosla = pocetakPosla;
        mkrajPosla = krajPosla;
        mbrojIzvodjaca = brojIzvodjaca;
    }

    public int getMbrojPosla(){
        return mbrojPosla;
    }
    public int getMbrojPonude(){
        return mbrojPonude;
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
    public int getMbrojIzvodjaca(){
        return mbrojIzvodjaca;
    }
}
