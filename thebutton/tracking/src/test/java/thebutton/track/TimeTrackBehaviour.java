
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
import org.joda.time.LocalDate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.Duration.*;

public class TimeTrackBehaviour {
    private TimeTracker tracker;
    private TestingClock clock;
    private Duration fiveMinutes;
    private Duration fiveSeconds;

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
    public void currentTrackLength() {
        tracker.tick();
        clock.advance(fiveSeconds);
        assertThat(tracker.currentTrackLength(), is(fiveSeconds));
    }

    @Test
    public void timeSinceStartZeroToBeginWith() {
        assertThat(tracker.sinceStarted(), is(Duration.ZERO));
    }

    @Test
    public void timeSinceStartOnOneClick() {
        tracker.tick();
        clock.advance(fiveMinutes);
        assertThat(tracker.sinceStarted(), is(fiveMinutes));
    }
    @Test
    public void timeSinceStartOnTwoClicks() {
        tracker.tick();
        clock.advance(fiveMinutes);
        tracker.tick();
        clock.advance(fiveMinutes);
        assertThat(tracker.sinceStarted(), is(fiveMinutes.plus(fiveMinutes)));
    }

    @Test
    public void noTimeWhenNoTick() {
        clock.advance(fiveSeconds);
        assertThat(tracker.currentTrackLength(), is(Duration.ZERO));
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        clock = new TestingClock();
        tracker = new TimeTracker(clock);
        fiveMinutes = Duration.standardMinutes(5);
        fiveSeconds = Duration.standardSeconds(5);
    }
}
