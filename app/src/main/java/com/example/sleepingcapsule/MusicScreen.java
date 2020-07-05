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


import java.util.ArrayList;

public class MusicScreen extends AppCompatActivity implements  Button.OnClickListener {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "music";


    Client apiMusicClient;

    private static final String URL_DATA = "url";

    ArrayList<Themes> themelist = new ArrayList<Themes>();


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
    MediaPlayer player;



    int beachsound, feuersound, rainsound, forrestsound, junglesound, mountainsound;

    Themes actualTheme;
    Themes favoriteTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);

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
        beachsound = soundpool.load(this, R.raw.beach, 1);
        feuersound = soundpool.load(this, R.raw.fireplace, 1);
        rainsound = soundpool.load(this, R.raw.rain, 1);
        forrestsound = soundpool.load(this, R.raw.forest, 1);
        junglesound = soundpool.load(this, R.raw.jungle, 1);
        mountainsound = soundpool.load(this, R.raw.mountain, 1);
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


        actualTheme = new Themes(); //create theme for saving
        favoriteTheme = new Themes();
        apiMusicClient = new Client();

        //player.setLooping(true);


        MainActivity.setTaskBarIcon(musicIcon, currentScreen);


        final ListView musicListView = findViewById(R.id.listview);
        ThemesListAdapter adapter = new ThemesListAdapter(this, themelist);
        musicListView.setAdapter(adapter);

        Themes beach = new Themes("Beach","The sounds of waves",R.drawable.strand,R.raw.beach);
        Themes forrest = new Themes("Forrest", "Forrest sounds", R.drawable.wald, R.raw.forest);
        Themes fireplace = new Themes("Fireplace", "The sound of a fire", R.drawable.lagerfeuer,R.raw.fireplace);
        Themes jungle = new Themes("Jungle", "The sounds of animals in a jungle", R.drawable.dschungel, R.raw.jungle);
        Themes rain = new Themes("Rain", "The sound of heavy rain", R.drawable.regen, R.raw.rain);
        Themes mountain = new Themes("Mountain", "The sound of the nature", R.drawable.berg, R.raw.mountain);

        themelist.add(beach);
        themelist.add(forrest);
        themelist.add(fireplace);
        themelist.add(jungle);
        themelist.add(rain);
        themelist.add(mountain);


        //load extra themes from server if needed.
       // loadDataFromServer();


        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                actualTheme = themelist.get(position);

                if (player == null) {

                    player = MediaPlayer.create(MusicScreen.this, actualTheme.getMusic());
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlayer();

                        }
                    });

                }
               else{
                    stopPlayer();
                    player=MediaPlayer.create(MusicScreen.this, actualTheme.getMusic());
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlayer();

                        }
                    });
                }
                player.start();
                //soundpool.play(actualTheme.getMusic(), 1, 1, 1, -1, 1);
                themeDescriptionView.setText(actualTheme.getDescription());
            }
        });


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
            themeTitle.setText(currentTheme.getTitle());

            ImageView themeImage = convertView.findViewById(R.id.themeimage);
            themeImage.setImageResource(currentTheme.getImage());

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
                if (player== null ) {
                    Toast.makeText(this, "Select a theme on the left", Toast.LENGTH_SHORT);
                }
                    else{
                    player.start();
                }
                break;
            case R.id.pausebutton:
                if (player != null) {
                    player.pause();
                }
                //soundpool.autoPause();
                break;
            case R.id.stopsoundbutton:
                stopPlayer();
                break;
            case R.id.saveFovriteThemeButton:
                favoriteTheme = actualTheme;
                playFavoriteThemeButton.setText("Play " + favoriteTheme.getTitle());
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
        if (player == null) {
            player = MediaPlayer.create(this, favoriteTheme.getMusic());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        else{
            stopPlayer();
            player=MediaPlayer.create(MusicScreen.this, favoriteTheme.getMusic());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();

                }
            });
        }
        player.start();
        themeDescriptionView.setText(favoriteTheme.getDescription());

    }
/*
    private void loadDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("themes");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        LauncherActivity.ListItem item = new LauncherActivity.ListItem(object.getString("title"),object.getString("description"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

 */
}