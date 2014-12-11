package com.aml.bbread.fastbbread;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowBBResult extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showbb_main);

        TouchWebView webView = (TouchWebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new Callback());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        
        Intent intent = getIntent();
		final ArrayList<String> linklist = intent.getStringArrayListExtra(MainActivity.LINK_LIST);
		webView.setPages(linklist);
        webView.loadUrl(linklist.get(0));
    }
    
    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,
        String url) {
            return (false);
        }
    }
}
