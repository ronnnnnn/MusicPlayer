package com.example.musicplayer.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.musicplayer.Domain.LrcContent;

public class LrcProcess {

	private LrcContent lrcContent;
	private List<LrcContent> lrcList;
	
	


	public LrcProcess() {
		// TODO Auto-generated constructor stub
		lrcContent = new LrcContent();
		lrcList = new ArrayList<LrcContent>();
	}
	
	public String readLRC(String path){
		
		StringBuilder stringBuilder = new StringBuilder();
		File f = new File(path.replace(".mp3", ".krc"));
		
		try {
			
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while((s = br.readLine())!=null){
				s = s.replace("[", "");
				s = s.replace("]", "@");
				
				String splitLrcData[] = s.split("@");
				if(splitLrcData.length > 1){
					lrcContent.setLrcStr(splitLrcData[1]);
					int lrcTime = time2Str(splitLrcData[0]);
					lrcContent.setLrcTime(lrcTime);
					lrcList.add(lrcContent);
					lrcContent = new LrcContent();
				}
			}
			
		} catch (FileNotFoundException e) {  
            e.printStackTrace();  
            stringBuilder.append("ľ�и���ļ����Ͻ�ȥ���أ�...");  
        } catch (IOException e) {  
            e.printStackTrace();  
            stringBuilder.append("ľ�ж�ȡ�����Ŷ��");  
        }  
		
		return stringBuilder.toString();
	}
	
	public int time2Str(String timeStr) {  
        timeStr = timeStr.replace(":", ".");  
        timeStr = timeStr.replace(".", "@");  
          
        String timeData[] = timeStr.split("@"); //��ʱ��ָ����ַ�������  
          
        //������֡��벢ת��Ϊ����  
        int minute = Integer.parseInt(timeData[0]);  
        int second = Integer.parseInt(timeData[1]);  
        int millisecond = Integer.parseInt(timeData[2]);  
          
        //������һ������һ�е�ʱ��ת��Ϊ������  
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;  
        return currentTime;  
    }  
   
	public List<LrcContent> getLrcList() {
		return lrcList;
	}
}
