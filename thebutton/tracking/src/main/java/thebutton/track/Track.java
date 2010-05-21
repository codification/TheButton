package thebutton.track;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableDuration;

/**
 * @author: aavisv
 * @created: 2010-05-20 2:34:42 PM
 */
public class Track {
    private Interval interval;
    private String task;

    public Track(Interval interval, String task) {
        this.interval = interval;
        this.task = task;
    }

    public ReadableDuration duration() {
        return interval.toDuration();
    }

    public String task() {
        return task;
    }

    public static TrackBuilder start(Instant start) {
        return new TrackBuilder(start);
    }

    public Interval interval() {
        return interval;
    }

    public DateTime start() {
        return interval.getStart();
    }

    public DateTime stop() {
        return interval.getEnd();
    }

    public static class TrackBuilder {
        private Instant start;
        private String task = "";

        public TrackBuilder(Instant start) {
            this.start = start;
        }

        public Track stop(Instant end) {
            return new Track(new Interval(start, end), task);
        }

        public TrackBuilder doing(String task) {
            this.task = task;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Track{" +
                "interval=" + interval +
                ", task='" + task + '\'' +
                '}';
    }
}
