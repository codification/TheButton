/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
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
        setLayout(new GridBagLayout());
        final Container rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        final GridBagConstraints rootConstraints = new GridBagConstraints();
        rootConstraints.fill = GridBagConstraints.BOTH;
        add(rootPanel, rootConstraints);

        final GridBagConstraints constraints = new GridBagConstraints();
        final Container buttonPanel = new JPanel();
        rootPanel.add(buttonPanel, constraints);

        final Container trackPanel = new JPanel();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 1;
        rootPanel.add(trackPanel, constraints);

        button = createTheButton();
        buttonPanel.add(button);

        track = createTrack();
        final JScrollPane scrollPane = new JScrollPane(track);
        GridBagConstraints trackConstraints = new GridBagConstraints();
        trackConstraints.fill = GridBagConstraints.BOTH;
        trackConstraints.gridwidth = GridBagConstraints.REMAINDER;
        trackPanel.add(scrollPane, constraints);
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
                return new LocalTime(startingInstant);
            } else {
                final DateTime endingInstant = timeTracker.tracksToday().track(rowIndex).getEnd();
                return new LocalTime(endingInstant);
            }
        }

    }
}
