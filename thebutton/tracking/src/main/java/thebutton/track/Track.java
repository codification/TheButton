package thebutton.track;

import org.joda.time.DateTime;
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

    public Interval interval() {
        return interval;
    }

    public DateTime startsAt() {
        return interval.getStart();
    }

    public DateTime endsAt() {
        return interval.getEnd();
    }

    @Override
    public String toString() {
        return "Track{" +
                "interval=" + interval +
                ", task='" + task + '\'' +
                '}';
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (!interval.equals(track.interval)) return false;
        if (task != null ? !task.equals(track.task) : track.task != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return interval.hashCode();
    }
}
