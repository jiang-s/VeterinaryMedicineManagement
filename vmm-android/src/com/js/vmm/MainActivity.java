package com.js.vmm;

import java.util.ArrayList;
import java.util.Arrays;

import com.js.vmm.data.Dao;
import com.js.vmm.data.Record;
import com.js.vmm.util.MyHttpRequest;
import com.js.vmm.util.MyListAdaper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageView mCameraButton;
	private ImageView mAddButton;
	private ListView mListView;
	
	private ArrayList<Record> mRecordList;
	private Dao dao = new Dao();
	
	private MyListAdaper mListAdaper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

	private void initEvent() {
		// TODO Auto-generated method stub
		mAddButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s = MyHttpRequest.sendGet("6905070601772");
				Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		Dao.dbpath = this.getFilesDir().toString() + "/record.db3";
		mRecordList = dao.getAll();
		data2View();
	}

	private void data2View() {
		// TODO Auto-generated method stub
		if (mRecordList == null) {
			Toast.makeText(this, "当前没有记录", Toast.LENGTH_SHORT).show();
			return;
		}

		mListAdaper = new MyListAdaper(this, R.layout.item_main, mRecordList);
		mListView.setAdapter(mListAdaper);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		mCameraButton = (ImageView) findViewById(R.id.id_camera);
		mAddButton = (ImageView) findViewById(R.id.id_add);
		mListView = (ListView) findViewById(R.id.id_list);
	}
}
