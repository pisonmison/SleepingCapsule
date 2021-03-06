package com.example.sleepingcapsule;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeatScreen extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener, EditText.OnFocusChangeListener {




    private ImageButton favPosButton;
    private ImageButton lyingPosButton;
    private ImageButton zeroGPosButton;
    private ImageButton sittingPosButton;

    private String currentScreen = "seat";

    private EditText inputBackrest;
    private EditText inputSeat;
    private EditText inputFeetrest;




    private SeekBar seekBarBackrest;
    private SeekBar seekBarSeat;
    private SeekBar seekBarFeetrest;
    private ImageButton stopchairButton;

    private TextView seekBarInfoTop;
    private TextView seekBarInfoMid;
    private TextView seekBarInfoDown;

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;



    private Button saveButton;


   private Position actualPosition = new Position();

   private Position favoritePosition = new Position();
   private Position lyingPosition = new Position();
   private Position zeroGPosition = new Position();
   private Position sittingPosition = new Position();


   private Context mContext;
   private Client apiClient;



    //position class with 3 values
    public class Position {
        //class for positons
        int backAngle;
        int seatAngle;
        int feetAngle;

        public int getBackAngle() {
            return backAngle;
        }

        public void setBackAngle(int backAngle) {
            this.backAngle = backAngle;
        }

        public int getSeatAngle() {
            return seatAngle;
        }

        public void setSeatAngle(int midAngle) {
            this.seatAngle = midAngle;
        }

        public int getFeetAngle() {
            return feetAngle;
        }

        public void setFeetAngle(int feetAngle) {
            this.feetAngle = feetAngle;
        }

        //printing object for testing
        @Override
        public String toString() {
            return getClass().getSimpleName() + "[Angles:" + backAngle + "/" + seatAngle + "/" + feetAngle + "]";
        }
    }
    public static RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_screen);
        //configure starting position
        setPositionsOnStart();
        mContext = this;

        configurePosition(actualPosition, 80, 0 , 0);


        //assign all the objects to their corresponding layout objects by ID
        favPosButton = findViewById(R.id.favPos_ID);
        lyingPosButton = findViewById(R.id.lyingPos_ID);
        zeroGPosButton = findViewById(R.id.zeroGPos_ID);
        sittingPosButton = findViewById(R.id.sittingPos_ID);

        inputBackrest = findViewById(R.id.editTextBackrest_ID);
        inputSeat = findViewById(R.id.editTextSeat_ID);
        inputFeetrest = findViewById(R.id.editTextFeetrest_ID);

        seekBarBackrest = findViewById(R.id.seekBarTop_ID);
        seekBarSeat = findViewById(R.id.seekBarMiddle_ID);
        seekBarFeetrest = findViewById(R.id.seekBarDown_ID);

        seekBarInfoTop = findViewById(R.id.seekBarViewInfo_Top_ID);
        seekBarInfoMid = findViewById(R.id.seekBarViewInfo_Mid_ID);
        seekBarInfoDown = findViewById(R.id.seekBarViewInfo_Down_ID);

        saveButton = findViewById(R.id.savebutton_ID);
        stopchairButton = findViewById(R.id.stopChair_seatScreen_ID);

        seatIcon = findViewById(R.id.seatImageView_ID);
        lightIcon = findViewById(R.id.lightImageView_ID);
        musicIcon = findViewById(R.id.musicImageView_ID);

        //add on click, on change, on focus listeners

        inputBackrest.setOnFocusChangeListener(this);
        inputSeat.setOnFocusChangeListener(this);
        inputFeetrest.setOnFocusChangeListener(this);



        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);


        seekBarBackrest.setOnSeekBarChangeListener(this);
        seekBarSeat.setOnSeekBarChangeListener(this);
        seekBarFeetrest.setOnSeekBarChangeListener(this);



        favPosButton.setOnClickListener(this);
        lyingPosButton.setOnClickListener(this);
        zeroGPosButton.setOnClickListener(this);
        sittingPosButton.setOnClickListener(this);

        saveButton.setOnClickListener(this);
        stopchairButton.setOnClickListener(this);

        MainActivity.setTaskBarIcon(seatIcon,currentScreen);

        setAllEditTexts();
        setAllSeekbars(80,0,0);

        apiClient = new Client();

    }




    //pre configure settings for different positions
    public void setPositionsOnStart(){
        configurePosition(actualPosition, 0,0,0);
        configurePosition(lyingPosition, 0, 0, 89);
        configurePosition(zeroGPosition, 45, 15, 45);
        configurePosition(sittingPosition, 75,0,5);
    }

    //method which sets parameters in the position object
    public void configurePosition(Position p, int a, int b, int c){
        p.setBackAngle(a);
        p.setSeatAngle(b);
        p.setFeetAngle(c);
        System.out.println("Position was set:" + p + "Back:" + a + "Seat:" + b + "Feet:" + c);

    }

    //updates actual position based chosen position and passes to server
    public void updateActualPosition(Position p){
       actualPosition.setBackAngle(p.getBackAngle());
       actualPosition.setSeatAngle(p.getSeatAngle());
       actualPosition.setFeetAngle(p.getFeetAngle());
       System.out.println("updated Actual Pos:" + p.getBackAngle() + "/" + p.getSeatAngle() + "/" + p.getFeetAngle());
       setAllSeekbars(p.getBackAngle(), p.getSeatAngle(), p.getFeetAngle());
    }


    //sends 3 requests with corresponding endpoint to the server, adjust 3 values at the same time of the chair
