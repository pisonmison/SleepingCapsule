package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LightScreen extends AppCompatActivity implements Button.OnClickListener {


    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "light";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_screen);

        //taskbar iniation
        mContext = this;
        seatIcon = findViewById(R.id.seatImageView__LightScreen_ID);
        lightIcon = findViewById(R.id.lightImageView_LightScreen_ID);
        musicIcon = findViewById(R.id.musicImageView_LightScreen_ID);

        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);

        MainActivity.setTaskBarIcon(lightIcon,currentScreen);
        ////////



    }







    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.seatImageView__LightScreen_ID:
                MainActivity.openSeatScreen(mContext);
                break;
            case R.id.lightImageView_LightScreen_ID:
                MainActivity.openLightScreen(mContext);
                break;
            case R.id.musicImageView_LightScreen_ID:
                MainActivity.openMusicScreen(mContext);
                break;

        }

    }
}
