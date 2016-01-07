package com.example.musicplayer;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import com.example.musicplayer.DB.GetMusicInfo;
import com.example.musicplayer.Domain.LrcContent;
import com.example.musicplayer.Domain.MusicInfo;
import com.example.musicplayer.config.MyConfig;
import com.example.musicplayer.myview.LrcView;
import com.example.musicplayer.receive.SystemReceiver;
import com.example.musicplayer.server.LrcProcess;
import com.example.musicplayer.service.MusicPlayerService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.RootElement;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {

	private TextView tvMusicName;
	private TextView tvMusicInfo;
	private TextView tvTimeHead;
	private TextView tvTimeEnd;
	private LrcView mlrcView;
	private ImageButton myIbPlayStop;
	private ImageButton myIbBack;
	private ImageButton myIbnext;
	private SeekBar mySbProgress;

	private MusicInfo musicInfo2;
	private List<MusicInfo> musicInfos;
	private SystemReceiver systemReceiver;

	private String musicName;
	private String musicArtist;
	private String musicInfo;
	private int status;
	private String url;
	private int current;
	private int currentTime;
	private int modle = 1;
	private long myDuration;
	private Handler handler;
	private boolean myPause;
	private long myPosition = 0;
	private IncomeMsg incomeMsg;
	private boolean isContinue = true;
	private boolean isFromMain = true;
	private long myProgress;

	private LrcProcess mLrcProcess; // 歌词处理
	private List<LrcContent> lrcList = new ArrayList<LrcContent>(); // 存放歌词列表对象
	private int index = 0;
	private int lrcIndex;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		incomeMsg = new IncomeMsg();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.ron.tochange");
		registerReceiver(incomeMsg, intentFilter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_show);

		current = getIntent().getIntExtra("position", 0);
		status = getIntent().getIntExtra("status", 0);

		getView();
		initDate();
		initView();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					initDate();

					if (modle == 1 && !myPause) {

						Intent intentToContinue = new Intent();
						// intentToContinue.putExtra("progress", catchPosition);
						intentToContinue.putExtra("current", current);
						intentToContinue.putExtra("url", url);
						intentToContinue.putExtra("MSG", MyConfig.MUSIC_PALY);
						intentToContinue.setClass(PlayActivity.this,
								MusicPlayerService.class);
						startService(intentToContinue);

						Log.d("bug", "就这凑比");

						// Timer timer2Mark = new Timer();
						// timer2Mark.schedule(new TimerTask() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// current++;
						// }
						// }, myDuration);
						// initDate();//mark
						// initView();
						// handler.sendEmptyMessageDelayed(1, myDuration);
					} else if (!myPause) {

						Random rondom = new Random(GetMusicInfo.getMusicInfo(
								PlayActivity.this).size());
						current = rondom.nextInt();
						url = GetMusicInfo.getMusicInfo(PlayActivity.this)
								.get(current).getUrl();
						myDuration = GetMusicInfo
								.getMusicInfo(PlayActivity.this).get(current)
								.getDuration();
						Intent intentToContinue2 = new Intent();
						// intentToContinue2.putExtra("progress", myPosition);
						intentToContinue2.putExtra("url", url);
						intentToContinue2.putExtra("MSG", MyConfig.MUSIC_PALY);
						intentToContinue2.putExtra("current", current);
						intentToContinue2.setClass(PlayActivity.this,
								MusicPlayerService.class);
						startService(intentToContinue2);
						initView();
						// handler.sendEmptyMessageDelayed(1, myDuration);
					}
				}

			};
		};

		handler.sendEmptyMessage(1);

		/*
		 * 
		 * 
		 * if (modle == 1) { timer = new Timer(); timer.schedule(new TimerTask()
		 * {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub try
		 * { current++; Intent intentToContinue = new Intent(); url =
		 * GetMusicInfo.getMusicInfo(PlayActivity.this) .get(current).getUrl();
		 * intentToContinue.putExtra("url", url);
		 * intentToContinue.putExtra("MSG", MyConfig.MUSIC_PALY);
		 * intentToContinue.setClass(PlayActivity.this,
		 * MusicPlayerService.class); startService(intentToContinue); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); Toast.makeText(PlayActivity.this, "播完了",
		 * Toast.LENGTH_LONG).show(); } } }, myDuration); } else {
		 * 
		 * Random rondom = new Random(GetMusicInfo.getMusicInfo(
		 * PlayActivity.this).size()); current = rondom.nextInt(); url =
		 * GetMusicInfo.getMusicInfo(PlayActivity.this).get(current) .getUrl();
		 * Intent intentToContinue = new Intent();
		 * intentToContinue.putExtra("url", url);
		 * intentToContinue.putExtra("MSG", MyConfig.MUSIC_PALY);
		 * intentToContinue.setClass(PlayActivity.this,
		 * MusicPlayerService.class); startService(intentToContinue);
		 * 
		 * }
		 */

		myIbPlayStop.setBackgroundResource(R.drawable.music_stop2);

		myIbPlayStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (status == 1) {
					Intent intentToPause = new Intent();
					intentToPause.putExtra("url", url);
					intentToPause.putExtra("MSG", MyConfig.MUSIC_PAUSE);
					intentToPause.putExtra("current", current);
					intentToPause.setClass(PlayActivity.this,
							MusicPlayerService.class);
					status = 0;
					myIbPlayStop.setBackgroundResource(R.drawable.music_play1);
					startService(intentToPause);
				} else {
					Intent intentToResume = new Intent();
					intentToResume.putExtra("url", url);
					intentToResume.putExtra("MSG", MyConfig.MUSIC_RESUME);
					intentToResume.putExtra("current", current);
					intentToResume.setClass(PlayActivity.this,
							MusicPlayerService.class);

					status = 1;

					myIbPlayStop.setBackgroundResource(R.drawable.music_stop2);
					startService(intentToResume);
				}
			}
		});

		myIbBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					musicInfos = GetMusicInfo.getMusicInfo(PlayActivity.this);
					current = current - 1;
					musicInfo2 = musicInfos.get(current);
					String urlToNext = musicInfo2.getUrl();
					long currentDuration = musicInfo2.getDuration();
					String currentTitle = musicInfo2.getTitle();
					tvMusicName.setText(currentTitle);
					Intent intentToNext = new Intent();
					intentToNext.putExtra("url", urlToNext);
					intentToNext.putExtra("MSG", MyConfig.MUSIC_PALY);
					intentToNext.putExtra("current", current);
					intentToNext.setClass(PlayActivity.this,
							MusicPlayerService.class);
					startService(intentToNext);
					// Timer timer = new Timer();
					// timer.schedule(new TimerTask() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// handler.sendEmptyMessage(1);
					// }
					// }, currentDuration);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(PlayActivity.this, "并没有上一首",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		myIbnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					musicInfos = GetMusicInfo.getMusicInfo(PlayActivity.this);
					current = current + 1;
					musicInfo2 = musicInfos.get(current);
					String urlToNext = musicInfo2.getUrl();
					long currentDuration2 = musicInfo2.getDuration();
					String currentTitle = musicInfo2.getTitle();
					tvMusicName.setText(currentTitle);
					Intent intentToNext = new Intent();
					intentToNext.putExtra("url", urlToNext);
					intentToNext.putExtra("MSG", MyConfig.MUSIC_PALY);
					intentToNext.putExtra("current", current);
					intentToNext.setClass(PlayActivity.this,
							MusicPlayerService.class);
					startService(intentToNext);

					// Timer timerCount = new Timer();
					// timerCount.schedule(new TimerTask() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// handler.sendEmptyMessage(1);
					// }
					// }, currentDuration2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(PlayActivity.this, "并没有下一首",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		this.findViewById(R.id.bt_follow).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						modle = 1;
					}
				});

		this.findViewById(R.id.bt_rondom).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						modle = 0;
					}
				});

		// mySbProgress.setMax((int) myDuration);

		mySbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				tvTimeHead.setText(GetMusicInfo.formatTime(progress));
				tvTimeEnd.setText(GetMusicInfo
						.formatTime(myDuration - progress));

				myProgress = progress;

				if (fromUser) {
					initDate();
					initView();

					Log.d("progress", String.valueOf(progress));

					Intent intentToSeek = new Intent();
					intentToSeek.putExtra("progress", progress);
					intentToSeek.putExtra("url", url);
					intentToSeek.putExtra("current", current);
					if (myPause) {
						intentToSeek.putExtra("MSG", MyConfig.MUSIC_PAUSE);
					} else {
						intentToSeek.putExtra("MSG", MyConfig.MUSIC_PROGRESS);
					}
					intentToSeek.setClass(PlayActivity.this,
							MusicPlayerService.class);

					startService(intentToSeek);
					// isContinue = false;

					// int myTime = (int) (myDuration - progress);

					// if (!myPause) {
					// Timer myTimer = new Timer();
					//
					// myTimer.schedule(new TimerTask() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					//
					// isContinue = true;
					// handler.sendEmptyMessage(1);
					//
					// }
					// }, myTime);
					//
					// }
				}

				// try {
				// myDuration =
				// GetMusicInfo.getMusicInfo(PlayActivity.this).get(current).getDuration();
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// if(isContinue && progress==seekBar.getMax()){
				//
				// current++;
				// try {
				// myDuration =
				// GetMusicInfo.getMusicInfo(PlayActivity.this).get(current).getDuration();
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// handler.sendEmptyMessage(1);
				// Log.d("progress", "this is bug");
				// }
			}

		});

	}

	public void getView() {
		mySbProgress = (SeekBar) this.findViewById(R.id.sb_control);
		myIbBack = (ImageButton) this.findViewById(R.id.bt_back);
		myIbnext = (ImageButton) this.findViewById(R.id.bt_next);
		myIbPlayStop = (ImageButton) this.findViewById(R.id.bt_play_and_stop);
		tvMusicName = (TextView) this.findViewById(R.id.tv_music_name);
		tvTimeHead = (TextView) this.findViewById(R.id.tv_time_head);
		tvTimeEnd = (TextView) this.findViewById(R.id.tv_time_end);
		mlrcView = (LrcView) this.findViewById(R.id.lrcShowView);
	}

	public void initDate() {

		try {
			url = GetMusicInfo.getMusicInfo(PlayActivity.this).get(current)
					.getUrl();
			myDuration = GetMusicInfo.getMusicInfo(PlayActivity.this)
					.get(current).getDuration();
			musicName = GetMusicInfo.getMusicInfo(PlayActivity.this)
					.get(current).getTitle();
			musicArtist = GetMusicInfo.getMusicInfo(PlayActivity.this)
					.get(current).getArtist();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initView() {
		mySbProgress.setMax((int) myDuration);
		tvMusicName.setText(musicName);
	}

	public void initLrc() {
		mLrcProcess = new LrcProcess();
		// 读取歌词文件
		mLrcProcess.readLRC(GetMusicInfo.getMusicInfo(PlayActivity.this)
				.get(current).getUrl());
		// 传回处理后的歌词文件
		lrcList = mLrcProcess.getLrcList();
		
		mlrcView.setmLrcList(lrcList);
		// 切换带动画显示歌词
		// PlayActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(MusicPlayerService.this,R.anim.alpha_z));
		handler.post(mRunnable);
	}

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			mlrcView.setIndex(lrcIndex);
			mlrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(incomeMsg);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}

	public class IncomeMsg extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			if (action.equals("com.ron.tochange")) {
				currentTime = intent.getIntExtra("currentTime", -1);
				myPause = intent.getBooleanExtra("isPause", false);
				lrcIndex = intent.getIntExtra("lrcIndex", 0);
				mySbProgress.setProgress(currentTime);
				initDate();
				initView();
				if (myProgress == mySbProgress.getMax()) {
					current++;
					handler.sendEmptyMessage(1);
				}

			}
		}

	}
}
