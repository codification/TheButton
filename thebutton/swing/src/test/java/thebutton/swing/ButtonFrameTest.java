package thebutton.swing;

import org.joda.time.Duration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import thebutton.track.Ticker;
import thebutton.track.TickerTracker;
import thebutton.track.TrackFollower;

import javax.swing.*;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author: aavisv
 * @created: 2010-05-21 9:13:18 PM
 */
public class ButtonFrameTest {
    private TickerTracker tracker;
    private Ticker ticker;
    private ButtonFrame buttonFrame;
    private ButtonResources resources;

    @Test
    public void
    setsModelAsListener() throws IOException {
        buttonFrame.init();
        verify(tracker, atLeastOnce()).setTrackFollower(any(TrackFollower.class));
        verifyZeroInteractions(ticker);
    }

    @Test
    public void
    titleIsInitiallyIdle() throws Exception {
        buttonFrame.getTitle();
        verify(resources).idleTitle();
    }

    @Test
    public void
    titleChangesWhenRunning() throws Exception {
        setupTrackerAsRunning();
        buttonFrame.updateTime();
        buttonFrame.getTitle();
        verify(resources).runningTitle(anyString());
    }

    private void setupTrackerAsRunning() {
        when(tracker.isIdle()).thenReturn(false);
        when(tracker.sinceStarted()).thenReturn(Duration.ZERO);
        when(tracker.runningTime()).thenReturn(Duration.ZERO);
    }

    private TickerTracker anIdleTracker() {
        final TickerTracker tickerTracker = mock(TickerTracker.class);
        when(tickerTracker.isIdle()).thenReturn(Boolean.TRUE);
        return tickerTracker;
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        tracker = anIdleTracker();
        ticker = mock(Ticker.class);
        resources = mock(ButtonResources.class);
        buttonFrame = new ButtonFrame(tracker, ticker, new Timer(0, null), resources);
    }
}
