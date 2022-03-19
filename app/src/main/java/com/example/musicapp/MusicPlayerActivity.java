package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTextView,currenttimeTextView, totaltimeTextView;
    SeekBar seekBar;
    ImageView pausePlay, previousBtn, nextBtn, musicIcon;
    ArrayList<AudioModel> musicList;
    AudioModel currentMusic;
    MediaPlayer mediaPlayer = MusicMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTextView = findViewById(R.id.musicTitle);
        currenttimeTextView = findViewById(R.id.currentTime);
        totaltimeTextView = findViewById(R.id.totalTime);
        seekBar = findViewById(R.id.seekBar);
        pausePlay = findViewById(R.id.pauseMusic);
        previousBtn = findViewById(R.id.previousMusic);
        nextBtn = findViewById(R.id.nextMusic);
        musicIcon = findViewById(R.id.musicIconLarge);

        titleTextView.setSelected(true);

        musicList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currenttimeTextView.setText(convertDuration(mediaPlayer.getCurrentPosition()+""));

                    if (mediaPlayer.isPlaying()) {
                        pausePlay.setImageResource(R.drawable.ic_pause_music);
                    }
                    else {
                        pausePlay.setImageResource(R.drawable.ic_play_music);
                    }
                }
                new Handler().postDelayed(this, 100);

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*Set music as current music played when clicked*/
    void setResourcesWithMusic() {
        currentMusic = musicList.get(MusicMediaPlayer.currentIndex);

        titleTextView.setText(currentMusic.getTitle());

        totaltimeTextView.setText(convertDuration(currentMusic.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlayedMusic());
        previousBtn.setOnClickListener(v-> playPreviousMusic());
        nextBtn.setOnClickListener(v-> playNextMusic());

        playMusic();
    }
    /*Control for music (play next, play previous, play and stop)*/
    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentMusic.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playNextMusic() {
        if (MusicMediaPlayer.currentIndex == musicList.size()-1)
            return;
        MusicMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousMusic() {
        if (MusicMediaPlayer.currentIndex == 0)
            return;
        MusicMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void pausePlayedMusic() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();

    }

    public static String convertDuration(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.HOURS.toMinutes(1));

    }
}