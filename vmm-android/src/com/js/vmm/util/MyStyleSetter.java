package com.js.vmm.util;

import com.js.vmm.R;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class MyStyleSetter {

	public static ImageView setOnClickStyle(final ImageView imageView) {
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						imageView.setColorFilter(Color.parseColor("#77000000"));
						break;
					case MotionEvent.ACTION_UP:
						imageView.setColorFilter(null);
						break;
					default:
						break;
				}
				return false;
			}
		});
		return imageView;
	}
	
	public static Button setOnClickStyle(final Button button) {
		button.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						button.setBackgroundResource(R.drawable.shape_click);
						break;
					case MotionEvent.ACTION_UP:
						button.setBackgroundResource(R.drawable.shape);
						break;
					default:
						break;
				}
				return false;
			}
		});
		return button;
	}
}
