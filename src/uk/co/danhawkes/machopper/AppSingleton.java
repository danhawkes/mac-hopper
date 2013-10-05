package uk.co.danhawkes.machopper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.greenrobot.event.EventBus;

public class AppSingleton {

	private static Store store;
	private static AlarmUtils alarmUtils;
	private static SharedPreferences preferences;
	private static Logger logger;
	private static EventBus eventBus;

	public static void initialise(Context context) {
		AppSingleton.store = new Store(context);
		AppSingleton.alarmUtils = new AlarmUtils(context, store);
		AppSingleton.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		AppSingleton.eventBus = new EventBus();
		AppSingleton.logger = new Logger(context, eventBus);
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

	public static Logger getLogger() {
		return logger;
	}

	public static EventBus getBus() {
		return eventBus;
	}
}
