/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import thebutton.track.TimeTracker;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class ButtonFrame extends JFrame {
    private ResourceBundle resources;
    private final TimeTracker timeTracker;
    private JTable track;
    private JButton button;
    private IntervalsTableModel tracksModel;

    public ButtonFrame(ResourceBundle resources, TimeTracker timeTracker) throws HeadlessException {
        super(resources.getString(ButtonResources.BUTTON_FRAME_TITLE));
        this.resources = resources;
        this.timeTracker = timeTracker;
        init();
    }

    protected void init() {
        setLayout(new BorderLayout());

        final Container buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.NORTH);

        final Container trackPanel = new JPanel(new BorderLayout());
        add(trackPanel, BorderLayout.CENTER);

        button = createTheButton();
        buttonPanel.add(button);

        track = createTrack();
        track.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        track.setFillsViewportHeight(true);
        final JScrollPane scrollPane = new JScrollPane(track);

        trackPanel.add(scrollPane, BorderLayout.CENTER);
        pack();
    }

    private JTable createTrack() {
        tracksModel = new IntervalsTableModel(timeTracker);
        final JTable trackTable = new JTable(tracksModel);
        trackTable.setName("the.track");
        return trackTable;
    }

    private JButton createTheButton() {
        final JButton button = new JButton();
        final AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPushed();
            }
        };

        button.setAction(action);
        button.setName("the.button");
        button.setText(resources.getString(ButtonResources.BUTTON_BUTTON_TITLE));
        button.setSize(200,75);
        return button;
    }

    private void buttonPushed() {
        timeTracker.tick();
        tracksModel.fireTableDataChanged();
    }

    private static class IntervalsTableModel extends AbstractTableModel {
        private final TimeTracker timeTracker;

        public IntervalsTableModel(TimeTracker timeTracker) {
            this.timeTracker = timeTracker;
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                final DateTime startingInstant = timeTracker.tracksToday().track(rowIndex).getStart();
                return toTimeString(startingInstant);
            } else {
                final DateTime endingInstant = timeTracker.tracksToday().track(rowIndex).getEnd();
                return toTimeString(endingInstant);
            }
        }

        private String toTimeString(DateTime instant) {
            return instant.toString("HH:mm:ss");
        }

    }
}
