package uk.co.danhawkes.machopper;

import uk.co.danhawkes.machopper.R;
import uk.co.danhawkes.machopper.mac.MacChangeBroadcastReceiver;
import uk.co.danhawkes.machopper.mac.MacUtils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public class AlarmUtils {

	public static final String TAG = AlarmUtils.class.getSimpleName();

	private final Context context;
	private final Store store;

	public AlarmUtils(Context context, Store store) {
		this.context = context.getApplicationContext();
		context.registerReceiver(mReceiver, new IntentFilter(
				MacChangeBroadcastReceiver.ACTION_SET_MAC));
		this.store = store;
	}

	public void rescheduleAlarm(long interval) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = getAlarmPendingIntent();
		long time = System.currentTimeMillis() + interval;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, pendingIntent);
		store.saveScheduledAlarmTimeAndInterval(time, interval);
	}

	public void rescheduleAlarmFromPreferences(SharedPreferences preferences) {
		String intervalMins = preferences.getString(
				context.getString(R.string.pref_key_rotate_interval), null);
		long intervalMs = Integer.parseInt(intervalMins) * 60 * 1000;
		rescheduleAlarm(intervalMs);
	}

	private PendingIntent getAlarmPendingIntent() {
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				MacUtils.newChangeMacIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	public void cancelAlarm() {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(getAlarmPendingIntent());
	}

	public long getTimeRemainingUntilNextAlarm() {
		long t0 = store.loadScheduledAlarmTime();
		long t1 = System.currentTimeMillis();
		long dt = t0 - t1;
		return (dt > 0) ? dt : 0;
	}

	public long getAlarmInterval() {
		return store.loadScheduledAlarmInterval();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			store.saveScheduledAlarmTime(System.currentTimeMillis()
					+ store.loadScheduledAlarmInterval());
		}
	};
}
