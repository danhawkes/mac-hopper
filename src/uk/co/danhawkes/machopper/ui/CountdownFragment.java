package uk.co.danhawkes.machopper.ui;

import java.lang.ref.WeakReference;
import java.util.Locale;

import uk.co.danhawkes.machopper.AppSingleton;
import uk.co.danhawkes.machopper.R;
import uk.co.danhawkes.machopper.mac.Mac;
import uk.co.danhawkes.machopper.mac.MacUtils;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stericson.RootTools.RootTools;

public class CountdownFragment extends Fragment {

	private TextView macTextView;
	private TextView progressTextView;
	private ProgressBar progressBar;
	private UiHandler uiHandler = new UiHandler(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		// Start the timer if enabled and not previously been set
		// TODO this should check various other preferences as well
		if (AppSingleton.getPreferences().getBoolean(getString(R.string.pref_key_rotate_enabled),
				false)) {
			if (AppSingleton.getAlarmUtils().getAlarmInterval() == 0) {
				AppSingleton.getAlarmUtils().rescheduleAlarmFromPreferences(
						AppSingleton.getPreferences());
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_countdown, container, false);
		macTextView = (TextView) v.findViewById(R.id.mac_text);
		progressTextView = (TextView) v.findViewById(R.id.progress_text);
		progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.countdown, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_change_mac) {
			changeMac();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		requestRootAndShowErrorOnRejection();
		AppSingleton.getBus().register(this);
		uiHandler.sendEmptyMessage(UiHandler.INITIAL);
	}

	@Override
	public void onPause() {
		super.onPause();
		AppSingleton.getBus().unregister(this);
		uiHandler.removeMessages(UiHandler.TICK);
	}

	private static class UiHandler extends Handler {

		public static final int INITIAL = 0;
		public static final int TICK = 1;
		private final WeakReference<CountdownFragment> mFragment;

		public UiHandler(CountdownFragment fragment) {
			mFragment = new WeakReference<CountdownFragment>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			CountdownFragment fragment = mFragment.get();

			if (fragment != null) {
				long remaining = AppSingleton.getAlarmUtils().getTimeRemainingUntilNextAlarm();
				long interval = AppSingleton.getAlarmUtils().getAlarmInterval();
				fragment.updateProgressTime(remaining, interval);
				fragment.updateProgressBar(remaining, interval);
				if (msg.what == INITIAL) {
					fragment.updateDisplayedMac(MacUtils.getCurrentMac(fragment.getActivity(),
							AppSingleton.getStore()));
					this.sendMessageDelayed(obtainMessage(TICK), remaining % 1000);
				} else {
					this.sendMessageDelayed(obtainMessage(TICK), 1000);
				}
			}
		};
	};

	public void onEvent(MacChangedEvent event) {
			uiHandler.sendEmptyMessage(UiHandler.INITIAL);
		}

	private void requestRootAndShowErrorOnRejection() {
		if (!RootTools.isAccessGiven()) {
			new RootUnavailableDialogFragment().show(getFragmentManager(), null);
		} else {
			DialogFragment fragment = (DialogFragment) getFragmentManager().findFragmentByTag(
					RootUnavailableDialogFragment.TAG);
			if (fragment != null) {
				fragment.dismiss();
			}
		}
	}

	private void updateDisplayedMac(Mac mac) {
		macTextView.setText(mac.toString(true, ':'));
	}

	private void updateProgressTime(long remaining, long interval) {
		int seconds = (int) ((remaining / 1000) % 60);
		int minutes = (int) ((remaining / 60000) % 60);
		int hours = (int) ((remaining / 3600000) % 60);
		String s;
		if (hours > 0) {
			s = String.format(Locale.US, "%dh %dm %ds", hours, minutes, seconds);
		} else if (minutes > 0) {
			s = String.format(Locale.US, "%dm %ds", minutes, seconds);
		} else {
			s = String.format(Locale.US, "%ds", seconds);
		}
		progressTextView.setText(s);
	}

	private void updateProgressBar(long remaining, long interval) {
		int progress = (int) (10000.0f * (((float) remaining / (float) interval)));
		ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress,
				progressBar.getMax());
		animator.setDuration(remaining);
		animator.setInterpolator(new LinearInterpolator());
		animator.start();
	}

	public static class RootUnavailableDialogFragment extends DialogFragment {

		private static final String TAG = RootUnavailableDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
			builder.setTitle(getString(R.string.error_root_unavailable_title));
			builder.setMessage(getString(R.string.error_root_unavailable_message));
			builder.setNeutralButton(android.R.string.ok, null);
			return builder.create();
		}
	}

	private void changeMac() {
		Mac mac = new Mac();
		getActivity().sendBroadcast(MacUtils.newChangeMacIntent(mac));
		// If timer is running, restart it
		long currentInterval = AppSingleton.getStore().loadScheduledAlarmInterval();
		if (currentInterval > 0) {
			AppSingleton.getAlarmUtils().rescheduleAlarm(currentInterval);
		}
	}
}
