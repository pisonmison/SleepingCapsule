package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;

public class LightScreen extends AppCompatActivity implements Button.OnClickListener, ImageView.OnTouchListener, EditText.OnFocusChangeListener {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    public static  Context mContext;
    private String currentScreen = "light";
    private Button saveAsFavoriteColor;
    private EditText editTextR;
    private EditText editTextG;
    private EditText editTextB;
    private Button showColor;
    private ImageButton stopButton;

    //create two color objects for current color and favorite color for saving
    private RGBColor currentColor;
    private RGBColor favoriteColor;

// part for the colorwheel, with m prefix
    private ImageView mColorwheel;
    private TextView mHex;
   private View mAusgabe;
   // using bitmap
   private Bitmap bitmap;
   private SeekBar seekBar2;

   private PictureTread thread;



    // client object
    Client apiLightClient;

    private ImageButton chairLightSettings;
    private ImageButton roomLightSettings;

    private String currentColorSettings = "No settings chosen";





    //color class handles all three colors as parameters
    public class RGBColor{

        int rValue;
        int gValue;
        int bValue;

        public int getrValue() {
            return rValue;
        }

        public void setrValue(int rValue) {
            this.rValue = rValue;
        }



        public int getgValue() {
            return gValue;
        }

        public void setgValue(int gValue) {
            this.gValue = gValue;
        }

        public int getbValue() {
            return bValue;
        }

        public void setbValue(int bValue) {
            this.bValue = bValue;
        }


        //to string for testing
        @Override
        public String toString() {
            return getClass().getSimpleName() + "[R:" + rValue + "/G:" + gValue + "/B:" + bValue + "]";
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_screen);

        mColorwheel = findViewById(R.id.colorwheel);
        mHex = findViewById(R.id.hex);
        mAusgabe = findViewById(R.id.ausgabe);


        mColorwheel.setDrawingCacheEnabled(true);
        mColorwheel.buildDrawingCache(true);

        // colorwheel on touch listener
        mColorwheel.setOnTouchListener(this);

        saveAsFavoriteColor = findViewById(R.id.saveAsFavoriteColor_ID);
        showColor = findViewById(R.id.showFavoriteButton_ID);
       // set on click listener
        saveAsFavoriteColor.setOnClickListener(this);
        showColor.setOnClickListener(this);

        editTextR = findViewById(R.id.editText_R_ID);
        editTextB = findViewById(R.id.editText_B_ID);
        editTextG = findViewById(R.id.editText_G_ID);

        editTextR.setOnFocusChangeListener(this);
        editTextG.setOnFocusChangeListener(this);
        editTextB.setOnFocusChangeListener(this);

        stopButton = findViewById(R.id.stopChair_lightScreen_ID2);
        stopButton.setOnClickListener(this);


        chairLightSettings = findViewById(R.id.button_chaircolor_ID);
        roomLightSettings = findViewById(R.id.button_roomcolor_ID);
         // set on click Listener
        chairLightSettings.setOnClickListener(this);
        roomLightSettings.setOnClickListener(this);

        //creating 2 instances of color class for saving
        currentColor = new RGBColor();
        favoriteColor = new RGBColor();
       // creating instance for client
        apiLightClient = new Client();

        thread = new PictureTread(mColorwheel, bitmap);
        thread.start();

        seekBar2 = (SeekBar) findViewById(R.id seekBar2);
        seekBar2 .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread.adjustBrightness(seekBar2.getProgress()-255);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ///taskbar code////
        mContext = this;
        seatIcon = findViewById(R.id.seatImageView__LightScreen_ID);
        lightIcon = findViewById(R.id.lightImageView_LightScreen_ID);
        musicIcon = findViewById(R.id.musicImageView_LightScreen_ID);

        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);

        MainActivity.setTaskBarIcon(lightIcon,currentScreen);




    }



    /*same funtionality as in seat screen. just with color values.
    * checks wether input is empty and in [0:255]
    * if input correct, then save into currentColor settings
    * */



    /*provides colorwheel funtionality for on touch listener
        * saves gotten pixels into current color object
        * prints current color choice afterwards in console

        * */
    public void startColorWheel(View v, MotionEvent event) {

        try {
            //  Block of code to try

            //get pixels data from the color wheel image
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                bitmap = mColorwheel.getDrawingCache();

                int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                //getting RGB values
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                //ignores black when touching outside the colorwheel
                if(r==0 && g==0 && b==0) {
                    System.out.println("Wrong color");
                }else{
                    currentColor.setrValue(r);
                    editTextR.setText(String.valueOf(r));

                    currentColor.setgValue(g);
                    editTextG.setText(String.valueOf(g));

                    currentColor.setbValue(b);
                    editTextB.setText(String.valueOf(b));

                    //getting Hex value
                    String hex ="#"+ Integer.toHexString(pixel);
                    //set background color of Ausgabe according to the picked color
                    mAusgabe.setBackgroundColor(Color.rgb(r,g,b));
                    //set Hex value to textview
                    mHex.setText("Hex: "+ hex); //\nHEX:wert "RGB: "+ r +", "+  g +", "+ b
                    // shows current color terminal
                    System.out.println("CURRENT COLOR:" + currentColor);
                }







            }





            /**
             *  /*send light http request here:  setlightseat | setlightinterior
                  when user releases his finger from wheel -> color chosen.
                    currently this only works for seat.
             */




               
            else if(event.getAction() == MotionEvent.ACTION_UP){

                if(currentColorSettings.equals("seat")){
                    apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }else if(currentColorSettings.equals("room")){
                    apiLightClient.colorGetRequest("setlightinterior", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }
                else{
                    System.out.println("NO COLOR SETTINGS CHOSEN");
                    //a new user probably would think to change room color first, so we just send on undefined settings colordata to room.
                    apiLightClient.colorGetRequest("setlightinterior", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }

            }
        }
        // warning colorwheel
        catch(Exception e) {
                e.printStackTrace();
                System.out.println("!Touching outside the color wheel!");
            }


    }



/*
    //overrides return android button to not close activity but to go to main menu instead.
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        //MainActivity.openMainMenu(mContext);
    }

*/

// chair color button
public void setChairSettings(){
    Toast.makeText(this, "Now choose your seat color!", Toast.LENGTH_SHORT).show();
    currentColorSettings = "seat";
    chairLightSettings.setBackgroundResource(R.drawable.roundbutton_when_clicked);
    roomLightSettings.setBackgroundResource(R.drawable.roundbutton);
    apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());

}


