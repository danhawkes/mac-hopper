package uk.co.danhawkes.machopper.ui.preferences;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
	private Calendar calendar;
	private TimePicker picker = null;

	public TimePreference(Context context) {
		this(context, null);
	}

	public TimePreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
	}

	public TimePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		calendar = new GregorianCalendar();
	}

	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker(getContext());
		return super.onCreateDialogView();
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
			calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

			setSummary(getSummary());
			if (callChangeListener(calendar.getTimeInMillis())) {
				persistLong(calendar.getTimeInMillis());
				notifyChanged();
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		if (restoreValue) {
			if (defaultValue == null) {
				calendar.setTimeInMillis(getPersistedLong(System.currentTimeMillis()));
			} else {
				calendar.setTimeInMillis(Long.parseLong(getPersistedString((String) defaultValue)));
			}
		} else {
			if (defaultValue == null) {
				calendar.setTimeInMillis(System.currentTimeMillis());
			} else {
				calendar.setTimeInMillis(Long.parseLong((String) defaultValue));
			}
		}
		setSummary(getSummary());
	}

	@Override
	public CharSequence getSummary() {
		if (calendar == null) {
			return null;
		}
		return DateFormat.getTimeFormat(getContext()).format(new Date(calendar.getTimeInMillis()));
	}
}