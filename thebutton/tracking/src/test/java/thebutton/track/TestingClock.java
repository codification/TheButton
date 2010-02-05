/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;

/**
 * A predictable clock implementation to use for testing.
 * Always returns the same instant for {@link #now()} unless it has been advanced.
 * The clock is reset to 06 in the morning the current day upon creation.
 */
public class TestingClock implements Clock {
    private Instant time;

    public TestingClock() {
        final MutableDateTime dateTime = new MutableDateTime();
        dateTime.setHourOfDay(6);
        time = dateTime.toInstant();
    }

    public void advance(Duration duration) {
        time = time.plus(duration);
    }

    @Override
    public Instant now() {
        return time;
    }
}
