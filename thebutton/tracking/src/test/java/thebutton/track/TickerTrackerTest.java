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

/**
 * @author: aavisv
 * @created: 2010-05-20 2:55:02 PM
 */
public class TickerTrackerTest {
    private TestingClock clock;
    private TickerTracker tickerTracker;
    private Tracks tracks;

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

    @Test
    public void
    addsTracks() throws Exception {
        final TickerTracker tracksTracker = new TickerTracker(clock, tracks);
        tracksTracker.tick();
        final Duration duration = Duration.standardSeconds(5);
        clock.advance(duration);
        final String task = "sth";
        tracksTracker.tick(task);
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
