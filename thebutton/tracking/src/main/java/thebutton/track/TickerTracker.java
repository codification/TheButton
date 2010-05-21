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

import org.apache.commons.collections.Closure;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.*;

public class TickerTracker implements TimeTracker {
    private final Clock clock;
    private Deque<ButtonTick> ticks;

    public TickerTracker(Clock clock) {
        this.clock = clock;
        ticks = new LinkedList<ButtonTick>();
    }

    @Override
    public Duration sumUpToday() {
        return sumUpDay(new LocalDate(clock.now()));
    }

    public void tick() {
        ticks.add(new ButtonTick(clock.now()));
    }

    @Override
    public Duration sumUpDay(LocalDate date) {
        return tracksFor(date).totalDuration();
    }

    private void iterateTicksAsIntervals(Closure closure, Collection<ButtonTick> ticks) {
        final Iterator<ButtonTick> iterator = ticks.iterator();
        while (iterator.hasNext()) {
            final Instant startTick = iterator.next().time();
            if (iterator.hasNext()) {
                ButtonTick endtick = iterator.next();
                closure.execute(Track.start(startTick).doing(endtick.task()).stop(endtick.time()));
            }
        }
    }

    @Override
    public Tracks todays() {
        final LocalDate today = new LocalDate(clock.now());
        return tracksFor(today);
    }

    @Override
    public Tracks tracksFor(LocalDate date) {
        final List<Track> intervals = new LinkedList<Track>();
        iterateTicksAsIntervals(new IntervalsInADay(date, intervals), ticks);
        return new Tracks(intervals);
    }

    @Override
    public Duration runningTime() {
        if (isIdle()) {
            return Duration.ZERO;
        } else {
            return new Interval(ticks.getLast().time(), clock.now()).toDuration();
        }
    }

    @Override
    public boolean isIdle() {
        return ticks.size() % 2 == 0;
    }

    @Override
    public boolean isTracking() {
        return !isIdle();
    }

    @Override
    public Duration sinceStarted() {
        if (ticks.isEmpty()) {
            return Duration.ZERO;
        } else {
            return new Interval(ticks.getFirst().time(), clock.now()).toDuration();
        }
    }

    public void tick(String taskname) {
        ticks.add(new ButtonTick(clock.now(), taskname));
    }

    private static class IntervalsInADay implements Closure {
        private final LocalDate day;
        private final Collection<Track> intervals;

        public IntervalsInADay(LocalDate day, Collection<Track> intervals) {
            this.day = day;
            this.intervals = intervals;
        }

        @Override
        public void execute(Object input) {
            Track track = (Track) input;
            final Interval interval = track.interval();
            if (day.toInterval().contains(interval)) {
                intervals.add(track);
            }
        }
    }

    private class ButtonTick {
        private Instant instant;
        private String task;

        private ButtonTick(Instant instant) {
            this(instant, "");
        }

        public ButtonTick(Instant instant, String taskname) {
            this.instant = instant;
            task = taskname;
        }

        public Instant time() {
            return instant;
        }

        public String task() {
            return task;
        }
    }
}