public void setRoomSettings(){
    Toast.makeText(this, "Now choose your room color!", Toast.LENGTH_SHORT).show();
    roomLightSettings.setBackgroundResource(R.drawable.roundbutton_when_clicked);
    chairLightSettings.setBackgroundResource(R.drawable.roundbutton);
    currentColorSettings = "room";
    apiLightClient.colorGetRequest("setlightinterior", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());


}
/*
on focus change checks wether an edit text is actually focused or not, therefore
 executing methods after focus change when user finally decided his input

 */
   // focus
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.editText_R_ID:

                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                    String temp = editTextR.getText().toString();

                    if(temp.isEmpty()){
                        System.out.println("Empty Color Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 256){
                            currentColor.setrValue(Integer.valueOf(temp));
                            System.out.println("Set R Color to:" +  temp);
                            //send server data
                            mAusgabe.setBackgroundColor(Color.rgb(currentColor.getrValue(), currentColor.getgValue(), currentColor.getgValue()));
                            apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());


                        }
                        else{
                            System.out.println("Wrong Color Input: out of bounds[0,255]");
                        }
                    }

                }

                ///////////////////////////////////////////////
            case R.id.editText_G_ID:
                if(!hasFocus){

                    String temp = editTextG.getText().toString();

                    if(temp.isEmpty()){
                        System.out.println("Empty Color Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 256){
                            currentColor.setgValue(Integer.valueOf(temp));
                            System.out.println("Set G Color to:" +  temp);
                            //send server data
                            mAusgabe.setBackgroundColor(Color.rgb(currentColor.getrValue(), currentColor.getgValue(), currentColor.getgValue()));
                            apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());

                        }
                        else{
                            System.out.println("Wrong Color Input: out of bounds[0,255]");
                        }
                    }


                }

                ///////////////////////////////////////////////
            case R.id.editText_B_ID:
                if(!hasFocus){

                    String temp = editTextB.getText().toString();

                    if(temp.isEmpty()){
                        System.out.println("Empty Color Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 256){
                            currentColor.setbValue(Integer.valueOf(temp));
                            System.out.println("Set B Color to:" +  temp);
                            //send server data
                            mAusgabe.setBackgroundColor(Color.rgb(currentColor.getrValue(), currentColor.getgValue(), currentColor.getgValue()));
                            apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());

                        }
                        else{
                            System.out.println("Wrong Color Input: out of bounds[0,255]");
                        }
                    }

                }


    }
    }


    //calls methods when touching color wheel
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        startColorWheel(v, event);

        return true; //for continous syncing
    }


    //on click methods for different buttons
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
            case R.id.saveAsFavoriteColor_ID:


                favoriteColor = currentColor;
                System.out.println("Current Saved Color:" + favoriteColor);

                break;
            case R.id.showFavoriteButton_ID:


                currentColor = favoriteColor;
                if(currentColorSettings.equals("seat")){
                    apiLightClient.colorGetRequest("setlightseat", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }else if(currentColorSettings.equals("room")){
                    apiLightClient.colorGetRequest("setlightinterior", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }
                else{
                    System.out.println("NO COLOR SETTINGS CHOSEN");
                    apiLightClient.colorGetRequest("setlightinterior", currentColor.getrValue(), currentColor.getgValue(), currentColor.getbValue());
                }

                System.out.println("Current Color showing:" + currentColor);

                break;
            case R.id.stopChair_lightScreen_ID2:
                apiLightClient.stopChairGetRequest1();
                apiLightClient.stopChairGetRequest2("setstopseating");
                apiLightClient.stopChairGetRequest2("setstopfootrest");

                break;

            case R.id.button_chaircolor_ID:

                setChairSettings();

                break;

            case R.id.button_roomcolor_ID:

                setRoomSettings();

                break;



        }
    }
}
