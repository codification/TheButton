/**
 * Purpose
 * @author aavisv
 * @created 3 Feb, 2010
 * $Id$
 */
package track;

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
}
