package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.Serializable;

public class TimeStringFormatter implements Serializable {
    public static final PeriodFormatter PERIOD_FORMATTER_MINUTES;
    public static final PeriodFormatter PERIOD_FORMATTER_SECONDS;

    static {
        PeriodFormatterBuilder formatterBuilder = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSuffix("h")
                .appendSeparator(":")
                .appendMinutes()
                .appendSuffix("m");
        PERIOD_FORMATTER_MINUTES = formatterBuilder.toFormatter();
        formatterBuilder = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSuffix("h")
                .appendSeparator(":")
                .appendMinutes()
                .appendSuffix("m");
        PERIOD_FORMATTER_SECONDS = formatterBuilder
                .appendSeparator(":")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
    }

    public TimeStringFormatter() {
    }

    public String usingSeconds(Duration duration) {
        return duration.toPeriod(PeriodType.time()).toString(PERIOD_FORMATTER_SECONDS);
    }

    public String usingMinutes(Duration duration) {
        return duration.toPeriod(PeriodType.time()).toString(PERIOD_FORMATTER_MINUTES);
    }

    public String timeOfDay(ReadableDateTime instant) {
        return instant.toString("HH:mm:ss");
    }
}