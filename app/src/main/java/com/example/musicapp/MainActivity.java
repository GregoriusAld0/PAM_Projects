package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView noMusicText;
    RecyclerView recyclerView;
    ArrayList<AudioModel> musicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noMusicText = findViewById(R.id.noMusicText);
        recyclerView = findViewById(R.id.musicRecyclerView);

        if(checkPermission() == false) {
            requestPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" !=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel musicData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if(new File(musicData.getPath()).exists())
            musicList.add(musicData);
        }
        if(musicList.size() == 0) {
            noMusicText.setVisibility(View.VISIBLE);
        }
        else {
            /*Recycler View*/
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(musicList, getApplicationContext()));
        }

    }
    /*Function to check if permission already given or not*/
    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }
    /*Function to ask permission from user*/
    void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Read permission required!", Toast.LENGTH_SHORT).show();
        }
        else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(musicList,getApplicationContext()));
        }
    }
}