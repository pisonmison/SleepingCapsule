package com.example.sleepingcapsule;

public class Themes {
    public String mTitle;
    public String mDescription;
    public int mImage;
    public int mMusic;
    //public String mColor;
    //public int id;

    public Themes() {

    }

    public Themes(String Title, String Description, int image, int music) {
        mTitle = Title;
        mDescription = Description;
        mImage = image;
        mMusic = music;
        //mColor = color;
    }



    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getImage() {
        return mImage;
    }

    public int getMusic() {
        return mMusic;
    }

   /* public String getColor(){
        return mColor;
    }

    */
}
