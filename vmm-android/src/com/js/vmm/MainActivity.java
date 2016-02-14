package com.js.vmm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.js.vmm.data.Dao;
import com.js.vmm.data.Record;
import com.js.vmm.util.MyHttpRequest;
import com.js.vmm.util.MyListAdaper;
import com.js.vmm.util.MyStyleSetter;
import com.js.vmm.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageView mCameraButton;
	private ImageView mAddButton;
	private ImageView mSettingButton;
	private ListView mListView;
	
	private ArrayList<Record> mRecordList;
	private Dao dao = new Dao();

	private SharedPreferences sharedPreferences;
	
	private MyListAdaper mListAdaper;
	
	private final static int DATA_REFRESH = 0x114;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case DATA_REFRESH:
				refreshData();
				break;
			default:
				break;
			}

		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
    
    private void refreshData() {
    	mRecordList = dao.getAll();
    	mListAdaper = new MyListAdaper(this, R.layout.item_main, mRecordList);
		mListView.setAdapter(mListAdaper);
    }

	private void initEvent() {
		// TODO Auto-generated method stub
		mAddButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableLayout inputForm = (TableLayout) getLayoutInflater().inflate(
						R.layout.dialog_input, null);
				final EditText etInput = (EditText) inputForm.findViewById(R.id.id_input_value);
				etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.js_icon)
					.setTitle("请输13位条形码")
					.setView(inputForm)
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							if(etInput.getText().toString().matches("\\d{13}")) {
								SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Bundle bundle = new Bundle();
								bundle.putString("result", etInput.getText().toString());
								bundle.putString("date", fmt.format(new Date()));
								startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtras(bundle));
		        			} else {
		        				Toast.makeText(MainActivity.this, "不符合规范", Toast.LENGTH_SHORT).show();
		        			}
						}			
					})
					.setNegativeButton("取消", null)
					.show();
			}
		});
		
		mCameraButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		mSettingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableLayout settingForm = (TableLayout) getLayoutInflater().inflate(
						R.layout.dialog_settings, null);
				final EditText etIp = (EditText) settingForm.findViewById(R.id.id_ip);
				etIp.setText(sharedPreferences.getString("ip", ""));
				final CheckBox cbLoadImg = (CheckBox) settingForm.findViewById(R.id.id_isLoadImg);
				cbLoadImg.setChecked(sharedPreferences.getBoolean("load", false));
				new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.js_icon)
					.setTitle("系统设置")
					.setView(settingForm)
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							if(etIp.getText().toString().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
								Editor editor = sharedPreferences.edit();
								editor.putString("ip", etIp.getText().toString());
								editor.putBoolean("load", cbLoadImg.isChecked());
								editor.commit();
		        				Toast.makeText(MainActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
		        			} else {
								Editor editor = sharedPreferences.edit();
								editor.putBoolean("load", cbLoadImg.isChecked());
								editor.commit();
		        				Toast.makeText(MainActivity.this, "ip地址不符合规范", Toast.LENGTH_SHORT).show();
		        			}
						}			
					})
					.setNegativeButton("取消", null)
					.show();
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int p = position;
				new AlertDialog.Builder(MainActivity.this)
				.setIcon(R.drawable.js_icon)
				.setMessage("确认删除？")
				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Record r = (Record) mListView.getItemAtPosition(p);
						dao.deleteData(r.getBarcode());
						mHandler.sendEmptyMessage(DATA_REFRESH);
					}			
				})
				.setNegativeButton("取消", null)
				.show();
				
				return true;
			}
		});  
		
		mListView.setOnItemClickListener(new OnItemClickListener() {  

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Record r = (Record) mListView.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putInt("num", r.getNum());
				startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtras(bundle));
			}  
        });  

	}

	private void initData() {
		// TODO Auto-generated method stub
		sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
		Dao.dbpath = this.getFilesDir().toString() + "/record.db3";
		Log.d("jiangshan", Dao.dbpath);
		mRecordList = dao.getAll();
		data2View();
		
		TableLayout ipForm = (TableLayout) getLayoutInflater().inflate(
				R.layout.dialog_config_ip, null);
		final EditText etIp = (EditText) ipForm.findViewById(R.id.id_ip);
		etIp.setText(sharedPreferences.getString("ip", ""));
//		etIp.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(MainActivity.this)
        	.setIcon(R.drawable.js_icon)
        	.setTitle("配置ip")
        	.setView(ipForm)
        	.setPositiveButton("确定", new DialogInterface.OnClickListener(){
        		@Override
        		public void onClick(DialogInterface dialog, int which)
        		{
        			if(etIp.getText().toString().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
        				Toast.makeText(MainActivity.this, "ip配置成功", Toast.LENGTH_SHORT).show();
        				Editor editor = sharedPreferences.edit();
						editor.putString("ip", etIp.getText().toString());
						editor.commit();
        				Toast.makeText(MainActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
        			} else {
        				Toast.makeText(MainActivity.this, "ip不符合规范", Toast.LENGTH_SHORT).show();
        			}        		}
        	})
        	.setNegativeButton("取消", null)
        	.show();
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
		mCameraButton = MyStyleSetter.setOnClickStyle(mCameraButton);
		mAddButton = (ImageView) findViewById(R.id.id_add);
		mAddButton = MyStyleSetter.setOnClickStyle(mAddButton);
		mSettingButton = (ImageView) findViewById(R.id.id_settings);
		mSettingButton = MyStyleSetter.setOnClickStyle(mSettingButton);
		mListView = (ListView) findViewById(R.id.id_list);
	}
}
