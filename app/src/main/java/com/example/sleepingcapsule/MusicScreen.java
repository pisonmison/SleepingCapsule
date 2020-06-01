package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MusicScreen extends AppCompatActivity implements  Button.OnClickListener  {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);

        //taskbar initation
        mContext = this;
        seatIcon = findViewById(R.id.seatImageView__MusicScreen_ID);
        lightIcon = findViewById(R.id.lightImageView_MusicScreen_ID);
        musicIcon = findViewById(R.id.musicImageView_MusicScreen_ID);

        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);

        MainActivity.setTaskBarIcon(musicIcon,currentScreen);
        //////

        final MediaPlayer wavesounds = MediaPlayer.create(this, R.raw.meeresrauschen);
        ImageButton playwavesound = (ImageButton) this.findViewById(R.id.strand_button);
        playwavesound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wavesounds.start();
            }
        });




    }







    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.seatImageView__MusicScreen_ID:
                MainActivity.openSeatScreen(mContext);
                break;
            case R.id.lightImageView_MusicScreen_ID:
                MainActivity.openLightScreen(mContext);
                break;
            case R.id.musicImageView_MusicScreen_ID:
                MainActivity.openMusicScreen(mContext);
                break;

        }

    }




}
