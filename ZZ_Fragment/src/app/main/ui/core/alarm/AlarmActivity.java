package app.main.ui.core.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import app.main.R;

public class AlarmActivity extends Activity {
	Button setBtn;
	Button cancelBtn;
	Button backBtn;
	TextView timeText;
	Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_alarm_layout);

		calendar = Calendar.getInstance();
		timeText = (TextView) findViewById(R.id.main_alarm_set_time);
		setBtn = (Button) findViewById(R.id.main_alarm_set_btn);
		cancelBtn = (Button) findViewById(R.id.main_alarm_cancel_btn);
		backBtn = (Button) findViewById(R.id.main_alarm_back);
		initText();
		setListener();
	}

	private void initText() {
		if (AlarmTime.alarm_hour == -1) {
			String tmpS = "亲，您还没有设置闹钟呐！";
			timeText.setText(tmpS);
		} else {
			String tmpS = "亲，您已经设置的闹钟时间为" + format(AlarmTime.alarm_hour) + ":"
					+ format(AlarmTime.alarm_minute);
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
								am = (AlarmManager) getSystemService(ALARM_SERVICE);
								am.set(AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										pendingIntent);
								// am.setRepeating(AlarmManager.RTC_WAKEUP,
								// System.currentTimeMillis()
								// + (10 * 1000),
								// (24 * 60 * 60 * 1000), pendingIntent);
								AlarmTime.alarm_hour = hourOfDay;
								AlarmTime.alarm_minute = minute;
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
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(pendingIntent);
				timeText.setText("闹钟已取消！");
			}
		});
	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

}
