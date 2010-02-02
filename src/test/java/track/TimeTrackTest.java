/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package track;

import org.joda.time.Duration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.Duration.standardDays;
import static org.joda.time.Duration.standardHours;
import static org.joda.time.Duration.standardMinutes;

public class TimeTrackTest {
    private TimeTracker tracker;
    private TestingClock clock;

    @Test
    public void noTimeTrackedWhenNotTicked() {
        assertThat(tracker.sumUpToday(), is(Duration.ZERO));
    }

    @Test
    public void noTimeTrackedForOneTick() {
        tracker.tick();
        assertThat(tracker.sumUpToday(), is(Duration.ZERO));
    }

    @Test
    public void trackOneHour() {
        tracker.tick();
        clock.advance(standardHours(1));
        tracker.tick();
        assertThat(tracker.sumUpToday(), is(standardHours(1)));
    }

    @Test
    public void trackTwoIntervals() {
        tracker.tick();
        clock.advance(standardHours(1).plus(standardMinutes(30)));
        tracker.tick();
        tracker.tick();
        clock.advance(standardMinutes(30));
        tracker.tick();
        assertThat(tracker.sumUpToday(), is(standardHours(2)));
    }

    @Test
    public void trackDayOverlap() {
        tracker.tick();
        clock.advance(standardHours(2));
        tracker.tick();
        clock.advance(standardDays(1));
        tracker.tick();
        clock.advance(standardHours(1));
        tracker.tick();
        assertThat(tracker.sumUpDay(clock.now()), is(standardHours(1)));
        assertThat(tracker.sumUpDay(clock.now().minus(standardDays(1))), is(standardHours(2)));
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        clock = new TestingClock();
        tracker = new TimeTracker(clock);
    }
}
