/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package track;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class TimeTracker {
    private final Clock clock;
    private Collection<Instant> ticks;

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
        final LinkedList<Interval> todaysIntervals = new LinkedList<Interval>();
        iterateIntervals(new IntervalsInADay(date, todaysIntervals));
        final AddIntervalDurations closure = new AddIntervalDurations();
        CollectionUtils.forAllDo(todaysIntervals, closure);
        return closure.result();
    }

    protected void iterateIntervals(Closure closure) {
        final Iterator<Instant> iterator = ticks.iterator();
        while (iterator.hasNext()) {
            final Instant startTick = iterator.next();
            if (iterator.hasNext()) {
                final Instant endTick = iterator.next();
                closure.execute(new Interval(startTick, endTick));
            }
        }
    }

    public Periods periodsToday() {
        final LocalDate today = new LocalDate(clock.now());
        return periodsFor(today);
    }

    private Periods periodsFor(LocalDate today) {
        final Collection<Interval> intervals = new LinkedList<Interval>();
        iterateIntervals(new IntervalsInADay(today, intervals));
        return new Periods(intervals);
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

    private static class AddIntervalDurations implements Closure {
        private Duration soFar;

        public AddIntervalDurations() {
            this.soFar = Duration.ZERO;
        }

        @Override
        public void execute(Object input) {
            Interval interval = (Interval) input;
            soFar = soFar.plus(interval.toDuration());
        }

        public Duration result() {
            return soFar;
        }
    }
}
