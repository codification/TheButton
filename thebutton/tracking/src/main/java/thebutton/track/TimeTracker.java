/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package thebutton.track;

import org.apache.commons.collections.Closure;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.*;

public class TimeTracker {
    private final Clock clock;
    private Deque<Instant> ticks;

    public TimeTracker(Clock clock) {
        this.clock = clock;
        ticks = new LinkedList<Instant>();
    }

    public Duration sumUpToday() {
        return sumUpDay(new LocalDate(clock.now()));
    }

    public void tick() {
        ticks.add(clock.now());
    }

    public Duration sumUpDay(LocalDate date) {
        return tracksFor(date).totalDuration();
    }

    private void iterateTicksAsIntervals(Closure closure, Collection<Instant> ticks) {
        final Iterator<Instant> iterator = ticks.iterator();
        while (iterator.hasNext()) {
            final Instant startTick = iterator.next();
            if (iterator.hasNext()) {
                final Instant endTick = iterator.next();
                closure.execute(new Interval(startTick, endTick));
            }
        }
    }

    public Tracks tracksToday() {
        final LocalDate today = new LocalDate(clock.now());
        return tracksFor(today);
    }

    public Tracks tracksFor(LocalDate date) {
        final List<Interval> intervals = new LinkedList<Interval>();
        iterateTicksAsIntervals(new IntervalsInADay(date, intervals), ticks);
        return new Tracks(intervals);
    }

    public Duration currentTrackLength() {
        if (!isIdle()) {
            return new Interval(ticks.getLast(), clock.now()).toDuration();
        } else {
            return Duration.ZERO;
        }
    }

    public boolean isIdle() {
        return ticks.size() % 2 == 0;
    }

    public boolean isTracking() {
        return !isIdle();
    }

    private static class IntervalsInADay implements Closure {
        private final LocalDate day;
        private final Collection<Interval> intervals;

        public IntervalsInADay(LocalDate day, Collection<Interval> intervals) {
            this.day = day;
            this.intervals = intervals;
        }

        @Override
        public void execute(Object input) {
            final Interval interval = (Interval) input;
            if (day.toInterval().contains(interval)) {
                intervals.add(interval);
            }
        }
    }

}
