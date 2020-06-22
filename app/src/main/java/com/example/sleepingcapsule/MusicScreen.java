package com.example.sleepingcapsule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
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

public class MusicScreen<soundpool> extends AppCompatActivity implements  Button.OnClickListener {

    private ImageView seatIcon;
    private ImageView lightIcon;
    private ImageView musicIcon;
    private Context mContext;
    private String currentScreen = "music";


    Client apiMusicClient;

    ArrayList<Themes> themelist = new ArrayList<Themes>();



    /*
    ImageButton beachbutton;
    ImageButton rainbutton;
    ImageButton forrestbutton;
    ImageButton junglebutton;
    ImageButton fireplacebutton;
    ImageButton mountainbutton;
     */
    private TextView themeDescriptionView;

    private ImageButton spotifyButton;
    private Button playFavoriteThemeButton;
    private Button saveFavoriteThemeButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton musicLibraryButton;
    private ImageButton stopchairButton;

    SoundPool soundpool;
    int beachsound, feuersound, rainsound, forrestsound, junglesound, mountainsound;

    Themes actualTheme;
    Themes favoriteTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundpool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        beachsound = soundpool.load(this, R.raw.beach, 1);
        feuersound = soundpool.load(this, R.raw.fireplace, 1);
        rainsound = soundpool.load(this, R.raw.rain, 1);
        forrestsound = soundpool.load(this, R.raw.forest, 1);
        junglesound = soundpool.load(this, R.raw.jungle, 1);
        mountainsound = soundpool.load(this, R.raw.mountain, 1);

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
        spotifyButton = findViewById(R.id.spotifyButton);
        spotifyButton.setOnClickListener(this);

        //zuordnung
        themeDescriptionView =  findViewById(R.id.textDescriptionView);


        playButton=findViewById(R.id.playbutton);
        pauseButton = findViewById(R.id.pausebutton);
        musicLibraryButton=findViewById(R.id.musicLibraryButton);


        saveFavoriteThemeButton = findViewById(R.id.saveFovriteThemeButton);
        playFavoriteThemeButton = findViewById(R.id.playFavoriteThemeButton);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        musicLibraryButton.setOnClickListener(this);

        saveFavoriteThemeButton.setOnClickListener(this);
        playFavoriteThemeButton.setOnClickListener(this);





        actualTheme = new Themes(); //create theme for saving
        favoriteTheme = new Themes();
        apiMusicClient = new Client();
       // beachbutton = (ImageButton) findViewById(R.id.strand_button);
       // rainbutton = (ImageButton) findViewById(R.id.regen_button);
       //forrestbutton = (ImageButton) findViewById(R.id.wald_button);
       // mountainbutton = (ImageButton) findViewById(R.id.berg_button);
       // junglebutton = (ImageButton) findViewById(R.id.dschungel_button);
        //fireplacebutton = (ImageButton) findViewById(R.id.lagerfeuer_button);

        //onclicklistener
       /* beachbutton.setOnClickListener(this);
        rainbutton.setOnClickListener(this);
        forrestbutton.setOnClickListener(this);
        mountainbutton.setOnClickListener(this);
        junglebutton.setOnClickListener(this);
        fireplacebutton.setOnClickListener(this);
*/



        MainActivity.setTaskBarIcon(musicIcon, currentScreen);
        //

        final ListView musicListView= findViewById(R.id.listview);
        ThemesListAdapter adapter = new ThemesListAdapter(this,themelist);
        musicListView.setAdapter(adapter);


        themelist.add( new Themes("Beach", "The sounds of waves", R.drawable.strand,beachsound));
        themelist.add( new Themes("Forrest", "Forrest sounds", R.drawable.wald, forrestsound));
        themelist.add( new Themes("Fireplace", "The sound of a fire", R.drawable.lagerfeuer,feuersound));
        themelist.add( new Themes("Jungle", "The sounds of animals in a jungle", R.drawable.dschungel, junglesound));
        themelist.add( new Themes("Rain", "The sound of heavy rain", R.drawable.regen, rainsound));
        themelist.add( new Themes("Mountain", "The sound of the nature", R.drawable.berg, mountainsound));


        //load extra themes from server if needed.
        //loadDataFromServer();


        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter , View view, int position, long l) {
                actualTheme = themelist.get(position);

                themeDescriptionView.setText(actualTheme.getDescription());
                soundpool.play(actualTheme.getMusic(), 1, 1, 1, -1, 1);

            }

        });



    }

    public class Themes {
        public String mTitle;
        public String mDescription;
        public int mImage;
        public int mMusic;


        public Themes() {

        }


        public  Themes(String Title, String Description, int image, int music) {
            mTitle=Title;
            mDescription=Description;
            mImage=image;
            mMusic=music;

        }


        public String getTitle() {
            return mTitle;
        }



        public String getDescription() {
            return mDescription;
        }

        public int getImage() {
            return mImage;
        }

        public int getMusic() {
            return mMusic;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public void setmDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public void setmImage(int mImage) {
            this.mImage = mImage;
        }

        public void setmMusic(int mMusic) {
            this.mMusic = mMusic;
        }

    }


    //transfers data into our listview
public class ThemesListAdapter extends ArrayAdapter<Themes> {

    public ThemesListAdapter(@NonNull Context context,  @NonNull ArrayList<Themes> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView= inflater.inflate(R.layout.adapter_view_layout,parent,false);
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
                soundpool.autoResume();
                break;
            case R.id.pausebutton:
                soundpool.autoPause();
                break;

            case R.id.saveFovriteThemeButton:
                favoriteTheme = actualTheme;
                playFavoriteThemeButton.setText("Play "+favoriteTheme.getTitle());
                break;
            case R.id.playFavoriteThemeButton:
                playFavorite();
                break;
            case R.id.musicLibraryButton:
                Toast.makeText(this, "Unplug the AUX cable from the tablet and plug it into your device.", Toast.LENGTH_LONG).show();
            case R.id.stopChair_musicScreen_ID:
                apiMusicClient.stopChairGetRequest1();
                apiMusicClient.stopChairGetRequest2("setstopseating");
                apiMusicClient.stopChairGetRequest2("setstopfootrest");


             case R.id.spotifyButton:

                openSpotify();

               break;

          /*  case R.id.wald_button:
                themeview.setText("Forrest");
                soundpool.play(forrestsound, 1, 1, 0, -1, 1);
                break;*/
           /* case R.id.lagerfeuer_button:
                themeview.setText("Fireplace");
                soundpool.play(feuersound, 1, 1, 0, -1, 1);
                break;
            case R.id.dschungel_button:
                themeview.setText("Jungle");
                soundpool.play(junglesound, 1, 1, 0, -1, 1);
                break;
            case R.id.regen_button:
                themeview.setText("Rain");
                soundpool.play(rainsound, 1, 1, 0, -1, 1);
                break;
            case R.id.berg_button:
                themeview.setText("Mountain");
                soundpool.play(mountainsound, 1, 1, 0, -1, 1);
                break;
*/
        }

    }

    /*
    checks if package is installed,
    if true, open spotify app
    if false, notify user that app is not installed
     */
public void openSpotify(){
        Intent openSpotify = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
        if(openSpotify == null){
            Toast.makeText(this, "Spotify is not installed", Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(openSpotify);
        }
    }

public void playFavorite(){
    soundpool.play(favoriteTheme.getMusic(), 1, 1, 1, -1, 1);
    themeDescriptionView.setText(favoriteTheme.getDescription());
}

}
