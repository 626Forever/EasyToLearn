package app.main.ui.core.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import app.main.R;
import app.main.ui.core.alarm.FloatCtlService.MyBroadcastReceiver;

public class AlarmActivity extends Activity {
	private Button setBtn;
	private Button cancelBtn;
	private Button backBtn;
	private TextView timeText;
	private Calendar calendar;

	private MyBroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_alarm_layout);

		calendar = Calendar.getInstance();
		timeText = (TextView) findViewById(R.id.main_alarm_set_time);
		setBtn = (Button) findViewById(R.id.main_alarm_set_btn);
		cancelBtn = (Button) findViewById(R.id.main_alarm_cancel_btn);
		backBtn = (Button) findViewById(R.id.main_alarm_back);
		initText();
		setListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FloatCtlService.VIS_LAYOUT); // 为BroadcastReceiver指定action，即要监听的消息名字。
		receiver = AlarmCtl.receiver;
		registerReceiver(receiver, intentFilter); // 注册监听
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void initText() {
		if (AlarmCtl.alarm_hour == -1) {
			String tmpS = "亲，您还没有设置闹钟呐！";
			timeText.setText(tmpS);
		} else {
			String tmpS = "亲，您已经设置的闹钟时间为" + format(AlarmCtl.alarm_hour) + ":"
					+ format(AlarmCtl.alarm_minute);
			timeText.setText(tmpS);
		}
	}

	private void setListener() {
		setBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(AlarmActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Intent intent = new Intent(AlarmActivity.this,
										AlarmReceiver.class);
								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(AlarmActivity.this, 0,
												intent, 0);
								AlarmManager am;
								am = AlarmCtl.alarm_manager;
								am.set(AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										pendingIntent);
								// am.setRepeating(AlarmManager.RTC_WAKEUP,
								// System.currentTimeMillis()
								// + (10 * 1000),
								// (24 * 60 * 60 * 1000), pendingIntent);
								AlarmCtl.alarm_hour = hourOfDay;
								AlarmCtl.alarm_minute = minute;
								String tmpS = "亲，您已经设置的闹钟时间为"
										+ format(hourOfDay) + ":"
										+ format(minute);
								timeText.setText(tmpS);
							}
						}, mHour, mMinute, true).show();
			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AlarmActivity.this,
						AlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						AlarmActivity.this, 0, intent, 0);
				AlarmManager am;
				am = AlarmCtl.alarm_manager;
				am.cancel(pendingIntent);
				timeText.setText("闹钟已取消！");
			}
		});
		backBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FloatCtlService.VIS_LAYOUT);
				sendBroadcast(intent);
				AlarmActivity.this.finish();
			}
		});
	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(FloatCtlService.VIS_LAYOUT);
			sendBroadcast(intent);
			AlarmActivity.this.finish();
		}
		return false;
	}
}
