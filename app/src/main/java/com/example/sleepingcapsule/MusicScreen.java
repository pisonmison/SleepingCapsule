package com.example.sleepingcapsule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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
import com.squareup.picasso.Target;

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

    private static final String URL_DATA = "url";

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

    //SoundPool soundpool;
    private MediaPlayer player;

    private boolean able2PlaySound = false;


    private Themes actualTheme;
    private Themes favoriteTheme;

    private ListView musicListView;
    private ThemesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);
        apiMusicClient = new Client();
        actualTheme = new Themes(); //create theme for saving
        favoriteTheme = new Themes();

        enableMusic();





        mQueue = Volley.newRequestQueue(this);

        String url = "https://www.mboxdrive.com/Dance%20(online-audio-converter.com).mp3"; // your URL here
/*
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
/*
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundpool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
*/

/*
*/
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






        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {


                actualTheme = themelist.get(position);


                if (player == null) {

                    try {
                        player = new MediaPlayer();
                        player.setDataSource(actualTheme.getmMusic());
                        player.prepare(); // might take long! (for buffering, etc)
                        player.start();
                    } catch (Exception e) {
                        Toast.makeText(MusicScreen.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }


                }
               else{
                    stopPlayer();
                    try {
                        player = new MediaPlayer();
                        player.setDataSource(actualTheme.getmMusic());
                        player.prepare(); // might take long! (for buffering, etc)
                        player.start();
                    } catch (Exception e) {
                        Toast.makeText(MusicScreen.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();

                    }
                });

                player.setLooping(true);
                //soundpool.play(actualTheme.getMusic(), 1, 1, 1, -1, 1);
                themeDescriptionView.setText(actualTheme.getmDescription());

            }
        });


    }

    private void enableMusic(){
        apiMusicClient.handleSSLHandshake();
        able2PlaySound = true;
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
            themeTitle.setText(currentTheme.getmTitle());

            ImageView themeImage = convertView.findViewById(R.id.themeimage);
            Picasso.get().load(currentTheme.getmImage()).into(themeImage);
            //themeImage.setImageResource(currentTheme.getmImage());

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
                if (player == null) {
                    Toast.makeText(this, "select a theme", Toast.LENGTH_SHORT).show();
                }
                if(!player.isPlaying()){
                    player.start();
                }
                break;
            case R.id.pausebutton:
                if (player.isPlaying()) {
                    player.pause();
                }
                break;
            case R.id.stopsoundbutton:
                Toast.makeText(this, "Stopped Sound not Implemented.", Toast.LENGTH_SHORT).show();
                //stopPlayer();
                break;
            case R.id.saveFovriteThemeButton:
                favoriteTheme = actualTheme;
                playFavoriteThemeButton.setText("Play " + favoriteTheme.getmTitle());
                break;
            case R.id.playFavoriteThemeButton:
                playFavorite();
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

    private void stopPlayer() {
        if(player!=null) {
            player.stop();
            player.release();
            player = null;
        }
        else{
            Toast.makeText(this, "No Themes loaded yet", Toast.LENGTH_SHORT).show();
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

    public void playFavorite() {
        //soundpool.play(favoriteTheme.getMusic(), 1, 1, 1, -1, 1);
        if(able2PlaySound){
            if (player == null) {
                try {
                    player = new MediaPlayer();
                    player.setDataSource(favoriteTheme.getmMusic());
                    player.prepare(); // might take long! (for buffering, etc)
                    player.start();
                } catch (IOException e) {
                    Toast.makeText(MusicScreen.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();
                    }
                });
            }
            else{
                stopPlayer();
                try {
                    player = new MediaPlayer();
                    player.setDataSource(favoriteTheme.getmMusic());
                    player.prepare(); // might take long! (for buffering, etc)
                    player.start();

                } catch (IOException e) {
                    Toast.makeText(MusicScreen.this, "Error",Toast.LENGTH_SHORT).show();
                }
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();

                    }
                });
            }

            player.setLooping(true);
            themeDescriptionView.setText(favoriteTheme.getmDescription());
        }
        else{

        }

    }

}