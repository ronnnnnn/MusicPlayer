package com.example.musicplayer.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

import com.example.musicplayer.Domain.MusicInfo;

public class GetMusicInfo {
   
	public static List<MusicInfo> getMusicInfo(Context context){
		
	Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,null, null, null, Media.DEFAULT_SORT_ORDER);
	List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
	
	for (int i = 0; i < cursor.getCount(); i++) {
		MusicInfo musicInfo = new MusicInfo(); 
	
		cursor.moveToNext();
		
		long id = cursor.getLong(cursor.getColumnIndex(Media._ID));
		long duration = cursor.getLong(cursor.getColumnIndex(Media.DURATION));
		long size = cursor.getLong(cursor.getColumnIndex(Media.SIZE));
		String title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
		String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
		String url = cursor.getString(cursor.getColumnIndex(Media.DATA));
		int isMusic = cursor.getInt(cursor.getColumnIndex(Media.IS_MUSIC));
		
		
		if(isMusic!= 0){
			musicInfo.setArtist(artist);
			musicInfo.setDuration(duration);
			musicInfo.setId(id);
			musicInfo.setSize(size);
			musicInfo.setTitle(title);
			musicInfo.setUrl(url);
			musicInfos.add(musicInfo);
		}
	}
	
		return musicInfos;
		
	}
	
	public  static List<Map<String,String>> getetMusicInfoByMap(List<MusicInfo> musicInfos){
List<Map<String ,String>> musicList = new ArrayList<Map<String ,String>>();
    	
    	for(Iterator<MusicInfo> iterator =  musicInfos.iterator();iterator.hasNext();){
    		
    		MusicInfo musicInfo = (MusicInfo)iterator.next();
    		Map<String,String> map = new HashMap<String, String>();
    		
    		map.put("title", musicInfo.getTitle());
    		map.put("size",String.valueOf( musicInfo.getSize()));
    		map.put("artist", musicInfo.getArtist());
    		map.put("duration", formatTime(musicInfo.getDuration()));
    		map.put("id",String.valueOf( musicInfo.getId()));
    		map.put("url", musicInfo.getUrl());
    		
    		
    		musicList.add(map);
    		
    	}
		return musicList;
	} 
	
	 public static String formatTime(long time) {  
	        String min = time / (1000 * 60) + "";  
	        String sec = time % (1000 * 60) + "";  
	        if (min.length() < 2) {  
	            min = "0" + time / (1000 * 60) + "";  
	        } else {  
	            min = time / (1000 * 60) + "";  
	        }  
	        if (sec.length() == 4) {  
	            sec = "0" + (time % (1000 * 60)) + "";  
	        } else if (sec.length() == 3) {  
	            sec = "00" + (time % (1000 * 60)) + "";  
	        } else if (sec.length() == 2) {  
	            sec = "000" + (time % (1000 * 60)) + "";  
	        } else if (sec.length() == 1) {  
	            sec = "0000" + (time % (1000 * 60)) + "";  
	        }  
	        return min + ":" + sec.trim().substring(0, 2);  
	    }  
}
