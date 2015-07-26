package com.example.demo.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;
import com.example.demo.netutil.GetMailList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		init();
		
		new Thread(){

			@Override
			public void run() {
				GetMailList mailList = new GetMailList(MainActivity.this);
				String jsonString = mailList.getJsonString("getviewno");
				
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					JSONObject jsonObject = jsonArray.optJSONObject(0);
					
					Message msg = new Message();
					msg.what = 101;
					msg.arg1 = jsonObject.getInt("ViewNoReadNo");
					handler.sendMessage(msg);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
	}

	public void init() {
		findViewById(R.id.action).setVisibility(View.GONE);
		
		TextView tv;
		tv = (TextView)findViewById(R.id.actionbar_titletextview);
		tv.setText(R.string.mail_mymail);
		
		ImageView iv;
		iv = (ImageView)findViewById(R.id.actionbar_do1imageview);
		iv.setVisibility(View.VISIBLE);
		iv.setImageResource(R.drawable.test_edit);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CreateActivity.class);
				startActivity(intent);
			}
		});
		
		tv = (TextView) (findViewById(R.id.ReceBox).findViewById(R.id.test));
		tv.setText(R.string.mail_recevicebox);
		tv = (TextView) (findViewById(R.id.SendBox).findViewById(R.id.test));
		tv.setText(R.string.mail_sendbox);
		tv = (TextView) (findViewById(R.id.DraftsBox).findViewById(R.id.test));
		tv.setText(R.string.mail_draftsbox);
		tv = (TextView) (findViewById(R.id.GarbageBox).findViewById(R.id.test));
		tv.setText(R.string.mail_garbagebox);	
		
		((ImageView)findViewById(R.id.ReceBox).findViewById(R.id.img1)).setImageResource(R.drawable.test_recebox);
		((ImageView)findViewById(R.id.SendBox).findViewById(R.id.img1)).setImageResource(R.drawable.test_sendbox);
		((ImageView)findViewById(R.id.DraftsBox).findViewById(R.id.img1)).setImageResource(R.drawable.test_draft);
		((ImageView)findViewById(R.id.GarbageBox).findViewById(R.id.img1)).setImageResource(R.drawable.test_garbage);
		
		
		findViewById(R.id.ReceBox).setOnClickListener(new ButtonListener());
		findViewById(R.id.SendBox).setOnClickListener(new ButtonListener());
		findViewById(R.id.DraftsBox).setOnClickListener(new ButtonListener());
		findViewById(R.id.GarbageBox).setOnClickListener(new ButtonListener());
	}
	
	private class ButtonListener implements View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, BoxActivity.class);
			intent.putExtra("box", ((TextView)arg0.findViewById(R.id.test)).getText());
			startActivity(intent);
		}
	}
	
	@SuppressLint("HandlerLeak") 
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 101){
				TextView tv;
				tv = (TextView)(findViewById(R.id.ReceBox).findViewById(R.id.test_num));
				findViewById(R.id.test_num_wrapper).setVisibility(View.VISIBLE);
				tv.setText(msg.arg1+"");
			}
		}
	};

}
