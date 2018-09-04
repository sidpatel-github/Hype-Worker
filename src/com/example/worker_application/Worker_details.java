package com.example.worker_application;

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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Worker_details extends Activity {
	
	public static String NAMESPACE = "http://tempuri.org/";
	public static String SOAP_ACTION = "http://tempuri.org/upremark";
	String METHOD_NAME = "upremark"; 

	EditText eremark;
	TextView tarea,tstatus,tremark;
	Button bupload,bmap,bremark;
	ImageView img;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worker_details);
		tarea = (TextView) findViewById(R.id.area);
		tstatus = (TextView) findViewById(R.id.status);
		eremark = (EditText) findViewById(R.id.remark);
		bremark = (Button) findViewById(R.id.remarkbutton);
		bupload = (Button) findViewById(R.id.ubutton);
		bmap=(Button)findViewById(R.id.umap);
		img =(ImageView) findViewById(R.id.imageView1);
		
		Worker_login.uid = Worker_login.pref.getString("uid",null);
		
		eremark.setText(Customadapter.wremark);
		tarea.setText(Customadapter.warea);
		tstatus.setText(Customadapter.wstatus);
		Picasso.with(Worker_details.this).load(Worker_login.workerimgaddress+Customadapter.wimage).resize(100,100).into(img);
		if (Customadapter.wstatus.equals("admin_verify") || Customadapter.wstatus.equals("completed") ||Customadapter.wstatus.equals("accepted") ) 
		{
			tstatus.setTextColor(Color.GREEN);
		} 
		
		else 
		{
			tstatus.setTextColor(Color.RED);
		}
		
		
		bremark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Customadapter.wstatus.equals("completed"))
				{
				Toast.makeText(Worker_details.this,"banner completed",2000).show();	
				}
				else if(!eremark.getText().toString().equals(""))
				{
				new DoRemark().execute();
				}
				else
				{
					Toast.makeText(Worker_details.this, "Remark is not given...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	
		bmap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent i1 = new Intent(Worker_details.this,Worker_map.class); 
				startActivity(i1);	
			}
		});
		
		bupload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Intent i2 = new Intent(Worker_details.this,Worker_photo.class); 
				startActivity(i2);	
				
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("EXIT THIS DETAIL")
		        .setMessage("Are you sure you want to exit things to do?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		          /* Intent i = new Intent(Worker_details.this,Worker.class);
		           startActivity(i);*/
		        	finish();

		        }

		    })
		    .setNegativeButton("No", null)
		    .show();
		}	
	
	class DoRemark extends AsyncTask<Void, Void, String>
	{
		
		private ProgressDialog pdia;
	
		protected String doInBackground(Void... params) 
		{
		try
		{			
		SoapObject request = new SoapObject("http://tempuri.org/","upremark");
			
			request.addProperty("uid",Worker_login.uid);
			request.addProperty("hid",Customadapter.whid);
			request.addProperty("remark",eremark.getText().toString());
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet=true;
	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
	        androidHttpTransport.call("http://tempuri.org/upremark", envelope);
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
			pdia = new ProgressDialog(Worker_details.this);
			pdia.setCanceledOnTouchOutside(false);
			pdia.setMessage("Remarking...");
			pdia.show(); 	
		}
		
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			eremark.setText("");
			Intent i2 = new Intent(Worker_details.this,Worker.class); 
			startActivity(i2);	
			pdia.dismiss();						
		}
		
	}	

}
