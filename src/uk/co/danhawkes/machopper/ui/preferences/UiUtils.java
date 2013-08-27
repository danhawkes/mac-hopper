package uk.co.danhawkes.machopper.ui.preferences;

import uk.co.danhawkes.machopper.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UiUtils {

	public static void setThemeFromPreferences(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean light = prefs.getBoolean(context.getString(R.string.pref_key_theme_light), false);
		context.setTheme(light ? R.style.AppTheme : R.style.AppTheme_Dark);
	}
}
