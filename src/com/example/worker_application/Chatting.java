package com.example.worker_application;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Chatting extends Activity {
	
	public static String NAMESPACE = "http://tempuri.org/";
	EditText msg;
	Button send;
	ListView msgall;
	ArrayList<chaton> p1 = new ArrayList<chaton>();

	protected void onCreate(Bundle savedInstanceState) {
		Worker_login.uid = Worker_login.pref.getString("uid",null);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting);
		
		if( Worker_login.uid==null)
		{

			Intent i = new Intent(Chatting.this,Worker_login.class);
			startActivity(i); 	
			
		}
		else if (Worker_login.uid.equals(""))
				{

			Intent i = new Intent(Chatting.this,Worker_login.class);
			startActivity(i); 	
				}
		
		else
		{
			msg=(EditText)findViewById(R.id.editText1);
			send=(Button)findViewById(R.id.send);
			msgall=(ListView)findViewById(R.id.listView1);
			msgall.setDivider(null);
			
			new dochatfetch().execute();
			send.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(msg.getText().toString().equals(""))
					{
					Toast.makeText(Chatting.this,"msg empty", 2000).show();	
					}
					else
					{
					new dochat().execute();
					}
				}
				
			});
			
		}

			
	}
	
	@Override
	public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("EXIT THIS CHAT")
		        .setMessage("Are you sure you want to exit chatting?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		      /*      Intent i = new Intent(Chatting.this,Worker_home.class);
		            startActivity(i);*/
		        	finish();
		        }

		    })
		    .setNegativeButton("No", null)
		    .show();
		}	
	
	@Override
	public void onResume(){
	    super.onResume();
	    // put your code here...
		
		if( Worker_login.uid==null)
		{

			Intent i = new Intent(Chatting.this,Worker_login.class);
			startActivity(i); 	
			
		}
		else if (Worker_login.uid.equals(""))
				{

			Intent i = new Intent(Chatting.this,Worker_login.class);
			startActivity(i); 	
				}
		else
		{
	    new dochatfetch().execute();
		}
	}
	
	
	class chaton 
	{
		String sender1;
		String reciever1;
		String msgstr1;
		String date1;
		String time1;
		String seen1;
	}
	
		class dochat extends AsyncTask<Void, Void, String>
		{
			private ProgressDialog pdia;
		
			protected String doInBackground(Void... params) 
			{
			try
			{			
			SoapObject request = new SoapObject("http://tempuri.org/","chat");
		        request.addProperty("uid",Worker_login.uid);
				request.addProperty("msg1",msg.getText().toString());
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.dotNet=true;
		        envelope.setOutputSoapObject(request);
		        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
		        androidHttpTransport.call("http://tempuri.org/chat", envelope);
		        SoapPrimitive resultstr = (SoapPrimitive)envelope.getResponse();
				return resultstr.toString();								
			}
			catch(Exception e)
			{
				Log.e("error", e.toString());
				return e.toString();
			}		
			}
		
			protected void onPreExecute() 
			{
				super.onPreExecute();
				pdia = new ProgressDialog(Chatting.this);
				pdia.setCanceledOnTouchOutside(false);
				pdia.setMessage("data sending......");
				pdia.show(); 	
			}
			
			protected void onPostExecute(String result) 
			{	
				super.onPostExecute(result);
				msg.setText("");
				new dochatfetch().execute();
				pdia.dismiss();
	            
			}
		}
		
		class dochatfetch extends AsyncTask<Void, Void, String>
		{
			private ProgressDialog pdia;
		
			protected String doInBackground(Void... params) 
			{
			try
			{			
			SoapObject request = new SoapObject("http://tempuri.org/","chatfetch");
		        request.addProperty("uid",Worker_login.uid);
				//request.addProperty("msg",msg.getText().toString());
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.dotNet=true;
		        envelope.setOutputSoapObject(request);
		        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
		        androidHttpTransport.call("http://tempuri.org/chatfetch", envelope);
		        SoapPrimitive resultstr = (SoapPrimitive)envelope.getResponse();
				return resultstr.toString();								
			}
			catch(Exception e)
			{
				Log.e("error", e.toString());
				return e.toString();
			}		
			}
		
			protected void onPreExecute() 
			{
				super.onPreExecute();
				pdia = new ProgressDialog(Chatting.this);
				pdia.setCanceledOnTouchOutside(false);
				pdia.setMessage("data running......");
				pdia.show(); 	
			}
			
			protected void onPostExecute(String result) 
			{	
				super.onPostExecute(result);
							
				if(result.toString().equals("false"))
				{
					Toast.makeText(Chatting.this,"sorry no chat history", 2000).show();
				}
				else
				{
				Log.d("data",result.toString());
				//result=result.replace(" ","");
				//Log.d("data",result.toString());
				String rows[]=result.split("#");
				//Log.d("length",String.valueOf(rows.length));
				p1.clear();
				for(int i =0;i<rows.length;i++)
				{
				Log.d("lengthr",String.valueOf(rows.length));
				String cols[]=rows[i].split(",,");
				Log.d("lengthc",String.valueOf(cols.length));
				chaton cobj = new chaton();
				
				cobj.sender1=cols[0];
				cobj.reciever1=cols[1];
				cobj.msgstr1=cols[2];
				cobj.date1=cols[3];
				cobj.time1=cols[4];
				cobj.seen1=cols[5];
				p1.add(cobj);
				}
				
				Chattingcusadp c1 = new Chattingcusadp(p1,Chatting.this);
		        msgall.setAdapter(c1);
		        msgall.setSelection(rows.length);
				
		        c1.notifyDataSetChanged();
		        c1.notifyDataSetInvalidated();
				}
		        pdia.dismiss();
	            
			}
		}
	}


