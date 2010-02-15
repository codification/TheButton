
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

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.Collection;
import java.util.List;

public class Tracks {
    private final List<Interval> intervals;

    public Tracks(List<Interval> intervals) {
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

    public Interval track(int trackNo) {
        return intervals.get(trackNo);
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
