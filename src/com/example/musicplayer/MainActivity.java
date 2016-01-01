package com.example.musicplayer;

import java.util.List;

import com.example.musicplayer.DB.GetMusicInfo;
import com.example.musicplayer.Domain.MusicInfo;
import com.example.musicplayer.config.MyConfig;
import com.example.musicplayer.service.MusicPlayerService;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView musicListView;
	private SimpleAdapter simpleAdapter;
	private List<MusicInfo> musicInfos;
	private ImageButton buttonPlay;
	private TextView textViewTitle;
	private TextView textViewArtist;
	private int status = 0;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textViewArtist = (TextView) this.findViewById(R.id.tv_music_authentic);
		textViewTitle = (TextView) this.findViewById(R.id.tv_music_name);
		buttonPlay = (ImageButton) this.findViewById(R.id.bt_play_action);
		musicListView = (ListView) this.findViewById(R.id.lv_content);
		musicInfos = GetMusicInfo.getMusicInfo(MainActivity.this);
		setListViewAdapter(musicInfos);
		musicListView
				.setOnItemClickListener(new MusicListItemOnClickListener());
		buttonPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (status == 1) {
					Intent intent = new Intent();
					intent.putExtra("MSG", MyConfig.MUSIC_PALY);
					intent.setClass(MainActivity.this, MusicPlayerService.class);
					status = 0;
					buttonPlay.setBackgroundResource(R.drawable.music_play);
					stopService(intent);

				} else {
					Intent intent = new Intent();
					intent.putExtra("url", url);
					intent.putExtra("MSG", MyConfig.MUSIC_PALY);
					intent.setClass(MainActivity.this, MusicPlayerService.class);
					status = 1;
					buttonPlay.setBackgroundResource(R.drawable.music_stop);
					startService(intent);
				}
			}
		});

	}

	private class MusicListItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			if (musicInfos != null) {
				MusicInfo musicInfo = musicInfos.get(position);

				String title = musicInfo.getTitle();
				String artist = musicInfo.getArtist();
				Long duration = musicInfo.getDuration();
				url = musicInfo.getUrl();
				textViewArtist.setText(artist);
				textViewTitle.setText(title);
				buttonPlay.setBackgroundResource(R.drawable.music_stop);

				Intent intent = new Intent();
				intent.putExtra("url", url);
				intent.putExtra("MSG", MyConfig.MUSIC_PALY);
				intent.setClass(MainActivity.this, MusicPlayerService.class);

				status = 1;

				startService(intent);
				
				Intent intentActivity = new Intent();
				intentActivity.putExtra("title", title);
				intentActivity.putExtra("artist", artist);
				intentActivity.putExtra("duration", String.valueOf(duration));
				intentActivity.setClass(MainActivity.this, PlayActivity.class);
				startActivity(intentActivity);
			}

		}

	}

	public void setListViewAdapter(List<MusicInfo> musicInfos) {

		simpleAdapter = new SimpleAdapter(this,
				GetMusicInfo.getetMusicInfoByMap(musicInfos),
				R.layout.adapter_items, new String[] { "title", "artist",
						"duration" }, new int[] { R.id.tv_item_music_title,
						R.id.tv_item_music_artist, R.id.tv_item_music_size });
		musicListView.setAdapter(simpleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
