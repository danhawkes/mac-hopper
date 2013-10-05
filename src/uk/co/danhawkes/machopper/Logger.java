package uk.co.danhawkes.machopper;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import de.greenrobot.event.EventBus;

public class Logger {

	private final DateFormat dateFormat;
	private final StringBuilder stringBuilder = new StringBuilder();
	private final EventBus eventBus;

	public static class LogAppendedEvent {

		private final String message;

		public LogAppendedEvent(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public Logger(Context context, EventBus bus) {
		this.eventBus = bus;
		this.dateFormat = android.text.format.DateFormat.getTimeFormat(context);
	}

	public void log(String message) {
		stringBuilder.append(dateFormat.format(new Date()));
		stringBuilder.append(" | ");
		stringBuilder.append(message);
		stringBuilder.append("\n");
		eventBus.post(new LogAppendedEvent(message));
	}

	public String getLog() {
		return stringBuilder.toString();
	}
}
