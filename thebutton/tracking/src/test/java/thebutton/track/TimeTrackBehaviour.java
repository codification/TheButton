/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.Duration.*;

public class TimeTrackBehaviour {
    private TimeTracker tracker;
    private TestingClock clock;

    @Test
    public void noTimeTrackedWhenNotTicked() {
        assertTodaysDuration(Duration.ZERO);
        final int periodCount = 0;
        assertPeriodsCount(periodCount);
    }

    private void assertPeriodsCount(int periodCount) {
        assertThat(tracker.tracksToday().count(), is(periodCount));
    }

    private void assertTodaysDuration(Duration zero) {
        assertThat(tracker.sumUpToday(), is(zero));
    }

    @Test
    public void noTimeTrackedForOneTick() {
        tracker.tick();
        assertTodaysDuration(Duration.ZERO);
        assertPeriodsCount(0);
    }

    @Test
    public void trackOneHour() {
        tracker.tick();
        clock.advance(standardHours(1));
        tracker.tick();
        assertTodaysDuration(standardHours(1));
        assertPeriodsCount(1);
    }

    @Test
    public void trackTwoIntervals() {
        tracker.tick();
        clock.advance(standardHours(1).plus(standardMinutes(30)));
        tracker.tick();
        tracker.tick();
        clock.advance(standardMinutes(30));
        tracker.tick();
        assertTodaysDuration(standardHours(2));
        assertPeriodsCount(2);
    }

    @Test
    public void trackNiceDayOverlap() {
        tracker.tick();
        clock.advance(standardHours(2));
        tracker.tick();
        clock.advance(standardDays(1));
        tracker.tick();
        clock.advance(standardHours(1));
        tracker.tick();
        assertThat(tracker.sumUpDay(new LocalDate(clock.now())), is(standardHours(1)));
        assertThat(tracker.sumUpDay(new LocalDate(clock.now().minus(standardDays(1)))), is(standardHours(2)));
    }

    @Test
    public void timeSinceStart() {
        tracker.tick();
        final Duration duration = Duration.standardSeconds(5);
        clock.advance(duration);
        assertThat(tracker.currentTrackLength(), is(duration));
    }

    @Test
    public void noTimeWhenNoTick() {
        final Duration duration = Duration.standardSeconds(5);
        clock.advance(duration);
        assertThat(tracker.currentTrackLength(), is(Duration.ZERO));
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        clock = new TestingClock();
        tracker = new TimeTracker(clock);
    }
}
