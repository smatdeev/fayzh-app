package com.smartdeev.faizyah;

import android.support.annotation.NonNull;

public class ItemSound {

    public ItemSound() {
        super();
    }

     private String stitlesound;
    private String sdessound;
    private String surlsound;



    public ItemSound(String titlesound, String dessound, String urlsound) {
        stitlesound = titlesound;
        sdessound = dessound;
        surlsound = urlsound;


    }

    public String gettitlesound() {
        return stitlesound;
    }

    public String getdessound() {
        return sdessound;
    }

    //saeed
    public String geturlsound() {
        return surlsound;
    }


}