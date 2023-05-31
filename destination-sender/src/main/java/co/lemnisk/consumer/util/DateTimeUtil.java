package co.lemnisk.consumer.util;

import java.time.Instant;

public class DateTimeUtil {
	public static long getDateTimeInMilliseconds() {
		Instant instant = Instant.now();
		return instant.toEpochMilli();
	}
}
