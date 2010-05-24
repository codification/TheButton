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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import org.joda.time.Duration;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.Duration.*;
import static org.mockito.Mockito.*;

public class TickerTrackerTest {
    private TickerTracker tracker;
    private TestingClock clock;
    private Duration fiveMinutes;
    private Duration fiveSeconds;
    private TrackFollower follower;

    @Test
    public void noTimeTrackedForOneTick() {
        tracker.tic();
        assertThatNoTracksWereCreated();
    }

    private void assertThatNoTracksWereCreated() {
        verifyZeroInteractions(follower);
    }

    @Test
    public void
    knowsWhenItsIdle() throws Exception {
        assertThat("is idle", tracker.isIdle(), is(true));
        tracker.tic();
        assertThat("is not idle", tracker.isIdle(), is(false));
        tracker.tic();
        assertThat("is idle", tracker.isIdle(), is(true));
    }

    @Test
    public void trackOneHour() {
        tracker.tic();
        final Duration duration = standardHours(1);
        clock.advance(duration);
        tracker.tic();
        verify(follower).add(Mockito.argThat(aTrackWithDuration(duration)));
    }

    @Test
    public void trackTwoIntervals() {
        tracker.tic();
        final Duration firstDuration = standardHours(1).plus(standardMinutes(30));
        clock.advance(firstDuration);
        tracker.tic();
        tracker.tic();
        final Duration secondDuration = standardMinutes(30);
        clock.advance(secondDuration);
        tracker.tic();
        verify(follower).add(Mockito.argThat(aTrackWithDuration(firstDuration)));
        verify(follower).add(Mockito.argThat(aTrackWithDuration(secondDuration)));
    }

    private TypeSafeMatcher<Track> aTrackWithDuration(final Duration duration) {
        return new TypeSafeMatcher<Track>() {
            @Override
            public boolean matchesSafely(Track track) {
                return track.duration().equals(duration);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a track with a duration of " + duration);
            }
        };
    }

    @Test
    public void currentTrackLength() {
        tracker.tic();
        clock.advance(fiveSeconds);
        assertThat(tracker.runningTime(), is(fiveSeconds));
    }

    @Test
    public void timeSinceStartZeroToBeginWith() {
        assertThat(tracker.sinceStarted(), is(Duration.ZERO));
    }

    @Test
    public void timeSinceStartOnOneClick() {
        tracker.tic();
        clock.advance(fiveMinutes);
        assertThat(tracker.sinceStarted(), is(fiveMinutes));
    }

    @Test
    public void timeSinceStartOnTwoClicks() {
        tracker.tic();
        clock.advance(fiveMinutes);
        tracker.tic();
        clock.advance(fiveMinutes);
        assertThat(tracker.sinceStarted(), is(fiveMinutes.plus(fiveMinutes)));
    }

    @Test
    public void noTimeWhenNoTick() {
        clock.advance(fiveSeconds);
        assertThat(tracker.runningTime(), is(Duration.ZERO));
    }

    @Test
    public void
    setsTaskNameForNewTrack() {
        tracker.tic();
        String taskname = "myTask";
        tracker.tic(taskname);
        Mockito.verify(follower).add(argThat(hasTask(taskname)));

    }

    @Test
    public void
    addsTracks() throws Exception {
        tracker.tic();
        final Duration duration = standardSeconds(5);
        clock.advance(duration);
        final String task = "sth";
        tracker.tic(task);
        final ArgumentCaptor<Track> captor = ArgumentCaptor.forClass(Track.class);
        Mockito.verify(follower).add(captor.capture());
        assertThat("with duration", (Duration) captor.getValue().duration(), IsEqual.equalTo(duration));
        assertThat("with task", captor.getValue().task(), IsEqual.equalTo(task));
    }

    private Matcher<Track> hasTask(final String taskname) {
        return new TypeSafeMatcher<Track>() {
            @Override
            public boolean matchesSafely(Track track) {
                return track.task().equals(taskname);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a track with the task: " + taskname);
            }
        };
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        clock = new TestingClock();
        tracker = new TickerTracker(clock);
        fiveMinutes = Duration.standardMinutes(5);
        fiveSeconds = standardSeconds(5);
        follower = mock(TrackFollower.class);
        tracker.setTrackFollower(follower);
    }
}
