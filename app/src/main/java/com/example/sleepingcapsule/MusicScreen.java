package com.example.sleepingcapsule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MusicScreen extends AppCompatActivity implements  Button.OnClickListener {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    public static Context mContext;
    private String currentScreen = "music";

    private RequestQueue mQueue;

    private Client apiMusicClient;



    private ArrayList<Themes> themelist = new ArrayList<Themes>();


    private TextView themeDescriptionView;

    private ImageButton spotifyButton;
    private Button playFavoriteThemeButton;
    private Button saveFavoriteThemeButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton stopsoundbutton;
    private ImageButton musicLibraryButton;
    private ImageButton stopchairButton;

    private MediaPlayer player;



    private Themes actualTheme;
    private Themes favoriteTheme;

    private ListView musicListView;
    private ThemesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);
        apiMusicClient = new Client();
        actualTheme = new Themes(); //theme selected in the listview
        favoriteTheme = new Themes();//theme saved

        enableMusic();
        mQueue = Volley.newRequestQueue(this);


        //taskbar initation
        mContext = this;
        seatIcon = findViewById(R.id.seatImageView__MusicScreen_ID);
        lightIcon = findViewById(R.id.lightImageView_MusicScreen_ID);
        musicIcon = findViewById(R.id.musicImageView_MusicScreen_ID);

        seatIcon.setOnClickListener(this);
        lightIcon.setOnClickListener(this);
        musicIcon.setOnClickListener(this);

        stopchairButton = findViewById(R.id.stopChair_musicScreen_ID);
        stopchairButton.setOnClickListener(this);
        spotifyButton = findViewById(R.id.spotifyButton_ID);
        spotifyButton.setOnClickListener(this);

        //zuordnung
        themeDescriptionView = findViewById(R.id.textDescriptionView);


        playButton = findViewById(R.id.playbutton);
        pauseButton = findViewById(R.id.pausebutton);
        stopsoundbutton = findViewById(R.id.stopsoundbutton);
        musicLibraryButton = findViewById(R.id.musicLibraryButton);


        saveFavoriteThemeButton = findViewById(R.id.saveFovriteThemeButton);
        playFavoriteThemeButton = findViewById(R.id.playFavoriteThemeButton);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopsoundbutton.setOnClickListener(this);
        musicLibraryButton.setOnClickListener(this);

        saveFavoriteThemeButton.setOnClickListener(this);
        playFavoriteThemeButton.setOnClickListener(this);

        loadThemesFromServer();



        MainActivity.setTaskBarIcon(musicIcon, currentScreen);


        musicListView = findViewById(R.id.listview);
        adapter = new ThemesListAdapter(this, themelist);
        musicListView.setAdapter(adapter);

