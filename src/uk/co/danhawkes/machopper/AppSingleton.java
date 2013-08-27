package uk.co.danhawkes.machopper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSingleton {

	private static Store store;
	private static AlarmUtils alarmUtils;
	private static SharedPreferences preferences;

	public static void initialise(Context context) {
		AppSingleton.store = new Store(context);
		AppSingleton.alarmUtils = new AlarmUtils(context, store);
		AppSingleton.preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static Store getStore() {
		return store;
	}

	public static AlarmUtils getAlarmUtils() {
		return alarmUtils;
	};

	public static SharedPreferences getPreferences() {
		return preferences;
	}
}
