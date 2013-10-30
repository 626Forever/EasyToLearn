package app.main.ui.core.alarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import app.main.R;

public class FloatCtlService extends Service {
	public static final int START_FLOAT_WND = 0;
	public static final int CLOSE_FLOAT_WND = 1;
	private final Messenger msger = new Messenger(new MsgHandler());
	private static final String TAG = "FloatWindowTest";
	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	LinearLayout mFloatLayout;
	Button mFloatView;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return msger.getBinder();
	}

	private class MsgHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case START_FLOAT_WND:
				createFloatView();
				break;
			case CLOSE_FLOAT_WND:
				hideFloatView();
				break;
			default:
				super.handleMessage(msg);
				break;
			}

		}
	}

	private void createFloatView() {
		// 获取LayoutParams对象
		wmParams = new WindowManager.LayoutParams();

		// 获取的是CompatModeWrapper对象
		mWindowManager = (WindowManager) getApplication().getSystemService(
				Context.WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager3--->" + mWindowManager);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		;
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());

		mFloatLayout = (LinearLayout) inflater.inflate(
				R.layout.main_float_layout, null);
		mWindowManager.addView(mFloatLayout, wmParams);
		// setContentView(R.layout.main);
		mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);

		Log.i(TAG, "mFloatView" + mFloatView);
		Log.i(TAG, "mFloatView--parent-->" + mFloatView.getParent());
		Log.i(TAG, "mFloatView--parent--parent-->"
				+ mFloatView.getParent().getParent());
		// 绑定触摸移动监听
		mFloatView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				wmParams.x = (int) event.getRawX() - mFloatLayout.getWidth()
						/ 2;
				// 25为状态栏高度
				wmParams.y = (int) event.getRawY() - mFloatLayout.getHeight()
						/ 2 - 40;
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});

		// 绑定点击监听
		mFloatView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FloatCtlService.this,
						AlarmActivity.class);
				startActivity(intent);
				
			}
		});

	}

	private void hideFloatView() {
		mWindowManager.removeView(mFloatLayout);
	}

}
