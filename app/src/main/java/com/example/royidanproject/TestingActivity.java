package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.example.royidanproject.Adapters.SongAdapter;
import com.example.royidanproject.Custom.Song;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Services.MusicService;
import com.example.royidanproject.Services.MusicService2;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;
import com.example.royidanproject.Views.WifiView;

import static com.example.royidanproject.MainActivity.SP_NAME;

import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    AppDatabase db;
    SharedPreferences sp;


    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;
    Toolbar toolbar;
    ImageView ivPhoto;

    // TODO - move all of this bs to external class when finished

    private List<Song> songList;
    private MusicService2 musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private musicPlayerController playerController;
    private MusicController controller;
    Context context;

    private boolean paused=false, playbackPaused=false;

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    private class musicPlayerController implements MediaController.MediaPlayerControl {

        @Override
        public void pause() {
            musicSrv.pausePlayer();
        }

        @Override
        public void start() {
            musicSrv.go();
        }

        @Override
        public int getDuration() {
            if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
            else return 0;
        }

        @Override
        public int getCurrentPosition() {
            if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
            else return 0;
        }

        @Override
        public void seekTo(int pos) {
            musicSrv.seek(pos);
        }

        @Override
        public boolean isPlaying() {
            if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
            return false;
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return 0;
        }
    }

    public class MusicController extends MediaController {

        public MusicController(Context c){
            super(c);
        }

        public void hide(){}

    }

    private void setController(){
        //set the controller up
        controller = new MusicController(context);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }

            //play next
            private void playNext(){
                musicSrv.playNext();
                if(playbackPaused){
                    setController();
                    playbackPaused=false;
                }
                controller.show(0);
            }

        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }

            //play previous
            private void playPrev(){
                musicSrv.playPrev();
                if(playbackPaused){
                    setController();
                    playbackPaused=false;
                }
                controller.show(0);
            }

        });
        controller.setMediaPlayer(playerController);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    private void startMusic() {
        if(playIntent==null){
            playIntent = new Intent(this, MusicService2.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private List<Song> getSongList() {
        Song s1 = new Song(R.raw.music_elevator, "Masheu", "Misheu");
        Song s2 = new Song(3, "Masheu2", "Misheu2");
        List<Song> songs = new ArrayList<>();
        songs.add(s1);
        songs.add(s2);
        return songs;
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService2.MusicBinder binder = (MusicService2.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        db = AppDatabase.getInstance(this);
        sp = getSharedPreferences(SP_NAME, 0);

        songList = getSongList();

        ListView lvSongs = findViewById(R.id.lvSongs);

        SongAdapter songAdt = new SongAdapter(TestingActivity.this, songList);
        lvSongs.setAdapter(songAdt);

        context = this;

        playerController = new musicPlayerController();

        setController();

        startMusic();




        llRoot = findViewById(R.id.llRoot);

        toolbar = findViewById(R.id.toolbar);

        ToolbarManager toolbarManager = new ToolbarManager(TestingActivity.this, toolbar);
    }

}