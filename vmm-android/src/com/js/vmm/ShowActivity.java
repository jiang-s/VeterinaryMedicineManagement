package com.js.vmm;

import com.js.vmm.data.Dao;
import com.js.vmm.data.JsonDesc;
import com.js.vmm.data.Record;
import com.js.vmm.util.MyHttpRequest;
import com.js.vmm.util.MyStyleSetter;
import com.js.vmm.util.ParseJson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends Activity {
	// 数据存取
	private Dao dao = new Dao();
	// 对应该Activity显示的数据
	private Record mRecord;
	// 扫描的结果，即条形码的13位数字
	private String result;
	// 布局相关组件
	private ImageView mImg;
	private TextView mName;
	private TextView mBarCode;
	private TextView mDescription;
	private EditText mSpecification;
	private EditText mInPrice;
	private EditText mOutPrice;
	private Button mSaveButton;
	// 数据处理相关
	private static final String failMsg = "{\"msg\":\"数据不存在！\",\"status\":false}";
	private final static int DATA_LOADED = 0x110;
	private final static int DATA_SAVED = 0x111;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case DATA_LOADED:
				mProgressDialog.dismiss();
				// 绑定数据到View中
				data2View();
				break;
			case DATA_SAVED:
				String s = (String) msg.obj;
				if (s.equals("")) {
					Toast.makeText(ShowActivity.this, "网络异常", Toast.LENGTH_LONG)
							.show();
				} else {
					mRecord.setSynState(true); // 不加也行，下次从数据库读的时候就改了
					dao.updateSynByNum(mRecord.getBarcode());
					mSaveButton.setEnabled(false);
					mSaveButton.setText("已同步");
					mSaveButton.setBackgroundResource(R.drawable.shape_click);
					Toast.makeText(ShowActivity.this, s, Toast.LENGTH_LONG)
							.show();
				}
				break;
			default:
				break;
			}

		};
	};

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);

		initView();
		initData();
		initEvent();
	}

	protected void data2View() {
		// TODO Auto-generated method stub
		if (mRecord.getJsonDesc().equals(failMsg)) {
			Toast.makeText(ShowActivity.this, "数据不存在！", Toast.LENGTH_LONG)
					.show();
			return;
		}
		JsonDesc jsonDesc = ParseJson.getRecordFromJsonString(mRecord
				.getJsonDesc());
		boolean showImg = getSharedPreferences("settings", Context.MODE_PRIVATE)
				.getBoolean("load", false);
		// 设置图片
		if (showImg) {
			mImg.setImageResource(R.drawable.loading);
			final String path = "http://tnfs.tngou.net/image"
					+ jsonDesc.getImg();// "http://tnfs.tngou.net/img"
			new Thread(new Runnable() {

				@Override
				public void run() {

					final Bitmap bmp = MyHttpRequest.returnBitMap(path);
					// post() 特别关键，就是到UI主线程去更新图片
					mImg.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mImg.setImageBitmap(bmp);
						}
					});
				}
			}).start();
		}
		// 设置名称
		if (mRecord.getName() == null || mRecord.getName().equals("")) {
			mRecord.setName(jsonDesc.getName());
		}
		mName.setText(mRecord.getName());
		// 设置条形码
		mBarCode.setText(mRecord.getBarcode());
		// 设置规格、进价、出价
		mSpecification.setText(mRecord.getSpecification());
		mInPrice.setText(String.valueOf(((double) mRecord.getInprice()) / 100));
		mOutPrice
				.setText(String.valueOf(((double) mRecord.getOutprice()) / 100));
		// 设置描述
		mDescription.setText(jsonDesc.getFactory() + "\n"
				+ jsonDesc.getDescription());
		// 设置按钮
		Log.d("jiangshan", String.valueOf(mRecord.isSynState()));
		if (mRecord.isSynState()) {
			mSaveButton.setEnabled(false);
			mSaveButton.setText("已同步");
			mSaveButton.setBackgroundResource(R.drawable.shape_click);
		}
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		mImg = (ImageView) findViewById(R.id.result_image);
		mName = (TextView) findViewById(R.id.id_item_name);
		mBarCode = (TextView) findViewById(R.id.bar_code);
		mDescription = (TextView) findViewById(R.id.id_description);
		mSpecification = (EditText) findViewById(R.id.id_specification);
		mInPrice = (EditText) findViewById(R.id.id_inprice);
		mOutPrice = (EditText) findViewById(R.id.id_outprice);
		mSaveButton = (Button) findViewById(R.id.id_saveButton);
		mSaveButton = MyStyleSetter.setOnClickStyle(mSaveButton);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mRecord = new Record();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			if (extras.getInt("num")!=0) {
				int num = extras.getInt("num");
				mRecord = dao.getRecordByNum(num);
				Log.d("jiangshan", String.valueOf(mRecord.isSynState())+"111");
				data2View();
				return;
			}
			
			if (extras.getString("result") != null) {
				result = extras.getString("result");
				mRecord.setBarcode(result);
			}

			if (extras.getString("date") != null) {
				mRecord.setDate(extras.getString("date"));
			}

		}
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				String jsonResult = MyHttpRequest.sendGet(result);
				mRecord.setJsonDesc(jsonResult);
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(DATA_LOADED);
			}
		}).start();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		mSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mRecord.getJsonDesc().equals(failMsg)) {
					return;
				}
				
				if(mInPrice.getText().toString().matches("\\d{1,10}(\\.\\d{1,2})?$ ")) {
					Toast.makeText(ShowActivity.this, "进价格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mOutPrice.getText().toString().matches("\\d{1,10}(\\.\\d{1,2})?$ ")) {
					Toast.makeText(ShowActivity.this, "卖价格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mOutPrice.getText().toString()==null || mOutPrice.getText().toString().equals("")
						|| mInPrice.getText().toString()==null || mInPrice.getText().toString().equals("")) {
					return;
				}
				
				mRecord.setSpecification(mSpecification.getText().toString());
				double in = Double.parseDouble(mInPrice.getText().toString());
				mRecord.setInprice(((int) (in * 100)));
				double out = Double.parseDouble(mOutPrice.getText().toString());
				mRecord.setOutprice(((int) (out * 100)));

				// 保存数据到本地数据库
				int res = dao.insertData(mRecord);
				if (res != -1) {
					mRecord.setName(String.valueOf(res));
					Toast.makeText(ShowActivity.this, "数据已保存到本地",
							Toast.LENGTH_SHORT).show();
				}

				// 发送请求将数据保存到服务器
				final String ipPath = getSharedPreferences("settings",
						Context.MODE_PRIVATE).getString("ip", "");

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						String s = MyHttpRequest.sendPost(
								ParseJson.recordToJSON(mRecord), ipPath);
						// 通知Handler保存完成
						Message message = mHandler.obtainMessage(DATA_SAVED, s);
						message.sendToTarget();
					}
				}).start();
			}
		});
	}
}
