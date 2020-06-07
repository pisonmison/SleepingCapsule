package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicScreen<soundpool> extends AppCompatActivity implements  Button.OnClickListener  {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "music";

    //textview theme
    ImageButton beachbutton;
    ImageButton rainbutton;
    ImageButton forrestbutton;
    ImageButton junglebutton;
    ImageButton fireplacebutton;
    ImageButton mountainbutton;
    TextView themeview;

    SoundPool soundpool;
    int beachsound, feuersound, rainsound, forestsound, junglesound, mountainsound;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundpool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        beachsound = soundpool.load(this, R.raw.beach, 1);
        feuersound= soundpool.load(this, R.raw.fireplace,1);
        rainsound=soundpool.load(this, R.raw.rain,1);
        forestsound= soundpool.load(this, R.raw.forest,1);
        junglesound=soundpool.load(this,R.raw.jungle,1);
        mountainsound=soundpool.load(this,R.raw.mountain,1);

        //taskbar initation
        mContext = this;
        seatIcon = findViewById(R.id.seatImageView__MusicScreen_ID);
        lightIcon = findViewById(R.id.lightImageView_MusicScreen_ID);
        musicIcon = findViewById(R.id.musicImageView_MusicScreen_ID);

        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);

        //zuordnung
        themeview = (TextView)findViewById(R.id. textViewtheme);
        beachbutton=(ImageButton)findViewById(R.id. strand_button);
        rainbutton=(ImageButton)findViewById(R.id. regen_button);
        forrestbutton=(ImageButton)findViewById(R.id. wald_button);
        mountainbutton=(ImageButton)findViewById(R.id. berg_button);
        junglebutton=(ImageButton)findViewById(R.id. dschungel_button);
        fireplacebutton=(ImageButton)findViewById(R.id. lagerfeuer_button);

        //onclicklistener
        beachbutton.setOnClickListener(this);
        rainbutton.setOnClickListener(this);
        forrestbutton.setOnClickListener(this);
        mountainbutton.setOnClickListener(this);
        junglebutton.setOnClickListener(this);
        fireplacebutton.setOnClickListener(this);





        MainActivity.setTaskBarIcon(musicIcon,currentScreen);
        //









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
            case R.id.strand_button:
                themeview.setText("Beach");
                soundpool.play(beachsound, 1, 1, 0, -1, 1);
                break;
            case R.id.wald_button:
                themeview.setText("Forrest");
                soundpool.play(forestsound, 1, 1, 0, -1, 1);
                break;
            case R.id.lagerfeuer_button:
                themeview.setText("Fireplace");
                soundpool.play(feuersound, 1, 1, 0, -1, 1);
                break;
            case R.id.dschungel_button:
                themeview.setText("Jungle");
                soundpool.play(junglesound, 1, 1, 0, -1, 1);
                break;
            case R.id.regen_button:
                themeview.setText("Rain");
                soundpool.play(rainsound, 1, 1, 0, -1, 1);
                break;
            case R.id.berg_button:
                themeview.setText("Mountain");
                soundpool.play(mountainsound, 1, 1, 0, -1, 1);
                break;

        }

    }

    public class Themes {
        public String title;



    }



}
