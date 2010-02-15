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
