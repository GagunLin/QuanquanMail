package com.example.demo.client;

import com.example.demo.R;

import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class PullToRefreshLayout extends RelativeLayout {

	private LayoutInflater layoutInflater;
	private View header;
	private Scroller mScroller;
	private TextView mtextview;
	private ImageView mimageview;
	private ProgressBar mProgressBar;
	private RefreshListener refreshListener;
	private ListView listView;

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public PullToRefreshLayout(Context context) {
		super(context);
		initView(context);

	}

	public PullToRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void initView(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		header = layoutInflater.inflate(R.layout.header, null);
		addView(header);
		mtextview = (TextView) header.findViewById(R.id.refreshtextview);
		mimageview = (ImageView) header.findViewById(R.id.refreshimg);
		mProgressBar = (ProgressBar) header.findViewById(R.id.refreshprogressBar);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int realHeigth = MeasureSpec.getSize(heightMeasureSpec);
		int headerHeigth = MeasureSpec.makeMeasureSpec((int) (realHeigth * 0.15f), MeasureSpec.EXACTLY);
		header.measure(widthMeasureSpec, headerHeigth);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		header.layout(l, -header.getMeasuredHeight(), r, 0);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (!mScroller.computeScrollOffset()) {
			return;
		}
		int currenY = mScroller.getCurrY();
		scrollTo(0, currenY);
		invalidate();
	}

	private boolean isrote;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!ispull) {
			isPullType(ev);
			return true;
		}
		if (isupdownpull) {
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_MOVE:
				int currentY = getScrollY();
				int disY = (int) (ev.getY() - point.y);
				int expectY = -disY / 2 + currentY;
				if (expectY < 0) {
					if (expectY < -header.getMeasuredHeight()) {
						mtextview.setText("释放刷新");
						if (!isrote) {
							RotateAnimation animation = new RotateAnimation(0f, 180f, mimageview.getWidth() / 2,
									mimageview.getHeight() / 2);
							animation.setDuration(150);
							animation.setFillAfter(true);
							mimageview.startAnimation(animation);
							isrote = !isrote;
						}
					}
					if (expectY > -header.getMeasuredHeight()) {
						mtextview.setText("下拉刷新");
						if (isrote) {
							RotateAnimation animation = new RotateAnimation(180f, 360f, mimageview.getWidth() / 2,
									mimageview.getHeight() / 2);
							animation.setDuration(150);
							animation.setFillAfter(true);
							mimageview.startAnimation(animation);
							isrote = !isrote;
						}
					}
				} else {

				}
				if (istop) {
					if (expectY > 0) {
					} else {
						scrollTo(0, expectY);
					}
				} else {

				}
				point.y = (int) ev.getY();

				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mimageview.clearAnimation();
				int currY = getScrollY();
				if (Math.abs(currY) > header.getMeasuredHeight() && currY < 0) {
					new refreshingAsy().execute(currY);
				} else {
					mScroller.startScroll(0, currY, 0, -currY, 500);
				}

				invalidate();
				ispull = false;
				isupdownpull = false;
				break;
			default:

				break;
			}
		} else {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				ispull = false;
				isupdownpull = false;
				break;

			default:
				break;
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	private boolean ispull;
	private boolean isupdownpull;
	private boolean istop;
	private boolean isrefreshing;
	private static final int MIN_DIS = 20;
	private Point point = new Point();

	public void isPullType(MotionEvent ev) {

		if (!isrefreshing) {
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
				super.dispatchTouchEvent(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				int dX = (int) Math.abs(ev.getX() - point.x);
				int dY = (int) Math.abs(ev.getY() - point.y);
				if (dY > MIN_DIS && dY > dX) {
					ispull = true;
					isupdownpull = true;
					point.x = (int) ev.getX();
					point.y = (int) ev.getY();
				} else if (dY > MIN_DIS && dY < dX) {
					ispull = true;
					isupdownpull = false;
					point.x = (int) ev.getX();
					point.y = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				ispull = false;
				isupdownpull = false;
				super.dispatchTouchEvent(ev);
				break;

			default:
				break;
			}

			View firstChild = listView.getChildAt(0);
			if (firstChild != null) {
				int firstChildPosition = listView.getFirstVisiblePosition();
				if (firstChildPosition == 0 && firstChild.getTop() == 0) {
					istop = true;
				} else {
					istop = false;
				}
			}
		}
	}

	class refreshingAsy extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			publishProgress(params);
			sleep(300);
			if (refreshListener != null) {
				isrefreshing = true;
				refreshListener.refresh();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			mtextview.setText("刷新中...");
			mProgressBar.setVisibility(View.VISIBLE);
			mimageview.setVisibility(View.GONE);
			mScroller.startScroll(0, values[0], 0, -values[0] - header.getMeasuredHeight(), 300);
			invalidate();
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			int currY = getScrollY();
			mtextview.setText("刷新完成");
			mProgressBar.setVisibility(View.GONE);
			mimageview.setVisibility(View.VISIBLE);
			mScroller.startScroll(0, currY, 0, -currY, 300);
			isrefreshing = false;
			invalidate();
		}

	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public interface RefreshListener {
		void refresh();
	}

	public void setonRefreshListener(RefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// return true;
	// }
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return false;
	// }
}
