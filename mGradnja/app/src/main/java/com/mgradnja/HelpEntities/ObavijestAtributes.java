package com.mgradnja.HelpEntities;

public class ObavijestAtributes {

    private String mIzvodjac;
    private String mObavijest;
    private String mVrijeme;

    public ObavijestAtributes(String izvodjac,String obavijest, String vrijeme ){
        mIzvodjac=izvodjac;
        mObavijest=obavijest;
        mVrijeme=vrijeme;
    }

    public String getIzvodjaca(){
        return mIzvodjac;
    }

    public String getObavijest() {
        return mObavijest;
    }

    public String getVrijeme() {
        return mVrijeme;
    }
}
