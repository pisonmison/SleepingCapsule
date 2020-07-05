package com.example.sleepingcapsule;

import android.net.Uri;

public class Themes {
    public String mTitle;
    public String mDescription;
    public String mImage;

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setmMusic(String mMusic) {
        this.mMusic = mMusic;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String mMusic;
    public String mColor;
    public int mId;

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmMusic() {
        return mMusic;
    }

    public String getmColor() {
        return mColor;
    }

    public int getmId() {
        return mId;
    }



    public Themes() {

    }

    public Themes(String Title, String Description, String image, String music, String color, int id) {
        mTitle = Title;
        mDescription = Description;
        mImage = image;
        mMusic = music;
        mColor = color;
        mId = id;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "[Theme:" + mTitle + "/" + mDescription + "/" + mImage + "/" + mMusic + "/"+ mColor + "/"+ mId + "]";
    }



}
