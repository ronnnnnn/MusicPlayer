package com.example.musicplayer.myview;

import java.util.ArrayList;
import java.util.List;

import com.example.musicplayer.Domain.LrcContent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LrcView extends TextView {

	private float height;
	private float width;
	private float textHeight = 25;
	private float textSize = 18;
	private int index;
	private Paint currentPaint;
	private Paint notCurrentPaint;
	
	private List<LrcContent> mLrcList = new ArrayList<LrcContent>();
	
	public LrcView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public void setmLrcList(List<LrcContent> mLrcList) {
		this.mLrcList = mLrcList;
	}
	
	public LrcView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    public LrcView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }

	private void init() {
		// TODO Auto-generated method stub
		setFocusable(true);
		
		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setTextAlign(Paint.Align.CENTER);
		
		notCurrentPaint = new Paint();
		notCurrentPaint.setAntiAlias(true);
		notCurrentPaint.setTextAlign(Paint.Align.CENTER);
	}  

	  @Override  
	    protected void onDraw(Canvas canvas) {  
	        super.onDraw(canvas);  
	        if(canvas == null) {  
	            return;  
	        }  
	          
	        currentPaint.setColor(Color.argb(210, 251, 248, 29));  
	        notCurrentPaint.setColor(Color.argb(140, 255, 255, 255));  
	          
	        currentPaint.setTextSize(24);  
	        currentPaint.setTypeface(Typeface.SERIF);  
	          
	        notCurrentPaint.setTextSize(textSize);  
	        notCurrentPaint.setTypeface(Typeface.DEFAULT);  
	          
	        try {  
	            setText("");  
	            canvas.drawText(mLrcList.get(index).getLrcStr(), width / 2, height / 2, currentPaint);  
	              
	            float tempY = height / 2;  
	            //画出本句之前的句子  
	            for(int i = index - 1; i >= 0; i--) {  
	                //向上推移  
	                tempY = tempY - textHeight;  
	                canvas.drawText(mLrcList.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);  
	            }  
	            tempY = height / 2;  
	            //画出本句之后的句子  
	            for(int i = index + 1; i < mLrcList.size(); i++) {  
	                //往下推移  
	                tempY = tempY + textHeight;  
	                canvas.drawText(mLrcList.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);  
	            }   
	        } catch (Exception e) {  
	            setText("...木有歌词文件，赶紧去下载...");  
	        }  
	    }  
	  
	    /** 
	     * 当view大小改变的时候调用的方法 
	     */  
	    @Override  
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	        this.width = w;  
	        this.height = h;  
	    }  
	  
	    public void setIndex(int index) {  
	        this.index = index;  
	    }  
}
