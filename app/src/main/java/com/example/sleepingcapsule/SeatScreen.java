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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_screen);
        //configure starting position
        setPositionsOnStart();
        mContext = this;
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

        //add on click, on change listeners
        inputBackrest.addTextChangedListener(watcher);
        inputSeat.addTextChangedListener(watcher);
        inputFeetrest.addTextChangedListener(watcher);

        inputBackrest.setOnFocusChangeListener(this);

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
        apiClient = new Client();
    }

    /**
     * timer method which handles executing http requests slighty delayed for callback failure evasion
     */



    //pre configure settings for different positions
    public void setPositionsOnStart(){
        configurePosition(actualPosition, 0,0,0);
        configurePosition(lyingPosition, 0, 0, 90);
        configurePosition(zeroGPosition, 70, 15, 60);
        configurePosition(sittingPosition, 85,0,5);
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
    apiClient.seatGetRequest("setanglebackrest",actualPosition.backAngle);
    apiClient.seatGetRequest("setangleseating",actualPosition.seatAngle);
    apiClient.seatGetRequest("setanglefootrest",actualPosition.feetAngle);

}


//textwatcher provides three different edit text input stages to call methods in.

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (inputBackrest.isFocused()) {

                //check if string is "" for crash when field is empty!!
                String temp = inputBackrest.getText().toString();
                if(temp.isEmpty()){
                    System.out.println("Empty Input");
                }
                else{
                    if(Integer.valueOf(temp) < 88){
                        seekBarInfoTop.setText(temp+ "°");
                        seekBarBackrest.setProgress(Integer.valueOf(temp));
                    }
                    else{
                        System.out.println("Wrong Input");
                    }
                }





            }

            else if (inputSeat.isFocused()) {
                String temp = inputSeat.getText().toString();
                if(temp.isEmpty()){
                    System.out.println("Empty Input");
                }
                else{
                    if(Integer.valueOf(temp) < 31){
                        seekBarInfoMid.setText(temp+ "°");
                        seekBarSeat.setProgress(Integer.valueOf(temp));
                    }
                    else{
                        System.out.println("Wrong Input");
                    }
                }


            }
            else if (inputFeetrest.isFocused()) {
                String temp = inputFeetrest.getText().toString();
                if(temp.isEmpty()){
                    System.out.println("Wrong Input");
                }
                else{
                    if(Integer.valueOf(temp) < 91){
                            seekBarInfoDown.setText(temp+ "°");
                            seekBarFeetrest.setProgress(Integer.valueOf(temp));
                        }
                        else{
                            System.out.println("Wrong Input");
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {




        }
    };



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
                    apiClient.seatGetRequest("setanglebackrest", actualPosition.getBackAngle());

                }
                break;


            case R.id.editTextSeat_ID:
                if(!hasFocus){
                    apiClient.seatGetRequest("setangleseating", actualPosition.getSeatAngle());

                }
                break;
            case R.id.editTextFeetrest_ID:
                if(!hasFocus){
                    apiClient.seatGetRequest("setanglefootrest", actualPosition.getFeetAngle());


                }
                break;
    }}

        //send data to server only when user has finished using the seekbar, as the chair moves not in realtime.
        //also sending too many requests results in callback errors.
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()) {
                case R.id.seekBarTop_ID:

                    apiClient.seatGetRequest("setanglebackrest", actualPosition.getBackAngle());

                    break;
                case R.id.seekBarMiddle_ID:

                    apiClient.seatGetRequest("setangleseating", actualPosition.getSeatAngle());
                    break;
                case R.id.seekBarDown_ID:

                    apiClient.seatGetRequest("setanglefootrest", actualPosition.getFeetAngle());
                    break;
            }

        }


        //functions to call when one of the buttons is clicked
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.favPos_ID:
                    updateActualPosition(favoritePosition);
                    Toast.makeText(this, "Fav. Position chosen", Toast.LENGTH_SHORT).show();
                    sendPositionAngles2Server();
                    break;
                case R.id.lyingPos_ID:
                    updateActualPosition(lyingPosition);
                    Toast.makeText(this, "Lying Position chosen", Toast.LENGTH_SHORT).show();
                    sendPositionAngles2Server();
                    break;

                    case R.id.zeroGPos_ID:
                    updateActualPosition(zeroGPosition);
                    Toast.makeText(this, "Zero G Position chosen", Toast.LENGTH_SHORT).show();
                    sendPositionAngles2Server();
                    break;

                case R.id.sittingPos_ID:
                    updateActualPosition(sittingPosition);
                    Toast.makeText(this, "Sitting Position chosen", Toast.LENGTH_SHORT).show();
                    sendPositionAngles2Server();
                    break;

                    case R.id.savebutton_ID:
                    configurePosition(favoritePosition, actualPosition.getBackAngle(),actualPosition.getSeatAngle(), actualPosition.getFeetAngle());
                    Toast.makeText(this, "Saved to Fav. Position!", Toast.LENGTH_SHORT).show();
                    System.out.print(actualPosition);
                    break;
                case R.id.stopChair_seatScreen_ID:
                    //stopchair();
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
