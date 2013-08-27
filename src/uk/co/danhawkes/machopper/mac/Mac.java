package uk.co.danhawkes.machopper.mac;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.io.BaseEncoding;

public class Mac implements Parcelable {

	private static final int MAC_LEN_BYTES = 6;
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final Pattern PATTERN = Pattern.compile("[^a-f0-9]", Pattern.CASE_INSENSITIVE);

	private final byte[] mData;

	public Mac(byte[] bytes) {
		mData = Arrays.copyOf(bytes, bytes.length);
	}

	public Mac() {
		mData = new byte[MAC_LEN_BYTES];
		RANDOM.nextBytes(mData);
	}

	public Mac(Parcel parcel) {
		mData = new byte[MAC_LEN_BYTES];
		parcel.readByteArray(mData);
	}

	public byte[] getRawData() {
		return Arrays.copyOf(mData, mData.length);
	}

	@Override
	public String toString() {
		return toString(false, ':');
	}

	public String toString(boolean lowercase, char delimiter) {
		BaseEncoding encoder = BaseEncoding.base16();
		if (lowercase) {
			encoder = encoder.lowerCase();
		}
		String encodeHex = encoder.encode(mData);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mData.length; i++) {
			sb.append(encodeHex.charAt(i));
			sb.append(encodeHex.charAt(i + 1));
			if (i < (mData.length - 1)) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	public static Mac fromString(String macString) {
		Matcher matcher = PATTERN.matcher(macString);
		macString = matcher.replaceAll("").toUpperCase(Locale.US);
		return new Mac(BaseEncoding.base16().upperCase().decode(macString));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeByteArray(mData);
	}

	public static final Parcelable.Creator<Mac> CREATOR = new Parcelable.Creator<Mac>() {
		@Override
		public Mac createFromParcel(Parcel in) {
			return new Mac(in);
		}

		@Override
		public Mac[] newArray(int size) {
			return new Mac[size];
		}
	};
}
