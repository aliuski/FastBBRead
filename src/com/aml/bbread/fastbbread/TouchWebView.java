package com.aml.bbread.fastbbread;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TouchWebView extends WebView {
	
	int _xDelta = 0;
	int _yDelta = 0;
	boolean move = false;
	ArrayList<String> pages;
	int page = 0;
	
    public TouchWebView(Context context) {
        super(context);
    }

    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	public void setPages(ArrayList<String> pages){
		this.pages = pages;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event){

    	int X = (int) event.getRawX();
    	int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            _xDelta = X;
            _yDelta = Y;
            move = true;
            break;
        case MotionEvent.ACTION_MOVE:
        	if(move && pages != null){
        		int xdiff = Math.abs(X-_xDelta);
        		if((xdiff > 8) && (xdiff > Math.abs((Y-_yDelta)*2))){
	            	if(_xDelta > X)
	            		page++;
	            	else if(_xDelta < X)
	            		page--;
	            	if(page < 0)
	            		page = pages.size()-1;
	            	if(page >= pages.size())
	            		page = 0;
	            	showToast();
	            	loadUrl(pages.get(page));
        		}
            	move = false;
        	}
        }
        return super.onTouchEvent(event);
    }
	
    private void showToast(){
    	LinearLayout layout=new LinearLayout(getContext());
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.RED);
        tv.setTextSize(50);
        tv.setText((page+1)+"/"+pages.size());
        layout.addView(tv);
        Toast toast=new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
