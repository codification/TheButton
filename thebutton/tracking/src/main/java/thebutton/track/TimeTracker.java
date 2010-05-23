package thebutton.track;

import org.joda.time.Duration;

/**
 * @author: aavisv
 * @created: 2010-05-19 1:23:50 PM
 */
public interface TimeTracker {

    Duration runningTime();

    boolean isIdle();

    boolean isTracking();

    Duration sinceStarted();

    void setTrackFollower(TrackFollower trackFollower);
}
