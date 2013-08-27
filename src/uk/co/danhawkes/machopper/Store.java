package uk.co.danhawkes.machopper;

import uk.co.danhawkes.machopper.mac.Mac;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Store {

	private static final String SP_SCHEDULED_TIME = "scheduledAlarmTime";
	private static final String SP_SCHEDULED_INTERVAL = "scheduledAlarmInterval";
	private static final String KEY_REAL_MAC = "realMac";
	private static final String KEY_CURRENT_MAC = "currentMac";
	private final SharedPreferences preferences;

	public Store(Context context) {
		this.preferences = context.getSharedPreferences(Store.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	public Mac loadRealMac() {
		return loadMac(KEY_REAL_MAC);
	}

	public void saveRealMac(Mac mac) {
		saveMac(KEY_REAL_MAC, mac);
	}

	public Mac loadCurrentMac() {
		return loadMac(KEY_CURRENT_MAC);
	}

	public void saveCurrentMac(Mac mac) {
		saveMac(KEY_CURRENT_MAC, mac);
	}

	private Mac loadMac(String keyName) {
		String s = preferences.getString(keyName, null);
		if (s != null) {
			return Mac.fromString(s);
		}
		return null;
	}

	private void saveMac(String keyName, Mac mac) {
		preferences.edit().putString(keyName, mac.toString()).commit();
	}

	public void saveScheduledAlarmTime(long time) {
		preferences.edit().putLong(SP_SCHEDULED_TIME, time).commit();
	}

	public long loadScheduledAlarmTime() {
		return preferences.getLong(SP_SCHEDULED_TIME, 0);
	}

	public void saveScheduledAlarmTimeAndInterval(long time, long interval) {
		Editor edit = preferences.edit();
		edit.putLong(SP_SCHEDULED_TIME, time);
		edit.putLong(SP_SCHEDULED_INTERVAL, interval);
		edit.commit();
	}

	public long loadScheduledAlarmInterval() {
		return preferences.getLong(SP_SCHEDULED_INTERVAL, 0);
	}
}
