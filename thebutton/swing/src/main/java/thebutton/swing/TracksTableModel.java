package thebutton.swing;

import org.joda.time.DateTime;
import thebutton.track.*;

import javax.swing.table.AbstractTableModel;

import static thebutton.swing.TrackTableColumn.columnByNumber;
import static thebutton.swing.TrackTableColumn.numberOfColumns;

/**
 * TableModel for the "tracks" in the time tracker
 *
 * @author: aavisv
 * @created: 2010-05-17 9:57:52 PM
 */
class TracksTableModel extends AbstractTableModel implements TrackFollower {
    private final ButtonResources resources;
    private TimeFormat timeFormatter;
    private TracksList tracksList;

    public TracksTableModel(ButtonResources resources, TimeFormat timeFormatter) {
        this.resources = resources;
        this.timeFormatter = timeFormatter;
        tracksList = new TracksList();
    }

    @Override
    public int getRowCount() {
        return tracks().count();
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns();
    }

    @Override
    public String getColumnName(int column) {
        return resources.columnName(columnByNumber(column));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Track track = tracks().track(rowIndex);
        if (columnIndex == 0) {
            final DateTime startingInstant = track.start();
            return timeFormatter.timeOfDay(startingInstant);
        } else if (columnIndex == 1) {
            final DateTime endingInstant = track.stop();
            return timeFormatter.timeOfDay(endingInstant);
        } else {
            return track.task();
        }
    }

    private Tracks tracks() {
        return tracksList;
    }

    @Override
    public void add(Track o) {
        tracksList.add(o);
    }
}
