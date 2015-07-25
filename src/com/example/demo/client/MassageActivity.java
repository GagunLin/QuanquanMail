package com.example.demo.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;
import com.example.demo.netutil.GetMailDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MassageActivity extends Activity {

	private GetMailDetail getMailDetail;
	private SelectPopupWindow selectPopupWindow;
	private TextView title, sender, time, content, recevicer;
	private String father;
	private ArrayList<String> eu_id_list;
	private ImageView pre_mail, next_mail;
	private int now;
	private boolean isdetailsopen = false;

	private final static String GET_MAILFAIL = "invalid token";
	private final static String GET_NETFAIL = "网络请求失败，无法获取信息";
	private final static int GET_SUCCESS = 1;
	private final static int GET_FAIL = 0;
	private final static int GET_NETERROR = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_maildetail);

		Intent intent = getIntent();
		father = intent.getStringExtra("father");
		eu_id_list = intent.getStringArrayListExtra("list");
		System.out.println(eu_id_list);
		now = intent.getIntExtra("now", 0);

		init();
		getMailDetail(eu_id_list.get(now));
	}

	public void init() {

		title = (TextView) findViewById(R.id.titletextview);
		sender = (TextView) findViewById(R.id.sendertextview);
		time = (TextView) findViewById(R.id.timetextview);
		content = (TextView) findViewById(R.id.contenttextview);
		recevicer = (TextView) findViewById(R.id.recevicertextview);
		next_mail = (ImageView) findViewById(R.id.actionbar).findViewById(R.id.actionbar_do1imageview);
		next_mail.setImageResource(R.drawable.test_next);
		pre_mail = (ImageView) findViewById(R.id.actionbar).findViewById(R.id.actionbar_do2imageview);
		pre_mail.setImageResource(R.drawable.test_pre);

		((TextView) findViewById(R.id.actionbar).findViewById(R.id.actionbar_titletextview))
				.setText(R.string.mail_details);
		((ImageView) findViewById(R.id.actionbar).findViewById(R.id.actionbar_do1imageview))
				.setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.actionbar).findViewById(R.id.actionbar_do2imageview))
				.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.actionbar_leftimageview)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.actionbar_leftimageview)).setText(father);
		
		checkNextOrPre();

		getMailDetail = new GetMailDetail(getApplicationContext());

		findViewById(R.id.errortextview).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getMailDetail(eu_id_list.get(now));
			}
		});

		findViewById(R.id.action).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		findViewById(R.id.showdetailtextview).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isdetailsopen) {
					((TextView) findViewById(R.id.showdetailtextview)).setText("显示详情");
					findViewById(R.id.layout2).setVisibility(View.GONE);
					isdetailsopen = !isdetailsopen;
				} else {
					((TextView) findViewById(R.id.showdetailtextview)).setText("隐藏详情");
					findViewById(R.id.layout2).setVisibility(View.VISIBLE);
					isdetailsopen = !isdetailsopen;
				}
			}
		});

		findViewById(R.id.replyimageview).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPopupWindow = new SelectPopupWindow(MassageActivity.this, new popupwinButtonOnclickListener(),
						R.id.replyimageview);
				selectPopupWindow.showAtLocation(MassageActivity.this.findViewById(R.id.powinlayout),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});

		findViewById(R.id.signimageview).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPopupWindow = new SelectPopupWindow(MassageActivity.this, new popupwinButtonOnclickListener(),
						R.id.signimageview);
				selectPopupWindow.showAtLocation(MassageActivity.this.findViewById(R.id.powinlayout),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});

		pre_mail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (now != 0) {
					getMailDetail(eu_id_list.get(now - 1));
					now = now - 1;
					checkNextOrPre();
				}
			}
		});

		next_mail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (now != (eu_id_list.size() - 1)) {
					getMailDetail(eu_id_list.get(now + 1));
					now = now + 1;
					checkNextOrPre();
				}
			}
		});

	}

	public void getMailDetail(final String eu_id) {

		((ProgressBar) findViewById(R.id.progressbar)).setVisibility(View.VISIBLE);
		((ScrollView) findViewById(R.id.contentscrollview)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.errortextview)).setVisibility(View.GONE);

		new Thread() {
			@Override
			public void run() {
				super.run();

				String result = getMailDetail.getJsonString(eu_id);

				switch (result) {
				case GET_NETFAIL:
					handler.sendEmptyMessage(GET_NETERROR);
					break;

				case GET_MAILFAIL:
					handler.sendEmptyMessage(GET_FAIL);
					break;

				default:
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("result", result);
					msg.setData(bundle);
					msg.what = GET_SUCCESS;
					handler.sendMessage(msg);

					break;
				}

			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case GET_SUCCESS:
				try {
					((ScrollView) findViewById(R.id.contentscrollview)).setVisibility(View.VISIBLE);
					((ProgressBar) findViewById(R.id.progressbar)).setVisibility(View.GONE);
					((TextView) findViewById(R.id.errortextview)).setVisibility(View.GONE);
					JSONArray jsarray = new JSONArray(msg.getData().getString("result"));
					JSONObject js = new JSONObject(jsarray.getString(0));
					title.setText(js.getString("e_title"));
					sender.setText(js.getString("e_sender_UserNm"));
					time.setText(js.getString("e_time").replace("T", " "));
					content.setText(js.getString("e_content").replace("<p>", "").replace("</p>", "\n"));
					recevicer.setText(js.getString("strreceiver").replace(";", " "));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case GET_FAIL:
				// TODO：Token发生改变 应该重新登陆
				((ScrollView) findViewById(R.id.contentscrollview)).setVisibility(View.GONE);
				((ProgressBar) findViewById(R.id.progressbar)).setVisibility(View.GONE);
				((TextView) findViewById(R.id.errortextview)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.errortextview)).setText("Token发生改变  应该重新登陆");
				break;

			case GET_NETERROR:
				((ScrollView) findViewById(R.id.contentscrollview)).setVisibility(View.GONE);
				((ProgressBar) findViewById(R.id.progressbar)).setVisibility(View.GONE);
				((TextView) findViewById(R.id.errortextview)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.errortextview)).setText(GET_NETFAIL);
				break;
			default:
				break;
			}
		}

	};

	private class popupwinButtonOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = selectPopupWindow.getId();
			selectPopupWindow.windowdismiss();
			switch (id) {
			case R.id.signimageview:
				switch (v.getId()) {
				case R.id.button1:
					// TODO：标为未读
					break;
				case R.id.button2:
					// TODO：添加星标
					break;
				}
				break;
			case R.id.recevicertextview:
				switch (v.getId()) {
				case R.id.button1:
					// TODO：回复
					break;
				case R.id.button2:
					// TODO：转发
					break;
				}
				break;
			}
		}
	}

	public void checkNextOrPre() {
		if (now == 0) {
			pre_mail.setAlpha(0.3f);
		}
		if (now == eu_id_list.size() - 1) {
			next_mail.setAlpha(0.3f);
		}
		if (now > 0 && now < eu_id_list.size() - 1) {
			pre_mail.setAlpha(1.0f);
			next_mail.setAlpha(1.0f);
		}
	}
}
