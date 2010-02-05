package thebutton.track;

import org.joda.time.Instant;

/**
 * Purpose
 *
 * @author aavisv
 * @created 2 Feb, 2010
 * $Id$
 */
public interface Clock {
    Instant now();

    public static final Clock SYSTEMCLOCK = new Clock() {
        @Override
        public Instant now() {
            return new Instant();
        }
    };
}
