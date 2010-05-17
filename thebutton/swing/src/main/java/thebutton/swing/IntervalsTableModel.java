package thebutton.swing;

import org.joda.time.DateTime;
import thebutton.track.TimeStringFormatter;
import thebutton.track.TimeTracker;

import javax.swing.table.AbstractTableModel;
import java.util.ResourceBundle;

import static thebutton.swing.ButtonResources.column;

/**
 * TableModel for the "tracks" in the time tracker
 *
 * @author: aavisv
 * @created: 2010-05-17 9:57:52 PM
 */
class IntervalsTableModel extends AbstractTableModel {
    private final ResourceBundle resources;
    private final TimeTracker timeTracker;
    private TimeStringFormatter timeFormatter;

    public IntervalsTableModel(ResourceBundle resources, TimeTracker timeTracker, TimeStringFormatter timeFormatter) {
        this.resources = resources;
        this.timeTracker = timeTracker;
        this.timeFormatter = timeFormatter;
    }

    @Override
    public int getRowCount() {
        return timeTracker.tracksToday().count();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return column(resources, "a");
        } else {
            return column(resources, "b");
        }

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            final DateTime startingInstant = timeTracker.tracksToday().track(rowIndex).getStart();
            return timeFormatter.timeOfDay(startingInstant);
        } else {
            final DateTime endingInstant = timeTracker.tracksToday().track(rowIndex).getEnd();
            return timeFormatter.timeOfDay(endingInstant);
        }
    }

}
