package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//we need to implement onclicklistener to later use less code for button functions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String currentScreen;
    //create button variabels
    private ImageButton seatButton;
    private ImageButton musicButton;
    private ImageButton lightButton;
    public static Context mContext;
    private Client apiClient;
   // private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext= this;
        //set variable to the created imagebutton, cast to a imagebutton
         seatButton =   findViewById(R.id.seat_button1);
         lightButton =  findViewById(R.id.light_button1);
         musicButton =  findViewById(R.id.music_button1);

        /*we use "this" to pass the activity itself, which implements onclicklistener,
        therefore we spare alot of code defining on click methods for all 3 buttons
         */


        //set click listener to buttons above
        seatButton.setOnClickListener(this);
        lightButton.setOnClickListener(this);
        musicButton.setOnClickListener(this);

    }




/* use this as template for later get request of several json theme objects
    public void updatePost(){


        Call<Themes> call = dummyServerAPI.putPost(5);

        call.enqueue(new Callback<Integer>() {
        @Override
        public void onResponse(Call<Themes> call, Response<Post> response) {
            if(!response.isSuccessful()){
                System.out.println("Code unsuccesfull:" + response.code());
                response.getThemeID...
                response.getThemeName...
                return;
            }

            String content ="";
            content+= "Code Succesfull:" + response.code() + "\n";

        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("Message:"+t.getMessage());
        }
    });




    }*/





    //here we define the different methods, which the buttons shall call
    @Override
    public void onClick(View v) {


        //switch statement for checking which button is clicked, checks button_id
        //open desired activity on user click
        switch (v.getId()){
            case R.id.seat_button1:



                openSeatScreen(mContext);
                Toast.makeText(this,"Seat Button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.light_button1:
                openLightScreen(mContext);
                Toast.makeText(this,"Light Button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_button1:
                openMusicScreen(mContext);
                Toast.makeText(this,"Music Button clicked", Toast.LENGTH_SHORT).show();
                break;

        }


    }

    /*
    this method colors the corresponding taskbar icon depending on designed icon and
    the type of the screen "currentScreen" assigned.
     */
    public static void setTaskBarIcon(ImageView view, String currentScreen){

        if(currentScreen.equals("seat")){
            view.setImageResource(R.drawable.seat_icon_blue);
        }
        else if(currentScreen.equals("light")){

            view.setImageResource(R.drawable.light_icon_blue);

        }
        else if(currentScreen.equals("music")){

            view.setImageResource(R.drawable.music_icon_blue);
        }


    }



        /*those functions will create a new acitivity for the specified screen we want
        * if an activity already exitsts, it will be moved to the front, with all progress made.
        * -> done by addFlags function.
        * Static for use in the three screens.
        * */

    public static void openSeatScreen(Context mContext){

        Intent intent = new Intent(mContext, SeatScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);

    }

    public static void openLightScreen(Context mContext){

        Intent intent= new Intent(mContext, LightScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);
    }

    public static void openMusicScreen(Context mContext){

        Intent intent = new Intent(mContext, MusicScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);
    }

    public static void openMainMenu(Context mContext){

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);

    }




}
