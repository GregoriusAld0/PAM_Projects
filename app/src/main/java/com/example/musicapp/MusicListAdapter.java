package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<AudioModel> musicList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent, false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
        AudioModel musicData = musicList.get(position);
        holder.titletextView.setText(musicData.getTitle());

        if(MusicMediaPlayer.currentIndex == position) {
            holder.titletextView.setTextColor(Color.parseColor("#5a189a"));
        }
        else {
            holder.titletextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Navigate to other activity*/
                MusicMediaPlayer.getInstance().reset();
                MusicMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",musicList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titletextView;
        ImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titletextView = itemView.findViewById(R.id.musicTitleText);
            iconImageView = itemView.findViewById(R.id.iconView);
        }
    }
}
