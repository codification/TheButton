package thebutton.swing;

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
    @Test
    public void
    setsModelAsListener() throws IOException {
        final TickerTracker tracker = anIdleTracker();
        final Ticker ticker = mock(Ticker.class);
        final ButtonFrame buttonFrame = new ButtonFrame(tracker, ticker, new Timer(0, null), mock(ButtonResources.class));
        buttonFrame.init();
        verify(tracker, atLeastOnce()).setTrackFollower(any(TrackFollower.class));
        verifyZeroInteractions(ticker);
    }

    private TickerTracker anIdleTracker() {
        final TickerTracker tickerTracker = mock(TickerTracker.class);
        when(tickerTracker.isIdle()).thenReturn(Boolean.TRUE);
        return tickerTracker;
    }
}
