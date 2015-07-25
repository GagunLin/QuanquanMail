package com.example.demo.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;
import com.example.demo.netutil.SendMail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CreateActivity extends Activity{
	
	private final static int SUCCESS=1;
	private final static int ERROR=250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create);
		
		init();
	}
	
	private void init(){
		findViewById(R.id.actionbar_backimageview).setVisibility(View.GONE);
		
		TextView tv;
		tv = (TextView)findViewById(R.id.actionbar_leftimageview);
		tv.setVisibility(View.VISIBLE);
		tv.setText("取消");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		tv = (TextView)findViewById(R.id.actionbar_titletextview);
		tv.setText("写邮件");
		
		tv = (TextView)findViewById(R.id.actionbar_rigthtextview);
		tv.setVisibility(View.VISIBLE);
		tv.setText("发送");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final EditText receiver = (EditText)findViewById(R.id.create_receiver);
				final EditText title = (EditText)findViewById(R.id.create_title);
				final EditText content = (EditText)findViewById(R.id.create_content);
				final CheckBox checkBox = (CheckBox)findViewById(R.id.create_reply);
				final String reply;
				if (checkBox.isChecked()) reply="1";
				else reply="0";
				
				new Thread(){

					@Override
					public void run() {
						SendMail sendMail = new SendMail(CreateActivity.this);
						String result = sendMail.send(receiver.getText().toString(), title.getText().toString(), content.getText().toString(),reply);
						Message msg = new Message();
						
						try {
							JSONArray jsonArray = new JSONArray(result);
							JSONObject jsonObject = jsonArray.optJSONObject(0);
							msg.what=SUCCESS;
							msg.obj=jsonObject.optString("result");							
							
						} catch (JSONException e) {
							e.printStackTrace();
							msg.what=ERROR;
							msg.obj=result;
							
						} finally{
							handler.sendMessage(msg);
						}
					}
					
				}.start();
			}
		});
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(CreateActivity.this);
			builder.setTitle("提示消息");
			builder.setMessage(msg.obj.toString());
			
			switch (msg.what){
				case SUCCESS:
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
						}
					});
					
					builder.show();	
					break;
					
				case ERROR:
					builder.setPositiveButton("确定", null);		
					builder.show();	
					break;
			}
		}
	};

}
