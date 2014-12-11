package com.aml.bbread.fastbbread;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class ReadLinkFromHtml {

	public static ArrayList<String> readLink(String everything, int readcolumn, String url){

		String urlstart = null;
		if(url != null){
			int i = url.lastIndexOf('/');
			int a = url.lastIndexOf('?');
			urlstart = ((i == -1) ? url :
				((a == -1) ? url.substring(0,url.lastIndexOf('/', i-1)) :
					url.substring(0,i)));
		}
		ArrayList<String> list = new ArrayList<String>();
		int dtcounter;
		int start;
		int end = 0;
		do{
			dtcounter = 0;
			start = everything.indexOf("<tr",end);
			if(start == -1)
				break;
			end = everything.indexOf("</tr>",start);
			String str = everything.substring(start+4, end);
	
		int tdstart;
		int tdend = 0;
		do{
			tdstart = str.indexOf("<td",tdend);
			if(tdstart == -1)
				break;
			tdend = str.indexOf("</td>",tdstart);
			dtcounter++;
			if(dtcounter == readcolumn){
			String tdstr = str.substring(tdstart, tdend);
	
		int astart;
		int aend = 0;
		do{
			astart = tdstr.indexOf("<a ",aend);
			if(astart == -1)
				break;
			aend = tdstr.indexOf("</a>",astart);
			String astr = tdstr.substring(astart, aend);
	
			int tmps = astr.indexOf("href=");

			if(urlstart != null){
				String addlink = astr.substring(tmps+((astr.charAt(tmps+6) == '.') ? 7 : 6),
						astr.indexOf("\"",tmps+6)).replaceAll("&amp;", "&");
				if(addlink.indexOf('\'') == -1){
					if(addlink.startsWith("http:"))
						list.add(addlink);
					else
						list.add(urlstart + addlink);
				}
			}else
				list.add(astr.substring(astr.lastIndexOf('>')+1));

		}while(true);
		}
		}while(true);
		}while(true);
		return list;
	}

	public static String DownloadText(String URL) {
		int BUFFER_SIZE = 2000;
		InputStream in = null;
		try {			
			in = OpenHttpGETConnection(URL);
		} catch (Exception e) {
			Log.d("Networking", e.getLocalizedMessage());
			return "";
		}

		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];
		try {
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			Log.d("Networking", e.getLocalizedMessage());
			return "";
		}
		return str;		
	}
    
	public static InputStream OpenHttpGETConnection(String url) {
		InputStream inputStream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
		} catch (Exception e) {
			Log.d("", e.getLocalizedMessage());
		}
		return inputStream;
	}    			
}
