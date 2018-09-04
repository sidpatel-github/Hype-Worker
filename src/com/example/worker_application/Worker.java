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
import android.widget.ListView;
import android.widget.Toast;

public class Worker extends Activity{
	ArrayList<product> p1 = new ArrayList<product>();
	ListView l1;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.worker_listview);
			
			Worker_login.uid = Worker_login.pref.getString("uid",null);
		//	Worker_login.workeruid= Worker_login.pref.getString("workeruid",null);
		    l1 = (ListView) findViewById(R.id.listView1);
			DoReg dr = new DoReg();
			dr.execute();
			
		}
/*
		@Override
		public void onBackPressed() {
			    new AlertDialog.Builder(this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("EXIT THIS LIST")
			        .setMessage("Are you sure you want to exit list of work?")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
			    {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			       /*     Intent i = new Intent(Worker.this,Worker_home.class);
			            startActivity(i); */
			      /*  	finish();

			        }

			    })
			    .setNegativeButton("No", null)
			    .show();
			}	*/
		
		 class DoReg extends AsyncTask<Void, Void, String>
		{
			
			private ProgressDialog pdia;
		
			protected String doInBackground(Void... params) 
			{
			try
			{			
			SoapObject request = new SoapObject("http://tempuri.org/","workerwork");
				
				request.addProperty("uid",Worker_login.uid);
				
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.dotNet=true;
		        envelope.setOutputSoapObject(request);
		        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
		        androidHttpTransport.call("http://tempuri.org/workerwork", envelope);
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
				pdia = new ProgressDialog(Worker.this);
				pdia.setCanceledOnTouchOutside(false);
				pdia.setMessage("processing...");
				pdia.show(); 	
			}
			
			protected void onPostExecute(String result) 
			{
				super.onPostExecute(result);				
				pdia.dismiss();						
				Log.d("lengthc",result.toString());
				if(result.toString().equals("false")) 	 	
				{
					Toast.makeText(Worker.this, "There is no any send hoarding", Toast.LENGTH_LONG).show();
				}
				else
				{
				String arr1[]=result.split(":");
				p1.clear();
				for(int i=0;i<arr1.length;i++)
					{
						Log.d("lengthc",String.valueOf(arr1.length));
						product p2 = new product();
						String arr2[]=arr1[i].split(",,");
						
						
						p2.w_hid=arr2[0];
						p2.w_uid=arr2[1];
						p2.w_btype=arr2[2];
						p2.w_size=arr2[3];
						p2.w_bookdate=arr2[4];
						p2.w_area=arr2[5];
						p2.w_image=arr2[6];
						p2.w_status=arr2[7];
						p2.w_remark=arr2[8];
						p2.w_locimage=arr2[9];
						
						p2.w_price=arr2[10];
						
						/*	p2.w_address=arr2[3];
						p2.w_area=arr2[3];
						p2.w_city=arr2[4];
						p2.w_state=arr2[5];*/
					
						p1.add(p2);
						//p2.equals(null);
					}
				
			Customadapter c1 = new Customadapter(p1, Worker.this);
			l1.setAdapter(c1);
			}
			}}
		}		
