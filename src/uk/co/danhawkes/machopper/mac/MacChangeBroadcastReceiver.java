package uk.co.danhawkes.machopper.mac;

import uk.co.danhawkes.machopper.AppSingleton;
import uk.co.danhawkes.machopper.R;
import uk.co.danhawkes.machopper.Store;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

public class MacChangeBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_SET_MAC = "SET_MAC";
	public static final String EXTRA_MAC = "MAC";

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (ACTION_SET_MAC.equals(intent.getAction())) {

			SharedPreferences prefs = AppSingleton.getPreferences();

			// Do not rotate if screen on and disallowed
			boolean rotateMacWhenScreenOn = AppSingleton.getPreferences().getBoolean(
					context.getString(R.string.pref_key_rotate_when_screen_on), false);
			if (!rotateMacWhenScreenOn) {
				boolean screenOn = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
						.isScreenOn();
				if (!screenOn) {
					return;
				}
			}

			WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));

			// Do not rotate if wifi is unconnected
			if (wifiManager.getConnectionInfo() == null) {
				return;
			}

			// Do not rotate if wifi wake-locked and disallowed
			boolean rotateMacWhenWifiLocked = prefs.getBoolean(
					context.getString(R.string.pref_key_rotate_when_wake_locked), false);
			if (!rotateMacWhenWifiLocked) {
				boolean wifiLockIsHeld = wifiManager.createWifiLock("Mac Rotator").isHeld();
				if (wifiLockIsHeld) {
					return;
				}
			}

			Mac macExtra = intent.getParcelableExtra(EXTRA_MAC);
			final Mac mac = (macExtra == null) ? new Mac() : macExtra;
			new Thread(new Runnable() {

				@Override
				public void run() {
					MacUtils.setMac(mac, context, new Store(context));
				}
			}).start();
		}
	}
}
