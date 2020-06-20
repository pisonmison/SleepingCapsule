package com.example.sleepingcapsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;

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

    //create two color objects for current color and favorite color for saving
    private RGBColor currentColor;
    private RGBColor favoriteColor;


    private ImageView mColorwheel;
    private TextView mHex;
    private View mAusgabe;
    private Bitmap bitmap;





    //color class which handles has all three colors as parameters
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

        mColorwheel.setOnTouchListener(this);

        saveAsFavoriteColor = findViewById(R.id.saveAsFavoriteColor_ID);
        showColor = findViewById(R.id.showFavoriteButton_ID);

        saveAsFavoriteColor.setOnClickListener(this);
        showColor.setOnClickListener(this);

        editTextR = findViewById(R.id.editText_R_ID);
        editTextB = findViewById(R.id.editText_B_ID);
        editTextG = findViewById(R.id.editText_G_ID);

        editTextR.setOnFocusChangeListener(this);
        editTextG.setOnFocusChangeListener(this);
        editTextB.setOnFocusChangeListener(this);





        //creating 2 instances of color class for saving
        currentColor = new RGBColor();
        favoriteColor = new RGBColor();



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


                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                if(r!=0 && g!=0 && b!=0){
                    currentColor.setrValue(r);
                    editTextR.setText(String.valueOf(r));

                    currentColor.setgValue(g);
                    editTextG.setText(String.valueOf(g));

                    currentColor.setbValue(b);
                    editTextB.setText(String.valueOf(b));


                    String hex ="#"+ Integer.toHexString(pixel);
                    mAusgabe.setBackgroundColor(Color.rgb(r,g,b));

                    mHex.setText("RGB: "+ r +", "+  g +", "+ b ); //\nHEX:wert

                    System.out.println("CURRENT COLOR:" + currentColor);
                }







            }
        }
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




/*
on focus change checks wether an edit text is actually focused or not, therefore
 executing methods after focus change when user finally decided his input

 */

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.editText_R_ID:

                if(!hasFocus){

                    String temp = editTextR.getText().toString();

                    if(temp.isEmpty()){
                        System.out.println("Empty Color Input");
                    }
                    else{
                        if(Integer.valueOf(temp) < 256){
                            currentColor.setrValue(Integer.valueOf(temp));
                            System.out.println("Set R Color to:" +  temp);
                            //send server data
                           // mAusgabe.setBackgroundColor(Color.rgb(currentColor.getrValue(), currentColor.getgValue(), currentColor.getgValue()));  edit text soll fabre updaten


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
                System.out.println("Current Color showing:" + currentColor);

                break;

        }
    }
}
