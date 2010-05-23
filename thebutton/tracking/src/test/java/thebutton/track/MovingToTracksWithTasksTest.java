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
import static org.mockito.Matchers.argThat;

/**
 * @author: aavisv
 * @created: 2010-05-20 2:55:02 PM
 */
public class MovingToTracksWithTasksTest {
    private TestingClock clock;
    private TickerTracker tickerTracker;
    private Tracks tracks;
    private TrackFollower trackFollower;

    @Test
    public void
    setsTaskNameForNewTrack() {
        tickerTracker.tic();
        String taskname = "myTask";
        tickerTracker.toc(taskname);
        Mockito.verify(trackFollower).add(argThat(hasTask(taskname)));

    }

    @Test
    public void
    knowsWhenItsIdle() throws Exception {
        assertThat("is idle", tickerTracker.isIdle(), is(true));
        tickerTracker.tic();
        assertThat("is not idle", tickerTracker.isIdle(), is(false));
        assertThat("is running", tickerTracker.isTracking(), is(true));
        tickerTracker.tic();
        assertThat("is idle", tickerTracker.isIdle(), is(true));
    }

    @Test
    public void
    addsTracks() throws Exception {
        final TickerTracker tracksTracker = new TickerTracker(clock, tracks);
        tracksTracker.tic();
        final Duration duration = Duration.standardSeconds(5);
        clock.advance(duration);
        final String task = "sth";
        tracksTracker.toc(task);
        final ArgumentCaptor<Track> captor = ArgumentCaptor.forClass(Track.class);
        Mockito.verify(tracks).add(captor.capture());
        assertThat("with duration", (Duration) captor.getValue().duration(), IsEqual.equalTo(duration));
        assertThat("with task", captor.getValue().task(), IsEqual.equalTo(task));
    }

    @BeforeMethod
    public void
    createTracker() {
        clock = new TestingClock();
        tickerTracker = new TickerTracker(clock);
        tracks = Mockito.mock(Tracks.class);
        trackFollower = Mockito.mock(TrackFollower.class);
        tickerTracker.setTrackFollower(trackFollower);
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
