package uk.co.danhawkes.machopper.ui;

import uk.co.danhawkes.machopper.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class HelpDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.action_help));
		builder.setMessage(getString(R.string.help_text));
		builder.setNeutralButton(android.R.string.ok, null);
		return builder.create();
	}

}
