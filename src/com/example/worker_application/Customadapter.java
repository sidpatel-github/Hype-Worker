package com.example.worker_application;

import java.util.ArrayList;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Customadapter extends BaseAdapter{
public static String warea,wbookdate,wimage,wcity,waddress,wstate,wstatus,wremark,wimageloc,wprice;
public static String whid;
	ArrayList<product> a2;
	Context c2;
	
	public Customadapter() {
		// TODO Auto-generated constructor stub
	}
	
	public Customadapter(ArrayList<product> a1,Context c1) {
		// TODO Auto-generated constructor stub
		a2=a1;
		c2=c1;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return a2.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
				 LayoutInflater inflater = (LayoutInflater)c2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 View single_row = inflater.inflate(R.layout.listitem, null,true);
				 TextView type= (TextView) single_row.findViewById(R.id.banner_type);
				 TextView size= (TextView) single_row.findViewById(R.id.banner_size);
				 TextView bookdate= (TextView) single_row.findViewById(R.id.banner_bookdate);
				 TextView status = (TextView) single_row.findViewById(R.id.banner_status);
				 TextView wprice = (TextView) single_row.findViewById(R.id.textView1);
				 ImageView img =(ImageView)single_row.findViewById(R.id.imageView1);
				 Button details=(Button)single_row.findViewById(R.id.button);
				 type.setText(a2.get(position).w_btype);
				 size.setText(a2.get(position).w_size);
				 bookdate.setText(a2.get(position).w_bookdate);
				 status.setText(a2.get(position).w_status);	
				 wprice.setText(a2.get(position).w_price);	
				  
				 
				 Picasso.with(c2).load(Worker_login.workerimgaddress+a2.get(position).w_image).resize(100,100).into(img);
				 details.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							whid = a2.get(position).w_hid;
							warea = a2.get(position).w_area;
							wstatus = a2.get(position).w_status;
							wimage =a2.get(position).w_image;
							wremark =a2.get(position).w_remark;
							wimageloc =a2.get(position).w_locimage;
							
							
							Intent i = new Intent(c2,Worker_details.class);
							c2.startActivity(i);
						}
					});	
				 return single_row; 
	}
}
