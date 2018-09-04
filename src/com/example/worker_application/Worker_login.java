package com.example.worker_application;

import java.net.InetAddress;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Worker_login extends Activity {
	public static String NAMESPACE = "http://tempuri.org/";
	public static String ip="192.168.0.101";
	public static String URL = "http://"+ip.toString()+"/WebSite5/WebService.asmx";
	public static String SOAP_ACTION = "http://tempuri.org/selectuser";
	public static String websiteURL = "http://"+ip.toString()+"/WebSite5/";
	public static String urlServer = "http://"+ip.toString()+"/WebSite5/fileupload.aspx";
	public static String workerimgaddress ="http://"+ip.toString()+"/WebSite5/Uploads/";
	
	public static String wr_username,wr_uid;

	public static String mypref="mypref";
	public static SharedPreferences pref,pref1;
	public Editor edit;	
	String METHOD_NAME = "selectuser"; 
	public static String workeruid,uid,uniqueuserid;
//	public static String workerimgaddress ="http://192.168.0.102/worker/worker_image/";
	EditText w_u,w_p;
	Button w_l;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worker_login);
		w_u=(EditText)findViewById(R.id.wr_username);
		w_p=(EditText)findViewById(R.id.wr_password);
        w_l=(Button)findViewById(R.id.wr_login);
        pref=getSharedPreferences(mypref, MODE_PRIVATE);
        edit=pref.edit();	
        String str=pref.getString("wr_username",wr_username);
        if(str!=null)
        {
        	Intent i1=new Intent(Worker_login.this,Worker_home.class);
			startActivity(i1);	
        }
     /*   else if(isNetworkConnected()==false)
		{
			Toast.makeText(Worker_login.this, "NOT CONNECTED TO NETWORK", 3000).show();	
		}	
		else if(isInternetAvailable()==false)
		{
			Toast.makeText(Worker_login.this, "NO INTERNET AVAILABLE", 3000).show();	
		}
		else
		{
	        */
        w_l.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DoReg dr = new DoReg();
				dr.execute();
				
			}
		});
	}
	
	public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(Worker_login.ip.toString()); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }
	
	
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		  return cm.getActiveNetworkInfo() != null;
		 }
	
	
	@Override
	public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("EXIT THIS APPLICATION")
		        .setMessage("Are you sure you want to exit login?")
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
	
	class DoReg extends AsyncTask<Void, Void, String>
	{
		private ProgressDialog pdia;
		protected String doInBackground(Void... params) 
		{
			try
			{			
				SoapObject request = new SoapObject("http://tempuri.org/","selectuser");
				request.addProperty("username",w_u.getText().toString());
				request.addProperty("password",w_p.getText().toString());				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;
				envelope.setOutputSoapObject(request);				
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);				
				androidHttpTransport.call("http://tempuri.org/selectuser", envelope);				
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
			pdia = new ProgressDialog(Worker_login.this);
			pdia.setCanceledOnTouchOutside(false);
			pdia.setMessage("Login...");
			pdia.show(); 
		}		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);				
			pdia.dismiss();				
			String data[]=result.split(",");
			if(data[0].equals("active"))
			{
				wr_username=w_u.getText().toString();
				edit.putString("wr_username",wr_username);
				edit.commit();
				
			uid= data[1];
			uniqueuserid = w_u.getText().toString();
			
			edit.putString("uid",uid.toString());
			edit.commit();
			Toast.makeText(Worker_login.this, "Login Done", Toast.LENGTH_SHORT).show();	
			finish();
			Intent i1 = new Intent(Worker_login.this,Worker_home.class); 
			startActivity(i1);
			}
			else if(data[0].equals("deactive"))
			{
			/*	uid= data[1];
				uniqueuserid = w_u.getText().toString();
				
				
				workeruid=uid.toString();
				edit.putString("workeruid",workeruid);
				edit.commit();
				*/
				Toast.makeText(Worker_login.this, "Login Done",Toast.LENGTH_SHORT).show();	
				finish();
				Intent i1 = new Intent(Worker_login.this,Worker_home.class); 
				startActivity(i1);
			}
			else
			{
			Toast.makeText(Worker_login.this, "Invalid Username or password",Toast.LENGTH_SHORT).show();	
			}
		}
	}
}






