/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package track;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

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
        return sumUpDay(clock.now());
    }

    private Duration durationBetweenTicks(Instant start, Instant end) {
        return new Interval(start, end).toDuration();
    }

    public void tick() {
        ticks.add(clock.now());
    }

    public Duration sumUpDay(Instant day) {
        Duration soFar = Duration.ZERO;
        final Iterator<Instant> iterator = ticks.iterator();
        while (iterator.hasNext()) {
            final Instant startTick = iterator.next();
            final Instant endTick = (iterator.hasNext())? iterator.next() : startTick;
            if (isSameDay(day, startTick)) {
                soFar = soFar.plus(durationBetweenTicks(startTick, endTick));
            }
        }
        return soFar;
    }

    private boolean isSameDay(Instant day1, Instant day2) {
        return day2.toDateTime().dayOfYear().equals(day1.toDateTime().dayOfYear());
    }
}
