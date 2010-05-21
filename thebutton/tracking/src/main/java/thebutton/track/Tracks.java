package thebutton.track;

import org.joda.time.Duration;

/**
 * @author: aavisv
 * @created: 2010-05-21 4:11:06 PM
 */
public interface Tracks {
    int count();

    Duration totalDuration();

    Track track(int trackNo);

    Track first();

    void add(Track o);
}
