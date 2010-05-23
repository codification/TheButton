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

/**
 * Purpose
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
package thebutton.track;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

import static thebutton.track.OngoingTrack.start;

public class TickerTracker implements TimeTracker, Ticker {
    private final Clock clock;
    private TrackFollower trackFollower = TrackFollower.NULL;
    private OngoingTrack ongoingTrack;
    private Instant firstTick;

    public TickerTracker(Clock clock) {
        this.clock = clock;
        ongoingTrack = null;
        firstTick = null;
    }

    public TickerTracker(Clock clock, TrackFollower trackFollower) {
        this(clock);
        this.trackFollower = trackFollower;
    }

    @Override
    public void setTrackFollower(TrackFollower trackFollower) {
        this.trackFollower = trackFollower;
    }

    @Override
    public void tick() {
        tick(OngoingTrack.NO_TASK);
    }

    @Override
    public void tick(String taskname) {
        final Instant now = clock.now();
        if (isIdle()) {
            startTracking(now);
        } else {
            stopTracking(taskname, now);
        }
    }

    private void stopTracking(String taskname, Instant now) {
        trackFollower.add(ongoingTrack.doing(taskname).stop(now));
        ongoingTrack = null;
    }

    private void startTracking(Instant now) {
        ongoingTrack = start(now);
        if (!everBeenStarted()) {
            firstTick = now;
        }
    }

    @Override
    public Duration runningTime() {
        if (isIdle()) {
            return Duration.ZERO;
        } else {
            return new Interval(ongoingTrack.startsAt(), clock.now()).toDuration();
        }
    }

    @Override
    public boolean isIdle() {
        return ongoingTrack == null;
    }

    @Override
    public boolean isTracking() {
        return !isIdle();
    }

    @Override
    public Duration sinceStarted() {
        if (everBeenStarted()) {
            return new Interval(veryFirst(), clock.now()).toDuration();
        } else {
            return Duration.ZERO;
        }
    }

    private boolean everBeenStarted() {
        return firstTick != null;
    }

    private Instant veryFirst() {
        return firstTick;
    }

}
