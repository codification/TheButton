package thebutton.swing;

import org.joda.time.Instant;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import thebutton.track.TimeFormat;
import thebutton.track.Track;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.joda.time.Duration.standardSeconds;
import static org.mockito.Mockito.when;
import static thebutton.swing.TrackTableColumn.*;
import static thebutton.track.Track.start;

/**
 * @author: aavisv
 * @created: 2010-05-21 6:01:28 PM
 */
public class TracksModelTest {
    private ButtonResources buttonResources;
    private TracksTableModel tableModel;
    private Track track;
    private TimeFormat formatter;
    private String task;

    @Test
    public void
    addsTrack() {
        tableModel.add(track);
        assertThat("row count", tableModel.getRowCount(), is(1));
    }

    @Test
    public void
    laysOutColumnsInOrder() throws Exception {
        when(buttonResources.columnName(START)).thenReturn("0");
        when(buttonResources.columnName(STOP)).thenReturn("1");
        when(buttonResources.columnName(TASK)).thenReturn("2");
        tableModel.add(track);
        assertThat(tableModel.getColumnName(0), is("0"));
        assertThat(tableModel.getColumnName(1), is("1"));
        assertThat(tableModel.getColumnName(2), is("2"));
    }

    @Test
    public void
    laysOutTrack() throws Exception {
        tableModel.add(track);
        assertThat((String) tableModel.getValueAt(0, 0), equalTo(formatter.timeOfDay(track.start())));
        assertThat((String) tableModel.getValueAt(0, 1), equalTo(formatter.timeOfDay(track.stop())));
        assertThat((String) tableModel.getValueAt(0, 2), equalTo(task));
    }

    @Test
    public void
    returnsLastTrack() throws Exception {
        tableModel.add(track);
        assertThat(tableModel.last(), is(track));
        final Track anotherTrack = Track.start(new Instant()).stop(new Instant());
        tableModel.add(anotherTrack);
        assertThat(tableModel.last(), is(anotherTrack));
    }

    @BeforeMethod
    public void createModel() {
        buttonResources = Mockito.mock(ButtonResources.class);
        formatter = new TimeFormat();
        tableModel = new TracksTableModel(buttonResources, formatter);
        Instant start = new Instant();
        task = "something";
        track = start(start).doing(task).stop(start.plus(standardSeconds(5)));
    }
}
