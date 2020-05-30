package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SeatScreen extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener, EditText.OnFocusChangeListener {

    private ImageButton favPosButton;
    private ImageButton lyingPosButton;
    private ImageButton zeroGPosButton;
    private ImageButton sittingPosButton;

    private EditText inputBackrest;
    private EditText inputSeat;
    private EditText inputFeetrest;

    private LinearLayout buttonlayout;
    private LinearLayout sliderlayout;

    private SeekBar seekBarBackrest;
    private SeekBar seekBarSeat;
    private SeekBar seekBarFeetrest;
    private ImageButton stopchairButton;

    private TextView seekBarInfoTop;
    private TextView seekBarInfoMid;
    private TextView seekBarInfoDown;

    private Button saveButton;


   private Position actualPosition = new Position();

   private Position favoritePosition = new Position();
   private Position lyingPosition = new Position();
   private Position zeroGPosition = new Position();
   private Position sittingPosition = new Position();

   private int barBackrestValue;
   private int barSeatValue;
   private int barFeetValue;



    //position class with 3 values
    public class Position {
        //class for positons
        int backPos;
        int midPos;
        int feetPos;

        public int getBackPos() {
            return backPos;
        }

        public void setBackPos(int backPos) {
            this.backPos = backPos;
        }

        public int getMidPos() {
            return midPos;
        }

        public void setMidPos(int midPos) {
            this.midPos = midPos;
        }

        public int getFeetPos() {
            return feetPos;
        }

        public void setFeetPos(int feetPos) {
            this.feetPos = feetPos;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_screen);
        //configure starting position
        setPositionsOnStart();
        favPosButton = findViewById(R.id.favPos_ID);
        lyingPosButton = findViewById(R.id.lyingPos_ID);
        zeroGPosButton = findViewById(R.id.zeroGPos_ID);
        sittingPosButton = findViewById(R.id.sittingPos_ID);

        inputBackrest = findViewById(R.id.editTextBackrest_ID);
        inputSeat = findViewById(R.id.editTextSeat_ID);
        inputFeetrest = findViewById(R.id.editTextFeetrest_ID);

        inputBackrest.setOnFocusChangeListener(this);

        seekBarBackrest = findViewById(R.id.seekBarTop_ID);
        seekBarSeat = findViewById(R.id.seekBarMiddle_ID);
        seekBarFeetrest = findViewById(R.id.seekBarDown_ID);

        seekBarBackrest.setOnSeekBarChangeListener(this);
        seekBarSeat.setOnSeekBarChangeListener(this);
        seekBarFeetrest.setOnSeekBarChangeListener(this);

        seekBarInfoTop = findViewById(R.id.seekBarViewInfo_Top_ID);
        seekBarInfoMid = findViewById(R.id.seekBarViewInfo_Mid_ID);
        seekBarInfoDown = findViewById(R.id.seekBarViewInfo_Down_ID);



        saveButton = findViewById(R.id.savebutton_ID);
        stopchairButton = findViewById(R.id.stopChair_seatScreen_ID);

        favPosButton.setOnClickListener(this);
        lyingPosButton.setOnClickListener(this);
        zeroGPosButton.setOnClickListener(this);
        sittingPosButton.setOnClickListener(this);

        saveButton.setOnClickListener(this);
        stopchairButton.setOnClickListener(this);

    }

//check whether userinput is correct
  /*  public boolean inputCheck(TextView v){
        String temp = v.getText().toString();
        int angle = Integer.parseInt(temp);
        if(angle >= 0 && angle <=87){
            return true;
        }
        else {
            return false;
        }
    }
*/

    public void setPositionsOnStart(){
        configurePosition(actualPosition, 0,0,0);
        configurePosition(lyingPosition, 0, 15, 30);
        configurePosition(zeroGPosition, 20, 30, 40);
        configurePosition(sittingPosition, 1,2,3);
    }

    //method which sets parameters in the position object
    public void configurePosition(Position p, int a, int b, int c){
        p.setBackPos(a);
        p.setMidPos(b);
        p.setFeetPos(c);
        System.out.println("Position was set:" + p + "Back:" + a + "Seat:" + b + "Feet:" + c);

    }

    //updates actual position based chosen position and passes to server
    public void updateActualPosition(Position p){
       actualPosition.setFeetPos(p.getFeetPos());
       actualPosition.setMidPos(p.getMidPos());
       actualPosition.setMidPos(p.getFeetPos());
       System.out.println("updated Actual Pos:" + p.getBackPos() + "/" + p.getMidPos() + "/" + p.getFeetPos());
    }



    
    //update screen angle values when seekbar is moved
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBarTop_ID:

                System.out.println(progress);
                seekBarInfoTop.setText(String.valueOf(progress) + "°");
                break;
            case R.id.seekBarMiddle_ID:
                System.out.println(progress);
                seekBarInfoMid.setText(String.valueOf(progress) + "°");

                break;
            case R.id.seekBarDown_ID:
                System.out.println(progress);
                seekBarInfoDown.setText(String.valueOf(progress) + "°");

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    //functions to call when one of the buttons is clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.favPos_ID:
                updateActualPosition(favoritePosition);
                Toast.makeText(this,"Fav. Position chosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lyingPos_ID:
                updateActualPosition(lyingPosition);
                Toast.makeText(this,"Lying Position chosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.zeroGPos_ID:
                updateActualPosition(zeroGPosition);
                Toast.makeText(this,"Zero G Position chosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sittingPos_ID:
                updateActualPosition(sittingPosition);
                Toast.makeText(this,"Sitting Position chosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.savebutton_ID:
                configurePosition(favoritePosition, barBackrestValue, barSeatValue, barFeetValue);
                Toast.makeText(this,"Saved to Fav. Position!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stopChair_seatScreen_ID:
                //stopchair();
                Toast.makeText(this,"Chair Stopped!", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    //doesnt work fully!. App closes when parsing String to int
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {


            case R.id.editTextBackrest_ID:
                String temp = inputBackrest.getText().toString();
                seekBarInfoTop.setText(temp);
                break;
            case R.id.editTextSeat_ID:
                String temp2 = inputSeat.getText().toString();
                seekBarInfoMid.setText(temp2);
                break;
            case R.id.editTextFeetrest_ID:
                String temp3 = inputFeetrest.getText().toString();
                seekBarInfoDown.setText(temp3);
                break;



        }}


//button bar layout??

}