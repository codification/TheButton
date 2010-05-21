package thebutton.track;

import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.joda.time.Duration.standardSeconds;
import static thebutton.track.Track.start;

/**
 * @author: aavisv
 * @created: 2010-05-20 2:33:21 PM
 */
public class TrackTest {
    private ReadableDuration fiveSeconds;
    private Instant now;
    private Instant then;
    private String task;

    @Test
    public void
    hasADuration() {
        Track track = start(then).stop(now);
        assertThat(track.duration(), equalTo(fiveSeconds));
    }

    @Test
    public void
    hasATask() {
        Track track = start(then).doing(task).stop(now);
        assertThat(track.task(), equalTo(task));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void
    noNegativeDuration() {
        start(now).stop(then);
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        fiveSeconds = standardSeconds(5);
        now = new Instant();
        then = now.minus(fiveSeconds);
        task = "task";
    }
}
