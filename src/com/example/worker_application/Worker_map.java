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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Worker_map extends Activity{
	
	public static String NAMESPACE = "http://tempuri.org/";
	public static String SOAP_ACTION = "http://tempuri.org/workermap";
	String METHOD_NAME = "worker_map";
	public double latitude, longitude;
	WebView workerweb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worker_map);
		workerweb = (WebView) findViewById(R.id.webView1);
		workerweb.getSettings().setJavaScriptEnabled(true);
		new DoReg().execute();
	}
	
	@Override
	public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("EXIT THIS MAP")
		        .setMessage("Are you sure you want to exit map?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		           /* Intent i = new Intent(Worker_map.this,Worker_details.class);
		            startActivity(i);*/
		        	finish();

		        }

		    })
		    .setNegativeButton("No", null)
		    .show();
		}	
	
		class DoReg extends AsyncTask<Void, Void, String> {
			private ProgressDialog pdia;

			protected String doInBackground(Void... params) {
				try {
					SoapObject request = new SoapObject("http://tempuri.org/","workermap");
					//request.addProperty("username", Worker_login.uniqueuserid);
					request.addProperty("hid",Customadapter.whid);
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
					androidHttpTransport.call("http://tempuri.org/workermap",envelope);
					SoapPrimitive resultstr = (SoapPrimitive) envelope.getResponse();

					return resultstr.toString();

				} catch (Exception e) {
					Log.e("error", e.toString());
					return e.toString();
				}
			}

			protected void onPreExecute() {
				super.onPreExecute();
				pdia = new ProgressDialog(Worker_map.this);
				pdia.setCanceledOnTouchOutside(false);
				pdia.setMessage("opening map.....");
				pdia.show();
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				pdia.dismiss();
				if(result.toString().equals("false"))
				{}
				else{
				String data1[] = result.split("`");
				latitude = Double.parseDouble(data1[0]);
				longitude = Double.parseDouble(data1[1]);
				String uri = "http://maps.google.com?q=" + latitude + ","+ longitude;
				
				
				
				workerweb.setWebViewClient(new webactivity());
				workerweb.loadUrl(uri);
				}
				// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				// startActivity(intent);

			}

		}

		class webactivity extends WebViewClient {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

		}

}
