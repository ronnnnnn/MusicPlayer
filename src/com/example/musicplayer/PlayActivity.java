package com.example.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private TextView tvMusicName;
	private TextView tvMusicInfo;
	private String musicName;
	private String musicArtist;
	private String musicInfo;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.play_show);
    	
        tvMusicName = (TextView)this.findViewById(R.id.tv_music_name);
    	musicName = getIntent().getStringExtra("title");
    	musicArtist = getIntent().getStringExtra("artist");
    	musicInfo = musicName+""+"" +""+"" +musicArtist;
        tvMusicName.setText(musicName);
        
    }
}
