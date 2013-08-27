package uk.co.danhawkes.machopper.ui.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class DonatePreference extends ListPreference {

	private String value;

	public DonatePreference(Context context) {
		super(context);
	}

	public DonatePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
			getContext().startActivity(i);
		}
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);

		if ((getEntries() == null) || (getEntryValues() == null)) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array.");
		}
		builder.setItems(getEntries(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DonatePreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				dialog.dismiss();

				// Set the value outside of the usual preference mechanism so
				// it's not saved
				value = getEntryValues()[which].toString();
			}
		});

		builder.setPositiveButton(null, null);
	}

	@Override
	protected boolean callChangeListener(Object newValue) {
		return false;
	}
}
