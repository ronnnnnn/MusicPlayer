package com.example.musicplayer.service;

import java.io.IOException;

import com.example.musicplayer.config.MyConfig;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MusicPlayerService extends Service {

	
	private String url;
	private  boolean isPause;
	private MediaPlayer mediaPlayer = new MediaPlayer();
    
	
    public class MyserviceCallBack extends Binder{
		
		public boolean getServiceCallBack(){
			return isPause;
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		MyserviceCallBack myserviceCallBack = new MyserviceCallBack();
		return myserviceCallBack;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		if (mediaPlayer.isPlaying()) {
            stop();
		}
		url = intent.getStringExtra("url");
		int msg = intent.getIntExtra("MSG", 0);

		if (msg == MyConfig.MUSIC_PALY) {
             play(0);
		}else if(msg == MyConfig.MUSIC_PAUSE){
			pause();
		}else if(msg == MyConfig.MUSIC_STOP){
			stop();
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}

	private void play(int position) {
       
        try { 
        	
        	mediaPlayer.reset();
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new PreparedListener(position));
			isPause = false;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stop() {
       if(mediaPlayer != null){
    	   mediaPlayer.stop();
    	   
    	   try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
	}

	private void pause() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
        	mediaPlayer.pause();
        	isPause = true;
        }
	}
	
	private final class PreparedListener implements OnPreparedListener{

		private int position;
		
		public PreparedListener(int position) {
			// TODO Auto-generated constructor stub
			
			this.position = position;
			
		}
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mediaPlayer.start();
			if(position > 0){
				mediaPlayer.seekTo(position);
			}
		}
		
	}

}
