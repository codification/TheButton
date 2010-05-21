package thebutton.track;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author: aavisv
 * @created: 2010-05-20 2:55:02 PM
 */
public class TickerTrackerTest {
    @Test
    public void
    setsTaskNameForNewTrack() {
        TestingClock clock = new TestingClock();
        TickerTracker tickerTracker = new TickerTracker(clock);
        tickerTracker.tick();
        String taskname = "myTask";
        tickerTracker.tick(taskname);

        Tracks tracks = tickerTracker.todays();
        assertThat(tracks.count(), is(1));
        assertThat(tracks.first(), hasTask(taskname));

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
