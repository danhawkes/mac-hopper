package uk.co.danhawkes.machopper.mac;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import uk.co.danhawkes.machopper.AppSingleton;
import uk.co.danhawkes.machopper.Store;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class MacUtils {

	private static final String COMMAND_TEMPLATE = "ip link set %1$s down; ip link set %1$s address %2$s; ip link set %1$s up";
	public static final String EXTRA_MAC = "MAC";

	public static class MacChangedEvent {
		private final Mac mac;

		public MacChangedEvent(Mac mac) {
			this.mac = mac;
		}

		public Mac getMac() {
			return mac;
		}
	}

	public static void setMac(final Mac mac, Context context, Store store) {
		AppSingleton.getLogger().log("Set MAC to " + mac + ".");
		try {
			RootTools.getShell(true).add(getSetMacCommand("wlan0", mac)).waitForFinish();

			// Save changed MAC to disk
			store.saveCurrentMac(mac);

			// Send broadcast to update UI
			AppSingleton.getBus().post(new MacChangedEvent(mac));

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (RootDeniedException e) {
			e.printStackTrace();
		}
	}

	private static CommandCapture getSetMacCommand(String interfac, Mac mac) {
		String command = String.format(COMMAND_TEMPLATE, interfac, mac.toString());
		return new CommandCapture(0, command);
	}

	public static Mac getRealMac(Context context) {
		WifiManager wifiMan = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
		WifiInfo connInfo = wifiMan.getConnectionInfo();
		if (connInfo != null) {
			String macString = connInfo.getMacAddress();
			if (macString != null) {
				return Mac.fromString(macString);
			}
		}
		return null;
	}

	public static Mac getCurrentMac(Context context, Store store) {
		Mac savedCurrentMac = store.loadCurrentMac();
		if (savedCurrentMac != null) {
			return savedCurrentMac;
		} else {
			return getRealMac(context);
		}
	}

	public static Intent newChangeMacIntent(Mac mac) {
		Intent i = new Intent(MacChangeBroadcastReceiver.ACTION_SET_MAC);
		i.putExtra(MacChangeBroadcastReceiver.EXTRA_MAC, mac);
		return i;
	}

	public static Intent newChangeMacIntent() {
		return new Intent(MacChangeBroadcastReceiver.ACTION_SET_MAC);
	}
}
