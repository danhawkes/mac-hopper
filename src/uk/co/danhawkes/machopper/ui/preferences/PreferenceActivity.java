package uk.co.danhawkes.machopper.ui.preferences;

import uk.co.danhawkes.machopper.AppSingleton;
import uk.co.danhawkes.machopper.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Process;

public class PreferenceActivity extends Activity {

	private boolean originalThemeSetting;
	private boolean newThemeSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UiUtils.setThemeFromPreferences(this);

		this.originalThemeSetting = AppSingleton.getPreferences().getBoolean(
				getString(R.string.pref_key_theme_light), false);

		getFragmentManager().beginTransaction()
				.add(android.R.id.content, new PreferenceFragment(), null).commit();
		AppSingleton.getPreferences().registerOnSharedPreferenceChangeListener(
				onSharedPreferencesChangeListener);
	}

	@Override
	public void onBackPressed() {
		if (newThemeSetting != originalThemeSetting) {
			new ApplyThemeDialogFragment().show(getFragmentManager(), null);
		} else {
			super.onBackPressed();
		}
	}

	public static class ApplyThemeDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.preference_theme_dialog_title));
			builder.setMessage(getString(R.string.preference_theme_dialog_message));
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Process.killProcess(Process.myPid());
				}
			});
			builder.setNegativeButton(android.R.string.cancel, null);
			return builder.create();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppSingleton.getPreferences().unregisterOnSharedPreferenceChangeListener(
				onSharedPreferencesChangeListener);
	}

	private OnSharedPreferenceChangeListener onSharedPreferencesChangeListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(getString(R.string.pref_key_rotate_interval))) {
				AppSingleton.getAlarmUtils().rescheduleAlarmFromPreferences(sharedPreferences);
			} else if (key.equals(getString(R.string.pref_key_rotate_enabled))) {
				if (sharedPreferences
						.getBoolean(getString(R.string.pref_key_rotate_enabled), false)) {
					AppSingleton.getAlarmUtils().rescheduleAlarmFromPreferences(sharedPreferences);
				} else {
					AppSingleton.getAlarmUtils().cancelAlarm();
				}
			} else if (key.equals(getString(R.string.pref_key_theme_light))) {
				newThemeSetting = sharedPreferences.getBoolean(
						getString(R.string.pref_key_theme_light), false);
			}
		}
	};
}