/*
hier werden die themes ausgewählt
 */
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                actualTheme = themelist.get(position);
                playMusic(actualTheme);
                hextorgbAndSend(actualTheme);

            }

        });


    }

    /**
     * calculates rgb values from our Hex #RRGGBB String and sends it to server on
     * itemClick -> when theme is picked.
     */
    private void hextorgbAndSend(Themes theme){
        int r= Integer.valueOf(theme.getmColor().substring(1, 3), 16);
        int g= Integer.valueOf(theme.getmColor().substring(3, 5), 16);
        int b= Integer.valueOf(theme.getmColor().substring(5, 7), 16);
        apiMusicClient.colorGetRequest("setlightinterior", r,g,b);
        apiMusicClient.colorGetRequest("setledseat", r,g,b);
        System.out.println("Theme Color was sent: (R:" + r +"/G:" + g + "/B:" +b +")");
}

    private void enableMusic(){
        Client.handleSSLHandshake();

    }


    private void loadThemesFromServer() {
        String url = "https://10.18.12.95:3000/api/Themes?access_token=12345";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonTheme = response.getJSONObject(i);
                        Themes theme = new Themes();
                        theme.setmTitle(jsonTheme.getString("title"));
                        theme.setmDescription(jsonTheme.getString("description"));
                        theme.setmImage(jsonTheme.getString("image"));
                        theme.setmMusic(jsonTheme.getString("music"));
                        theme.setmColor(jsonTheme.getString("color"));
                        theme.setmId(jsonTheme.getInt("id"));
                        themelist.add(theme);
                        System.out.println(theme);
                        adapter.notifyDataSetChanged(); //refresh adapter on every change of themelist. in real time.


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(jsonArrayRequest);

    }

    //transfers data into our listview
    public class ThemesListAdapter extends ArrayAdapter<Themes> {

        public ThemesListAdapter(@NonNull Context context, @NonNull ArrayList<Themes> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_view_layout, parent, false);
            Themes currentTheme = getItem(position);
            TextView themeTitle = convertView.findViewById(R.id.themetitle);

            assert currentTheme != null;
            themeTitle.setText(currentTheme.getmTitle());//

            ImageView themeImage = convertView.findViewById(R.id.themeimage);
            Picasso.get().load(currentTheme.getmImage()).into(themeImage);//loads image from url

            return convertView;
        }
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
            case R.id.playbutton:
                resumePlayer();
                break;
            case R.id.pausebutton:
                pausePlayer();
                break;
            case R.id.stopsoundbutton:
                stopPlayer();
                break;
            case R.id.saveFovriteThemeButton:
                saveFavorite();
                break;
            case R.id.playFavoriteThemeButton:
                if (favoriteTheme.mMusic != null) {
                    playMusic(favoriteTheme);
                    hextorgbAndSend(favoriteTheme);
                }
                else{
                    Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.musicLibraryButton:
                Toast.makeText(this, "Unplug the AUX cable from the tablet and plug it into your device.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stopChair_musicScreen_ID:
                apiMusicClient.stopChairGetRequest1();
                apiMusicClient.stopChairGetRequest2("setstopseating");
                apiMusicClient.stopChairGetRequest2("setstopfootrest");
                break;

            case R.id.spotifyButton_ID:
                openSpotify();
                break;

        }

    }
/* stop button
*/
    private void stopPlayer() {
        try {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            } else {
                Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
/*
play button
 */
    private void resumePlayer(){
            try {
                if (actualTheme.mMusic != null) {
                    if (player == null) {
                        Toast.makeText(this, "Sound is stopped", Toast.LENGTH_SHORT).show();
                    } else if (player.isPlaying()) {
                        Toast.makeText(this, "Already playing", Toast.LENGTH_SHORT).show();
                    } else if (!player.isPlaying()) {
                        player.start();
                    }
                } else {
                    Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
/*
pause button
 */
    private void pausePlayer() {
        try {
            if (actualTheme.mMusic != null) {
                if (player == null) {
                    Toast.makeText(this, "Sound is stopped", Toast.LENGTH_SHORT).show();
                } else if (player.isPlaying()) {
                    player.pause();
                } else if (!player.isPlaying()) {
                    Toast.makeText(this, "Already on pause", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    checks if package is installed,
    if true, open spotify app
    if false, notify user that app is not installed
     */
    public void openSpotify() {
        Intent openSpotify = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
        if (openSpotify == null) {
            Toast.makeText(this, "Spotify is not installed", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(openSpotify);
        }
    }
/*
abspeichern eines themes & den text vom playfavorite button ändern
 */
    public void saveFavorite(){
        if (actualTheme.mMusic != null){
            favoriteTheme = actualTheme;
            playFavoriteThemeButton.setText("Play " + favoriteTheme.getmTitle());
        }
        else {
            Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    abspielen der themes
     */
    public void playMusic(Themes obj) {
        if (player != null) {
            stopPlayer();
        }
        try {
            player = new MediaPlayer();
            player.setDataSource(obj.getmMusic());//url path das abgespielt werden soll
            player.prepare(); //vorbereitung bis der mediaplayer den sound abspielen kann
            player.start();

        } catch (IOException e) {
            Toast.makeText(MusicScreen.this, "Error", Toast.LENGTH_SHORT).show();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });

            player.setLooping(true);
            themeDescriptionView.setText(obj.getmDescription());
       }

    }



