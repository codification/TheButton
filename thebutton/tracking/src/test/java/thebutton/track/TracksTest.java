/*
 * Copyright (c) 2010, Ville Svärd
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.testng.annotations.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TracksTest {
    @Test
    public void zeroDurationForNoIntervals() {
        final Tracks tracks = new TracksList(Collections.<Track>emptyList());
        assertThat(tracks.totalDuration(), is(Duration.ZERO));
    }

    @Test
    public void oneHourDurationForOneHourPeriod() {
        Duration duration = Duration.standardHours(1);
        Interval interval = new Interval(duration, new Instant());
        final Tracks tracks = new TracksList(Collections.<Track>singletonList(Track.start(interval.getStart().toInstant()).stop(interval.getEnd().toInstant())));
        assertThat(tracks.totalDuration(), is(duration));
    }

    @Test
    public void oneHourDurationForTwo30MinPeriods() {
        Duration thirtyMins = Duration.standardMinutes(30);
        Instant now = new Instant();
        Duration later = Duration.standardHours(2);
        Interval interval1 = new Interval(thirtyMins, now);
        Interval interval2 = new Interval(thirtyMins, now.plus(later));
        final Tracks tracks = new TracksList(asList(
                Track.start(interval1.getStart().toInstant()).stop(interval1.getEnd().toInstant()),
                Track.start(interval1.getStart().toInstant()).stop(interval1.getEnd().toInstant())));
        assertThat(tracks.totalDuration(), is(Duration.standardHours(1)));
    }

    @Test
    public void
    createWithTracks() throws Exception {
        Instant now = new Instant();
        new TracksList(asList(Track.start(now.minus(Duration.standardSeconds(5))).stop(now)));
    }
}
