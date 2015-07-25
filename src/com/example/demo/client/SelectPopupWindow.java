package com.example.demo.client;

import com.example.demo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectPopupWindow extends PopupWindow {

	private View view;
	private int id;
	private WindowManager.LayoutParams param;
	private Activity context;
	private Button button1, button2, dismiss;
	private View.OnClickListener onClickListener;
	private final static int REPLY = R.id.replyimageview;
	private final static int SIGN = R.id.signimageview;

	public SelectPopupWindow(Activity context , View.OnClickListener onClickListener , int id) {

		this.context = context;
		this.onClickListener = onClickListener;
		this.id = id;
		init();
	}

	protected void init() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popupwindow, null);
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setFocusable(true);
		
		this.setAnimationStyle(R.style.popupwin); //设置进出动画

		param = context.getWindow().getAttributes();
		param.alpha = 0.7f;
		context.getWindow().setAttributes(param);
		
		button1 = (Button) view.findViewById(R.id.button1);
		button2 = (Button) view.findViewById(R.id.button2);
		dismiss = (Button) view.findViewById(R.id.dismissbutton);
		
		switch (id) {
		case REPLY:
			button1.setText("回复");
			button2.setText("转发");
			break;
		case SIGN:
			button1.setText("标为未读");
			button2.setText("添加星标");
			break;

		default:
			break;
		}
		
		button1.setOnClickListener(onClickListener);
		button2.setOnClickListener(onClickListener);
		
		dismiss.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				windowdismiss();
			}
		});
		
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int heigth = view.findViewById(R.id.popupwinlayout).getTop();
				int  ydown = (int)event.getRawY();
				System.out.println(heigth+"   "+ydown);
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(ydown>heigth){
						windowdismiss();
					}
				}
				return false;
			}
		});

	}

	protected void windowdismiss() {
		param.alpha = 1f;
		context.getWindow().setAttributes(param);
		this.dismiss();
	}
	
	public int getId() {
		return id;
	}
}
