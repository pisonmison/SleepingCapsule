package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.time.ZoneOffset;

//we need to implement onclicklistener to later use less code for button functions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //create button variabels
    private ImageButton seatButton;
    private ImageButton musicButton;
    private ImageButton lightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set variable to the created imagebutton, cast to a imagebutton
         seatButton = (ImageButton) findViewById(R.id.seat_button1);
         lightButton = (ImageButton) findViewById(R.id.light_button1);
         musicButton = (ImageButton) findViewById(R.id.music_button1);

        /*we use "this" to pass the activity itself, which implements onclicklistener,
        therefore we spare alot of code defining on click methods for all 3 buttons
         */


        //set click listener to buttons above
        seatButton.setOnClickListener(this);
        lightButton.setOnClickListener(this);
        musicButton.setOnClickListener(this);


    }


    //here we define the different methods, which the buttons shall call
    @Override
    public void onClick(View v) {


        //switch statement for checking which button is clicked, checks button_id
        switch (v.getId()){
            case R.id.seat_button1:
                openSeatScreen();
                Toast.makeText(this,"Seat Button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.light_button1:
                openLightScreen();
                Toast.makeText(this,"Light Button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_button1:
                openMusicScreen();
                Toast.makeText(this,"Music Button clicked", Toast.LENGTH_SHORT).show();
                break;

        }


    }

        //those functions will create a new acitivity for the specified screen we want
    public void openSeatScreen(){

        Intent intent = new Intent(this, SeatScreen.class);
        startActivity(intent);
    }

    public void openLightScreen(){

        Intent intent = new Intent(this, LightScreen.class);
        startActivity(intent);
    }

    public void openMusicScreen(){

        Intent intent = new Intent(this, MusicScreen.class);
        startActivity(intent);
    }



}
