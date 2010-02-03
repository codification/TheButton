/**
 * Purpose
 * @author aavisv
 * @created 3 Feb, 2010
 * $Id$
 */
package track;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.Collection;

public class Periods {
    private final Collection<Interval> intervals;

    public Periods(Collection<Interval> intervals) {
        this.intervals = intervals;
    }

    public int count() {
        return intervals.size();
    }

    public Duration totalDuration() {
        final DurationAggregatingClosure durationAggregatingClosure = new DurationAggregatingClosure();
        CollectionUtils.forAllDo(intervals, durationAggregatingClosure);
        return durationAggregatingClosure.sumTotal();
    }

    private static class DurationAggregatingClosure implements Closure {
        private Duration result;

        public DurationAggregatingClosure() {
            this.result = Duration.ZERO;
        }

        @Override
        public void execute(Object input) {
            Interval interval = (Interval) input;
            result = result.plus(interval.toDuration());
        }

        public Duration sumTotal() {
            return result;
        }
    }
}
