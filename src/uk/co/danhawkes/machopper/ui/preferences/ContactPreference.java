package uk.co.danhawkes.machopper.ui.preferences;

import uk.co.danhawkes.machopper.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;

public class ContactPreference extends Preference {

	public ContactPreference(Context context) {
		this(context, null);
	}

	public ContactPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
	}

	public ContactPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onClick() {
		super.onClick();
		Resources res = getContext().getResources();
		Uri uri = Uri.fromParts("mailto", res.getString(R.string.preference_contact_email), null);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.app_name));
		getContext().startActivity(
				Intent.createChooser(intent,
						getContext().getString(R.string.preference_contact_chooser_title)));
	}
}
