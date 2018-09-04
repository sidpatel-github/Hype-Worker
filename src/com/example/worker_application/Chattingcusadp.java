package com.example.worker_application;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.worker_application.Chatting.chaton;

public class Chattingcusadp extends BaseAdapter {
	public static String NAMESPACE = "http://tempuri.org/";
	public static String SOAP_ACTION = "http://tempuri.org/chatfetch";
	String METHOD_NAME = "chatfetch"; 
	Button button1;
	ArrayList<chaton> a2;
	Context c2;
	
	public static String hmsg,hsender,hreciever,hdate,htime,hseen;
	public static String url;
	
	 public Chattingcusadp() {
		// TODO Auto-generated constructor stub
	 }
	 public Chattingcusadp(ArrayList<chaton> a1,Context c1){
		a2=a1;
		c2=c1;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return a2.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub // TODO Auto-generated method stub
		 LayoutInflater inflater = (LayoutInflater) c2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 View single_row = inflater.inflate(R.layout.chattingcusadp, null,true);
				 TextView msgdesc= (TextView) single_row.findViewById(R.id.textView1);
				 TextView sender= (TextView) single_row.findViewById(R.id.textView2);
				 TextView reciever= (TextView) single_row.findViewById(R.id.textView3);
				 TextView date= (TextView) single_row.findViewById(R.id.textView4);
				 TextView time= (TextView) single_row.findViewById(R.id.textView5);
				 TextView seen= (TextView) single_row.findViewById(R.id.textView6);
				 reciever.setText(a2.get(position).reciever1);
				 sender.setText(a2.get(position).sender1);
				 date.setText(a2.get(position).date1);
				 time.setText(a2.get(position).time1);
				 seen.setText(a2.get(position).seen1);
				 Log.d("hello",reciever.toString());
				 
				 if(reciever.getText().toString().equals("admin"))
				 {
			     Log.d("hello4544",reciever.getText().toString());
				 msgdesc.setText(a2.get(position).msgstr1);
				 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				 params.setMargins(270,0,0,0);
				 msgdesc.setLayoutParams(params);				 
//				 msgdesc.setPadding(270,0, 0,0);
				 sender.setLayoutParams(params);
				 sender.setText("");
				 reciever.setText("");
				 }
				 else
				 { 
				 msgdesc.setText(a2.get(position).msgstr1);
				
				 sender.setText("");
				 reciever.setText("");
				 seen.setText("");
				 }
				
	 return single_row; 
}

}
