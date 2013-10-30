package app.main.ui.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.alarm.FloatCtlService;

public class MainActivity extends Activity {
	private static final int START_SUBJECTS_ACTIVITY = 0;
	private Button start;
	private Button help;
	private Button author;
	private Button quit;

	private Messenger mService = null;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// Activity已经绑定了Service
			// 通过参数service来创建Messenger对象，这个对象可以向Service发送Message，与Service进行通信
			mService = new Messenger(service);
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
			mBound = false;
		}
	};

	private boolean mBound;
	private boolean isExit = false;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_core_menu);
		start = (Button) findViewById(R.id.main_start_btn);
		help = (Button) findViewById(R.id.main_help_btn);
		author = (Button) findViewById(R.id.main_author_btn);
		quit = (Button) findViewById(R.id.main_quit_btn);
		// 绑定Service
		mBound = this.getApplicationContext().bindService(
				new Intent(this, FloatCtlService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		setListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == START_SUBJECTS_ACTIVITY) {
			if (resultCode == RESULT_OK) {
				closeFloatWnd();
				System.out.println("asd");
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBound) {
			getApplicationContext().unbindService(mConnection);
			mBound = false;
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
		} else {
			System.exit(0);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return false;
	}

	private void setListener() {
		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SubjectsActivity.class);
				startActivityForResult(intent, START_SUBJECTS_ACTIVITY);
				openFloatWnd();
			}
		});
		help.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		author.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		quit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void openFloatWnd() {
		Message msg = Message.obtain(null, FloatCtlService.START_FLOAT_WND, 0,
				0);
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void closeFloatWnd() {
		Message msg = Message.obtain(null, FloatCtlService.CLOSE_FLOAT_WND, 0,
				0);
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
