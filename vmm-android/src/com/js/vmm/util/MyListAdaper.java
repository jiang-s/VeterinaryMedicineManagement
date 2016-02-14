package com.js.vmm.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.js.vmm.R;
import com.js.vmm.data.Record;

public class MyListAdaper extends ArrayAdapter<Record> {
	
	private LayoutInflater mInflater;
	private TextView mName;
	private TextView mDate;
	private ArrayList<Record> mRecordList;

	public MyListAdaper(Context context, int resource, List<Record> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		mRecordList = (ArrayList<Record>) objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.item_main, parent, false);
		}
		mName = (TextView) convertView.findViewById(R.id.id_item_name);
		mDate = (TextView) convertView.findViewById(R.id.id_item_date);
		
		mName.setText(mRecordList.get(position).getName()); 
		mDate.setText(mRecordList.get(position).getDate());
		
		return convertView;
	}
	
}
