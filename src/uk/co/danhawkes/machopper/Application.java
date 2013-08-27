package uk.co.danhawkes.machopper;

import android.preference.PreferenceManager;
import uk.co.danhawkes.machopper.R;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		AppSingleton.initialise(this);
	}
}
