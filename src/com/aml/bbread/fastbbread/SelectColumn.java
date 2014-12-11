package com.aml.bbread.fastbbread;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectColumn extends DialogFragment {
    Button btn;
    String htmlpage;
    
	public interface SelectColumnListener extends Serializable{
        void onFinishInputDialog(int column);
    }
	
    public SelectColumn() {
    }
        
    public void setDialogTitle(String htmlpage) {
    	this.htmlpage = htmlpage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    	final View viewf = inflater.inflate(R.layout.select_column, container);
    	
    	Spinner spinner = (Spinner)viewf.findViewById(R.id.spinner1);
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
    		public void onItemSelected(AdapterView parent, View view, int position, long id) {
    			if(position == 0)
    				return;
    			ArrayList<String> list = ReadLinkFromHtml.readLink(htmlpage, position, null);
    			String text = "";
    			for(int x=0 ; x<list.size() ; x++)
    				text += list.get(x) + "\n";
    			TextView tv = (TextView)viewf.findViewById(R.id.textView1);
    			tv.setText(text);
	        }
	        public void onNothingSelected(AdapterView arg0) {
	        }
    	});

        btn = (Button) viewf.findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
            	SelectColumnListener listener = (SelectColumnListener) getActivity();
            	int i = ((Spinner)viewf.findViewById(R.id.spinner1)).getSelectedItemPosition();
            	listener.onFinishInputDialog(i);
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getDialog().setTitle(getResources().getString(R.string.selectcolumn));
        
        return viewf;
    }
}
