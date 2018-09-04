package com.example.worker_application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;









import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;





import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler.Result;

import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Worker_photo extends Activity {

	Button wcamera,wgallery,wupload;
	ImageView img;
	Bitmap bmp=null;
	private String seimg, imgname;
	static final int REQUEST_IMAGE_CAPTURE = 2;
	private static final int SELECT_PICTURE = 1;
	
	@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.worker_photo);
	Worker_login.uid = Worker_login.pref.getString("uid",null);
	//wcamera=(Button)findViewById(R.id.camera);
	wgallery=(Button)findViewById(R.id.gallery);
	wupload=(Button)findViewById(R.id.upload);
	img=(ImageView)findViewById(R.id.image);
	
	Picasso.with(Worker_photo.this).load(Worker_login.workerimgaddress+Customadapter.wimageloc).resize(100,100).into(img);
	
	wgallery.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(Customadapter.wstatus.equals("completed") || Customadapter.wstatus.equals("expired") )
			{
				Toast.makeText(Worker_photo.this,"already completed",2000).show();
			}
			else
			{
			Intent intent = new Intent();
			intent.setType("image/*");
			Log.d("resultbook1", "sdad");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent,SELECT_PICTURE);
			}
			}
	});
  wupload.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(img.getDrawable()==null)
		{
			Toast.makeText(Worker_photo.this,"please choose image",2000).show();
		}
		else if(Customadapter.wstatus.equals("completed") || Customadapter.wstatus.equals("expired") )
		{
			Toast.makeText(Worker_photo.this,"already uploaded image",2000).show();
		}
		else
		{
			new douploaddata().execute();
		}
		
		
	}
  	});

	/*wcamera.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		Intent icamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(icamera,REQUEST_IMAGE_CAPTURE);
		wupload.setEnabled(true);
		}
	});*/
}
	
	
	@Override
	public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("EXIT THIS UPLOAD")
		        .setMessage("Are you sure you want to exit photo?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        /*   Intent i = new Intent(Worker_photo.this,Worker_details.class);
		           startActivity(i);*/ finish();
		        }

		    })
		    .setNegativeButton("No", null)
		    .show();
		}	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==SELECT_PICTURE){
			if (resultCode == Activity.RESULT_OK) {

				// result was ok
				InputStream input;
				try {
					input=getContentResolver().openInputStream(data.getData());
					bmp=BitmapFactory.decodeStream(input);
					img.setImageBitmap(bmp);
					bmp=null;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				seimg = getAbsolutePath(data.getData());
				File f1 = new File(seimg);
				imgname = f1.getName();
				Log.e("result1", "");

				Uri selectedImage = data.getData();			
				Intent i = new Intent(getApplicationContext(), ImageView.class);
				i.putExtra("PICTURE_LOCATION", selectedImage.toString());
			}
			}
		
			else if(requestCode== REQUEST_IMAGE_CAPTURE){
			if (resultCode == Activity.RESULT_OK){

				Bundle extras=data.getExtras();
				Bitmap photo=(Bitmap)extras.get("data");
				img.setImageBitmap(photo);	

				seimg = getAbsolutePath(data.getData());
				File f1 = new File(seimg);
				imgname = f1.getName();

				Uri selectedImage = data.getData();			
				Intent i = new Intent(getApplicationContext(), ImageView.class);
				i.putExtra("PICTURE_LOCATION", selectedImage.toString());
				
			}
			}
		
	}

	public String getAbsolutePath(Uri uri) {
		 
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			Log.d("resultbook4", "sdad");
			return cursor.getString(column_index);
		} else
			return null;
	}

	class douploaddata extends AsyncTask<Void, Void, String> {
		private ProgressDialog pdia;
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String FileName;

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		protected String doInBackground(Void... params) {
			try {
				FileInputStream fileInputStream = new FileInputStream(new File(seimg));

				URL url = new URL(Worker_login.urlServer);
				connection = (HttpURLConnection) url.openConnection();
				Log.d("resultbook5", "sdad");

				// Allow Inputs &amp; Outputs.
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Set HTTP method to POST.
				connection.setRequestMethod("POST");

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				Log.d("resultbook6", "sdad");
				outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""+ seimg + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				int serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();

				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
				return serverResponseMessage;

			} catch (Exception ex) {
				return (ex.getMessage());
				// Log.d("error", ex.getMessage());
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pdia = new ProgressDialog(Worker_photo.this);
			pdia.setCanceledOnTouchOutside(false);
			pdia.setMessage("Uploading in...");
			pdia.show();
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("resultbook7", result);
			//if(result.equals("ok"))
				
			pdia.setMessage("Photo upload Successfully");
			//Intent i1= new Intent(Worker_photo.this,Worker_details.class);
			//startActivity(i1);
			
			pdia.dismiss();
			new hoardingimage().execute();

		}
		
	}
	class hoardingimage extends AsyncTask<Void, Void, String>
	{
		private ProgressDialog pdia;
	
		protected String doInBackground(Void... params) 
		{
		try
		{			
		SoapObject request = new SoapObject("http://tempuri.org/","imghoarding");
			request.addProperty("hid",Customadapter.whid);
        	request.addProperty("image",imgname.toString());
        	request.addProperty("uid",Worker_login.uid);
	        //Log.d("uid",Login.uid + btype.getSelectedItem().toString() + bsize.getSelectedItem().toString() + bdate.getText().toString() + edate.getText().toString() + area.getSelectedItem().toString() + city.getSelectedItem().toString() + state.getSelectedItem().toString() + imgname.toString() + price.toString() );
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet=true;
	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(Worker_login.URL);
	        androidHttpTransport.call("http://tempuri.org/imghoarding", envelope);
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
			pdia = new ProgressDialog(Worker_photo.this);
			pdia.setCanceledOnTouchOutside(false);
			pdia.setMessage("uploading...");
			pdia.show(); 	
		}
		
		protected void onPostExecute(String result) 
		{	
			super.onPostExecute(result);	
			Log.d("rerer8",result);
			pdia.dismiss();		
			Log.d("resultbook11", result);
			finish();
		/*	Intent i1= new Intent(Worker_photo.this,Worker_details.class);
			startActivity(i1);
			*/
		}
	}
	

}