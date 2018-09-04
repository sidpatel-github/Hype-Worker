package com.example.worker_application;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class Worker_home extends Activity{
	public static String NAMESPACE = "http://tempuri.org/";
	
	//Button todo,chat;
	TextView t1;
	ImageButton todo,chat,log;
	public static String regId;
	final String SENDER_ID = "82716677818";
	
	private NotificationManager mgr=null;
	
	
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worker_home);
		t1=(TextView)findViewById(R.id.textView1);
		todo=(ImageButton)findViewById(R.id.imageButton1);
		chat=(ImageButton)findViewById(R.id.imageButton2);
		log = (ImageButton)findViewById(R.id.imageButton3);
		Worker_login.wr_username= Worker_login.pref.getString("wr_username",null);
		
		if(Worker_login.wr_username==null)
		{
			Intent i = new Intent(Worker_home.this,Worker_login.class);
			startActivity(i);
		}
		
		
		t1.setText("WELCOME " + Worker_login.wr_username);
	
		
		
		GCMRegistrar.checkDevice(Worker_home.this);
		GCMRegistrar.checkManifest(Worker_home.this);
		regId = GCMRegistrar.getRegistrationId(Worker_home.this);
		if (regId.equals("")) 
		{
		  GCMRegistrar.register(this, SENDER_ID);
		} 
		else
		{
		  Log.v("Push", regId);
		}
		new Doregid().execute();
		mgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mgr.cancel(1);
		
		
		
		todo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Worker_home.this,Worker.class);
				startActivity(i);
			}
		});
		
		
		chat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Worker_home.this,Chatting.class);
				startActivity(i);
			}
		});
		
		

		log.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences preferences = getSharedPreferences(Worker_login.mypref, 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.clear(); 
				editor.commit();
			/*	Intent i = new Intent(Worker_home.this,Worker_login.class);
				startActivity(i);
				*/
				finish();
			}
		});
		
		
	 }
		@Override
		public void onBackPressed() {
			    new AlertDialog.Builder(this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("EXIT THIS APPLICATION")
			        .setMessage("Are you sure you want to exit?")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
			    {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            finish();    
			        }

			    })
			    .setNegativeButton("No", null)
			    .show();
			}
		
		
		
	
	class Doregid extends AsyncTask<Void, Void, String>
	{
		private ProgressDialog pdia;
		protected String doInBackground(Void... params) 
		{
		try
		{			
		SoapObject request = new SoapObject("http://tempuri.org/","updateworkerdevice");
			request.addProperty("uid",Worker_login.uid);
			request.addProperty("deviceid",regId.toString());
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet=true;
	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
	        androidHttpTransport.call("http://tempuri.org/updateworkerdevice", envelope);
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
			pdia=new ProgressDialog(Worker_home.this);
			pdia.setCanceledOnTouchOutside(false);
			pdia.setMessage("loading......");
	        pdia.show(); 
		}
		
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			pdia.dismiss();	
		}
	}
	
	
}
