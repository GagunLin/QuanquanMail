package com.example.demo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;
import com.example.demo.client.PullToRefreshLayout.RefreshListener;
import com.example.demo.netutil.GetMailList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BoxActivity extends Activity{
	
	private Button[] button;
	private ListView listView;
	private List<HashMap<String, Object>> list;
	private ArrayList<String> eu_id;
	private String action;
	private PullToRefreshLayout pullToRefreshLayout;
	
	private final static int NO_MAIL=0;
	private final static int SUCCESS=1;
	private final static int ERROR=250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_box);	
		
		init();
		MyThread thread = new MyThread();
		thread.start();
		
	}
	
	private void init(){
		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshlayout);
		pullToRefreshLayout.setListView((ListView) findViewById(R.id.box_list));
		
		pullToRefreshLayout.setonRefreshListener(new RefreshListener(){

			@Override
			public void refresh() {
				list.clear();
				eu_id.clear();
				MyThread thread2 = new MyThread();
				thread2.start();
			}
			
		});
		
		
		findViewById(R.id.action).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		TextView tv;
		tv = (TextView)findViewById(R.id.actionbar_leftimageview);
		tv.setVisibility(View.VISIBLE);
		tv.setText(R.string.mail_mymail);
		
		Intent intent = getIntent();
		final String box = intent.getStringExtra("box");
		if (box.equals("收件箱")) action = "getviewmail";
		else if (box.equals("发件箱")) action = "getsendmail";
		else if (box.equals("垃圾箱")) action = "getdelmail";
		else if (box.equals("草稿箱")) action = "getdraftmail";
		
		tv = (TextView)findViewById(R.id.actionbar_titletextview);
		tv.setText(box);
		
		ImageView iv;
		iv = (ImageView)findViewById(R.id.actionbar_do1imageview);
		iv.setVisibility(View.VISIBLE);
		iv.setImageResource(R.drawable.test_edit);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(BoxActivity.this, CreateActivity.class);
				startActivity(intent);
			}
		});
		
		EditText et = (EditText)findViewById(R.id.box_search);
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (arg0.length() == 0){
					findViewById(R.id.box_conditions).setVisibility(View.GONE);
				}
				else {
					findViewById(R.id.box_conditions).setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		button = new Button[4];
		button[0] = (Button)findViewById(R.id.box_button1);
		button[1] = (Button)findViewById(R.id.box_button2);
		button[2] = (Button)findViewById(R.id.box_button3);
		button[3] = (Button)findViewById(R.id.box_button4);
		
		for (int i=0;i<4;i++){
			button[i].setOnClickListener(new ButtonListener());
		}
		
		listView = (ListView)findViewById(R.id.box_list);
		list = new ArrayList<HashMap<String, Object>>();
		eu_id = new ArrayList<String>();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(BoxActivity.this, MassageActivity.class);
				intent.putExtra("eu_id", list.get(arg2).get("eu_id").toString());
				intent.putExtra("list", eu_id);
				intent.putExtra("now", arg2);
				intent.putExtra("father", box);
				startActivity(intent);
			}
		});
	}
	
	private class MyThread extends Thread{
		
		@Override
		public void run() {
			GetMailList mailList = new GetMailList(BoxActivity.this);
			String jsonString = mailList.getJsonString(action);
			
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				for (int i=0;i<jsonArray.length();i++){
					JSONObject email = jsonArray.optJSONObject(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					
					String[] temp = email.optString("e_time").split("T");
					String[] date = temp[0].split("-");
					date[1] = (Integer.parseInt(date[1]))+"";
					String[] time = temp[1].split(":");
					
					map.put("eu_id", email.optString("eu_id"));
					map.put("isread_ImgUrl", email.optString("isread_ImgUrl"));
					map.put("e_title", email.optString("e_title"));
					map.put("e_sender_UserNm", email.optString("e_sender_UserNm"));
					map.put("e_time", date[1]+"-"+date[2]+" "+time[0]+":"+time[1]);
					list.add(0, map);
					
					eu_id.add(0,email.optString("eu_id"));
				}
				
				if (list.size() == 0) handler.sendEmptyMessage(NO_MAIL);
				else handler.sendEmptyMessage(SUCCESS);
				
			} catch (JSONException e) {
				handler.sendEmptyMessage(ERROR);
				e.printStackTrace();
			}
		}
	}
	
	
	
	private class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			for (int i=0;i<4;i++){
				button[i].setBackgroundResource(R.drawable.unselected);
				button[i].setTextColor(Color.BLACK);
			}
			
			Button now = (Button)arg0;
			now.setBackgroundResource(R.drawable.selected);
			now.setTextColor(Color.WHITE);
		}
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			findViewById(R.id.progressbar).setVisibility(View.GONE);
			TextView tv = (TextView)findViewById(R.id.errortextview);
			
			switch (msg.what){					
				case NO_MAIL:
					tv.setVisibility(View.VISIBLE);
					tv.setText("暂无邮件");
					break;
					
				case SUCCESS:
					SimpleAdapter adapter = new SimpleAdapter(
							BoxActivity.this, list, R.layout.box_item, 
							new String[]{"isread_ImgUrl","e_title","e_sender_UserNm","e_time"}, 
							new int[]{R.id.box_item_image,R.id.box_item_title,R.id.box_item_username,R.id.box_item_time});
					
					listView.setAdapter(adapter);
					break;
					
				case ERROR:
					tv.setVisibility(View.VISIBLE);
					tv.setText("网络错误");
					break;
			}
		}
	};

}
