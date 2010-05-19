package thebutton.swing;

import org.joda.time.DateTime;
import thebutton.track.TimeFormat;
import thebutton.track.TimeTracker;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for the "tracks" in the time tracker
 *
 * @author: aavisv
 * @created: 2010-05-17 9:57:52 PM
 */
class IntervalsTableModel extends AbstractTableModel {
    private final ButtonResources resources;
    private final TimeTracker timeTracker;
    private TimeFormat timeFormatter;

    public IntervalsTableModel(ButtonResources resources, TimeTracker timeTracker, TimeFormat timeFormatter) {
        this.resources = resources;
        this.timeTracker = timeTracker;
        this.timeFormatter = timeFormatter;
    }

    @Override
    public int getRowCount() {
        return timeTracker.todays().count();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return resources.column("a");
        } else {
            return resources.column("b");
        }

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            final DateTime startingInstant = timeTracker.todays().track(rowIndex).getStart();
            return timeFormatter.timeOfDay(startingInstant);
        } else {
            final DateTime endingInstant = timeTracker.todays().track(rowIndex).getEnd();
            return timeFormatter.timeOfDay(endingInstant);
        }
    }

}
