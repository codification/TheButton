package thebutton.track;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author: aavisv
 * @created: 2010-05-20 2:55:02 PM
 */
public class TickerTrackerTest {
    private TestingClock clock;
    private TickerTracker tickerTracker;

    @Test
    public void
    setsTaskNameForNewTrack() {
        tickerTracker.tick();
        String taskname = "myTask";
        tickerTracker.tick(taskname);

        Tracks tracks = tickerTracker.todays();
        assertThat(tracks.count(), is(1));
        assertThat(tracks.first(), hasTask(taskname));

    }

    @Test
    public void
    knowsWhenItsIdle() throws Exception {
        assertThat("is idle", tickerTracker.isIdle(), is(true));
        tickerTracker.tick();
        assertThat("is not idle", tickerTracker.isIdle(), is(false));
        assertThat("is running", tickerTracker.isTracking(), is(true));
        tickerTracker.tick();
        assertThat("is idle", tickerTracker.isIdle(), is(true));
    }


    @BeforeMethod
    public void
    createTracker() {
        clock = new TestingClock();
        tickerTracker = new TickerTracker(clock);
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
}
