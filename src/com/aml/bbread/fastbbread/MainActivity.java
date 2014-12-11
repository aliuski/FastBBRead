package com.aml.bbread.fastbbread;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements SelectColumn.SelectColumnListener {
	
	public static final String LINK_LIST = "linklist";
	WebView webView;
	String htmlresult;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText tv = (EditText) findViewById(R.id.editText1);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String wwwpage = preferences.getString("url","");
        if(!wwwpage.equalsIgnoreCase("")){
        	tv.setText(wwwpage);
        	loadPage(wwwpage);
        }else
        	tv.setText("http://");

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		webView.goBack();
        	}
      	});
        
        final EditText edittext = (EditText) findViewById(R.id.editText1);
        
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	if(edittext.getText().length() > 10)
            		loadPage(edittext.getText().toString());
        	}
      	});
        
        edittext.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(edittext.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                	if(edittext.getText().length() > 10)
                		loadPage(edittext.getText().toString());
                }
                return false;
            }
        });
    }

    private void loadPage(String input){
        webView = (WebView) findViewById(R.id.webView1);
        
        webView.setWebViewClient(new Callback());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        webView.loadUrl(input);
    }
    
    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,
        String url) {
            return (false);
        }
    }

    public void onFinishInputDialog(int column){
    	if(column == 0)
    		return;
		ArrayList<String> linklist = ReadLinkFromHtml.readLink(htmlresult, column, webView.getUrl());
		if(linklist.size() == 0)
			Toast.makeText(this, R.string.not_found, Toast.LENGTH_SHORT).show();
		else {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("url",webView.getUrl());
			editor.putInt("column",column);
			editor.apply();
			invalidateOptionsMenu();
			
		    Intent intent = new Intent(this, ShowBBResult.class);
		    intent.putStringArrayListExtra(LINK_LIST, linklist);
		    startActivity(intent);
		}
    }
    
    private void openColumnDialog(String htmlpage){
    	 FragmentManager fragmentManager = getSupportFragmentManager();
    	 SelectColumn inputNameDialog = new SelectColumn();
    	 inputNameDialog.setCancelable(false);
    	 inputNameDialog.setDialogTitle(htmlpage);
    	 inputNameDialog.show(fragmentManager, "input dialog");	
    }
    
	private class DownloadTextTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return ReadLinkFromHtml.DownloadText(urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			htmlresult = result;
	        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
	        int column = preferences.getInt("column",0);
			if(column == 0)
				openColumnDialog(result);
			else
				onFinishInputDialog(column);
		}
	}
  
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
	    MenuItem deleteItem = menu.findItem(R.id.gobb_www);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int column = preferences.getInt("column",0);
	    if(column > 0)
	    	deleteItem.setTitle(getResources().getString(R.string.gobb_www) + " ["+column+"]");
	    else
	    	deleteItem.setTitle(R.string.gobb_www);
	    deleteItem.setEnabled(true);

	    return super.onPrepareOptionsMenu(menu);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("column",0);
			editor.apply();
			Toast.makeText(this, R.string.clear_column, Toast.LENGTH_SHORT).show();
			invalidateOptionsMenu();
            return true;
        } else if (id == R.id.gobb_www) {
			if(webView != null)
	        	new DownloadTextTask().execute(webView.getUrl());
			return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
