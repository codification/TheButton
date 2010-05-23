package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

/**
 * @author: aavisv
 * @created: 2010-05-19 1:23:50 PM
 */
public interface TimeTracker {
    Duration sumUpToday();

    Duration sumUpDay(LocalDate date);

    Tracks todays();

    Tracks tracksFor(LocalDate date);

    Duration runningTime();

    boolean isIdle();

    boolean isTracking();

    Duration sinceStarted();

    void setTrackFollower(TrackFollower trackFollower);
}
