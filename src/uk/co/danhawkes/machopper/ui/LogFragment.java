package uk.co.danhawkes.machopper.ui;

import uk.co.danhawkes.machopper.AppSingleton;
import uk.co.danhawkes.machopper.Logger.LogAppendedEvent;
import uk.co.danhawkes.machopper.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LogFragment extends Fragment {

	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		textView = (TextView) inflater.inflate(R.layout.fragment_log, container, false);
		return textView;
	}

	@Override
	public void onStart() {
		super.onStart();
		AppSingleton.getBus().register(this);
		textView.setText(AppSingleton.getLogger().getLog());
	}

	@Override
	public void onStop() {
		super.onStop();
		AppSingleton.getBus().unregister(this);
	}

	public void onEventMainThread(LogAppendedEvent event) {
		textView.append(event.getMessage());
	}

}
