package thebutton.track;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

/**
 * @author: aavisv
 * @created: 2010-05-23 7:30:09 PM
 */
public class OngoingTrack {
    private Instant start;
    private String task = "";
    protected static final String NO_TASK = "";

    public static OngoingTrack start(Instant start) {
        return new OngoingTrack(start);
    }

    protected OngoingTrack(Instant start) {
        this.start = start;
    }

    public Track stop(Instant end) {
        return new Track(new Interval(start, end), task);
    }

    public OngoingTrack doing(String task) {
        this.task = task;
        return this;
    }

    public ReadableInstant startsAt() {
        return start;
    }
}
