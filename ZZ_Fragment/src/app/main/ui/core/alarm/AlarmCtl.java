package app.main.ui.core.alarm;

import android.app.AlarmManager;
import app.main.ui.core.alarm.FloatCtlService.MyBroadcastReceiver;

public class AlarmCtl {
	public static int alarm_hour = -1;
	public static int alarm_minute = -1;
	public static AlarmManager alarm_manager;
	public static MyBroadcastReceiver receiver;
}