public void sendPositionAngles2Server(){
        for(int i = 0; i < 3; i++){

        }
    apiClient.seatGetRequest("setanglebackrest","91",actualPosition.backAngle);
    apiClient.seatGetRequest("setangleseating","92",actualPosition.seatAngle);
    apiClient.seatGetRequest("setanglefootrest","92",actualPosition.feetAngle);

}
/*
Rückenlehne: 0° - 80° (werden noch ermittelt)

Sitzfläche: 0° - 33° (werden noch ermittelt)

Fußlehne: 0° - 89° (Verstellzeit zwischen Extremalwinkel: 15 Sek)
 */
//textwatcher provides three different edit text input stages to call methods in.



    public void setAllEditTexts(){

        inputBackrest.setText(String.valueOf(actualPosition.getBackAngle()));
        inputSeat.setText(String.valueOf(actualPosition.getSeatAngle()));
        inputFeetrest.setText(String.valueOf(actualPosition.getFeetAngle()));
    }


    //sets all seekbars in one method after updating actual pos
    public void setAllSeekbars(int angleBack, int angleSeat, int angleFeet){

            seekBarBackrest.setProgress(angleBack);
            seekBarSeat.setProgress(angleSeat);
            seekBarFeetrest.setProgress(angleFeet);

        }

        //update screen angle values when seekbar is moved
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.seekBarTop_ID:
                    inputBackrest.setText(String.valueOf(progress));
                    System.out.println(progress);
                    seekBarInfoTop.setText(String.valueOf(progress) + "°");
                    actualPosition.setBackAngle(progress);


                    break;
                case R.id.seekBarMiddle_ID:
                    inputSeat.setText(String.valueOf(progress));
                    System.out.println(progress);
                    seekBarInfoMid.setText(String.valueOf(progress) + "°");
                    actualPosition.setSeatAngle(progress);

                    break;
                case R.id.seekBarDown_ID:
                    inputFeetrest.setText(String.valueOf(progress));
                    System.out.println(progress);
                    seekBarInfoDown.setText(String.valueOf(progress) + "°");
                    actualPosition.setFeetAngle(progress);

                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {


        }


    /*send data to server only if user finnaly decided the final input and prooceeds onto something else
    therefore the edit text becomes unfocused and sends the final input to server
     */

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.editTextBackrest_ID:
                if(!hasFocus){
                    //check if string is "" for crash when field is empty!!
                    String temp = inputBackrest.getText().toString();
                    if(temp.isEmpty()){
                        System.out.println("Empty Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 81){
                            seekBarInfoTop.setText(temp+ "°");
                            seekBarBackrest.setProgress(Integer.valueOf(temp));
                            apiClient.seatGetRequest("setanglebackrest","91", actualPosition.getBackAngle());
                        }
                        else{
                            System.out.println("Wrong Input");
                        }
                    }
                }
                break;


            case R.id.editTextSeat_ID:
                if(!hasFocus){

                    String temp = inputSeat.getText().toString();
                    if(temp.isEmpty()){
                        System.out.println("Empty Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 31){
                            seekBarInfoMid.setText(temp+ "°");
                            seekBarSeat.setProgress(Integer.valueOf(temp));
                            apiClient.seatGetRequest("setangleseating","92", actualPosition.getSeatAngle());
                        }
                        else{
                            System.out.println("Wrong Input");
                        }
                    }
                }



                break;
            case R.id.editTextFeetrest_ID:
                if(!hasFocus){

                    String temp = inputFeetrest.getText().toString();
                    if(temp.isEmpty()){
                        System.out.println("Wrong Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 90){
                            seekBarInfoDown.setText(temp+ "°");
                            seekBarFeetrest.setProgress(Integer.valueOf(temp));
                            apiClient.seatGetRequest("setanglefootrest","92", actualPosition.getFeetAngle());
                        }
                        else{
                            System.out.println("Wrong Input");
                        }
                    }
                }
                break;
    }}

        //send data to server only when user has finished using the seekbar, as the chair moves not in realtime.
        //also sending too many requests results in callback errors.
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()) {
                case R.id.seekBarTop_ID:

                    apiClient.seatGetRequest("setanglebackrest","91", actualPosition.getBackAngle());

                    break;
                case R.id.seekBarMiddle_ID:

                    apiClient.seatGetRequest("setangleseating","92",actualPosition.getSeatAngle());
                    break;
                case R.id.seekBarDown_ID:

                    apiClient.seatGetRequest("setanglefootrest","92", actualPosition.getFeetAngle());
                    break;
            }

        }


        //functions to call when one of the buttons is clicked
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.favPos_ID:
                    updateActualPosition(favoritePosition);
                    setAllEditTexts();

                    sendPositionAngles2Server();
                    break;
                case R.id.lyingPos_ID:
                    updateActualPosition(lyingPosition);
                    setAllEditTexts();

                    sendPositionAngles2Server();
                    break;

                    case R.id.zeroGPos_ID:
                    updateActualPosition(zeroGPosition);
                    setAllEditTexts();

                    sendPositionAngles2Server();
                    break;

                case R.id.sittingPos_ID:
                    updateActualPosition(sittingPosition);
                    setAllEditTexts();

                    sendPositionAngles2Server();
                    break;

                    case R.id.savebutton_ID:
                    configurePosition(favoritePosition, actualPosition.getBackAngle(),actualPosition.getSeatAngle(), actualPosition.getFeetAngle());
                    setAllEditTexts();
                    Toast.makeText(this, "Saved to Fav. Position!", Toast.LENGTH_SHORT).show();
                    System.out.print(actualPosition);
                    break;
                case R.id.stopChair_seatScreen_ID:


                    apiClient.getThemesFromServerUsingRetrofit();

                    apiClient.stopChairGetRequest1();
                    apiClient.stopChairGetRequest2("setstopseating");
                    apiClient.stopChairGetRequest2("setstopfootrest");


                    Toast.makeText(this, "Chair Stopped!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.seatImageView_ID:
                    MainActivity.openSeatScreen(mContext);

                    break;
                case R.id.lightImageView_ID:
                    MainActivity.openLightScreen(mContext);

                    break;
                case R.id.musicImageView_ID:
                    MainActivity.openMusicScreen(mContext);

                    break;


            }

        }

//button bar layout??

}
