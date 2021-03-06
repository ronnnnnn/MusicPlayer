package com.example.musicplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.musicplayer.PlayActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.DB.GetMusicInfo;
import com.example.musicplayer.Domain.LrcContent;
import com.example.musicplayer.config.MyConfig;
import com.example.musicplayer.myview.LrcView;
import com.example.musicplayer.server.LrcProcess;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;

public class MusicPlayerService extends Service {

	private String url;
	private int progress;
	private boolean isPause;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	
	private LrcProcess mLrcProcess; // 歌词处理
	private List<LrcContent> lrcList = new ArrayList<LrcContent>(); // 存放歌词列表对象
	private int index = 0;
	
	private int duration;
	//private int current;

	private int currentTime;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				if (mediaPlayer != null) {
					 synchronized (this){
					currentTime = mediaPlayer.getCurrentPosition();
					}
					Intent intent2 = new Intent();
					intent2.putExtra("currentTime", currentTime);
					intent2.putExtra("isPause", isPause);
					intent2.putExtra("lrcIndex", lrcIndex());
					intent2.setAction("com.ron.tochange");
					sendBroadcast(intent2);	
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
	};

	public class MyserviceCallBack extends Binder {

		public boolean getServiceCallBack() {
			return isPause;
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("draw","draw");
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
		progress = intent.getIntExtra("progress", 0);
		//current = intent.getIntExtra("current", 0);
		int msg = intent.getIntExtra("MSG", 0);

		if (msg == MyConfig.MUSIC_PALY) {
			play(0);
		}else if (msg == MyConfig.MUSIC_PAUSE) {
			pause();
			
		}else if (msg == MyConfig.MUSIC_STOP) {
			stop();
		}else if (msg == MyConfig.MUSIC_RESUME) {
			
			resume();
		}else if (msg == MyConfig.MUSIC_PROGRESS) {
			
			play(progress);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaPlayer != null) {
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
			
		handler.sendEmptyMessage(1);

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
		if (mediaPlayer != null) {
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
		
			mediaPlayer.pause();
			isPause = true;
			mediaPlayer.pause();
			Log.d("stopAction","excute");
					
	}
	
	private void resume(){
		if (isPause) {
			mediaPlayer.pause();
			isPause = false;
			int position = mediaPlayer.getCurrentPosition();
			play(position);
			Log.d("stopAction","excute");
		}
		
	}

	private final class PreparedListener implements OnPreparedListener {

		private int position;

		public PreparedListener(int position) {
			// TODO Auto-generated constructor stub

			this.position = position;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mediaPlayer.start();
			if (position > 0) {
				mediaPlayer.seekTo(position);
			}
		}

	}
	

	    
	    public int lrcIndex() {  
	        if(mediaPlayer.isPlaying()) {  
	            currentTime = mediaPlayer.getCurrentPosition();  
	            duration = mediaPlayer.getDuration();  
	        }  
	        if(currentTime < duration) {  
	            for (int i = 0; i < lrcList.size(); i++) {  
	                if (i < lrcList.size() - 1) {  
	                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {  
	                        index = i;  
	                    }  
	                    if (currentTime > lrcList.get(i).getLrcTime()  
	                            && currentTime < lrcList.get(i + 1).getLrcTime()) {  
	                        index = i;  
	                    }  
	                }  
	                if (i == lrcList.size() - 1  
	                        && currentTime > lrcList.get(i).getLrcTime()) {  
	                    index = i;  
	                }  
	            }  
	        }  
	        return index;  
	    }  
	

}
