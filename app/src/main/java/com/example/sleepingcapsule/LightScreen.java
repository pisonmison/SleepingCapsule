package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LightScreen extends AppCompatActivity implements Button.OnClickListener {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "light";


    ImageView mColorwheel;
    TextView mHex;
    View mAusgabe;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_screen);

        mColorwheel = findViewById(R.id.colorwheel);
        mHex = findViewById(R.id.hex);
        mAusgabe = findViewById(R.id.ausgabe);

        mColorwheel.setDrawingCacheEnabled(true);
        mColorwheel.buildDrawingCache(true);

        mColorwheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    bitmap = mColorwheel.getDrawingCache();

                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    String hex ="#"+ Integer.toHexString(pixel);

                    mAusgabe.setBackgroundColor(Color.rgb(r,g,b));

                    mHex.setText("RGB: "+ r +", "+  g +", "+ b +"\nHex: "+ hex);


                }

                return true;
            }
        });



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
