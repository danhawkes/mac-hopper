package uk.co.danhawkes.machopper.ui.preferences;

import uk.co.danhawkes.machopper.R;
import android.os.Bundle;

public class PreferenceFragment extends android.preference.PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
