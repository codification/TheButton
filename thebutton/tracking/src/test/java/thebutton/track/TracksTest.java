/**
 * Purpose
 * @author aavisv
 * @created 3 Feb, 2010
 * $Id$
 */
package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TracksTest {
    @Test
    public void zeroDurationForNoIntervals() {
        final Tracks tracks = new Tracks(Collections.<Interval>emptyList());
        assertThat(tracks.totalDuration(), is(Duration.ZERO));
    }

    @Test
    public void oneHourDurationForOneHourPeriod() {
        final Tracks tracks = new Tracks(Collections.<Interval>singletonList(new Interval(Duration.standardHours(1), new Instant())));
        assertThat(tracks.totalDuration(), is(Duration.standardHours(1)));
    }
    @Test
    public void oneHourDurationForTwo30MinPeriods() {
        final Tracks tracks = new Tracks(Arrays.asList(
                new Interval(Duration.standardMinutes(30), new Instant()),
                new Interval(Duration.standardMinutes(30), new Instant().plus(Duration.standardHours(2)))));
        assertThat(tracks.totalDuration(), is(Duration.standardHours(1)));
    }

}
