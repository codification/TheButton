/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import org.joda.time.DateTime;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import thebutton.track.TimeTracker;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOError;
import java.io.IOException;
import java.util.ResourceBundle;

public class ButtonFrame extends JFrame {
    private ResourceBundle resources;
    private final TimeTracker timeTracker;
    private JButton button;
    private IntervalsTableModel tracksModel;
    private PeriodFormatter runningTrackFormatter;
    private Timer timer;

    public ButtonFrame(ResourceBundle resources, TimeTracker timeTracker) throws HeadlessException {
        super(resources.getString(ButtonResources.BUTTON_FRAME_TITLE));
        this.resources = resources;
        this.timeTracker = timeTracker;
        runningTrackFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .appendHours()
                .appendSuffix("h:")
                .appendMinutes()
                .appendSuffix("m:")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateButtonText();
            }
        });
        try {
            init();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    protected void init() throws IOException {
        setLayout(new BorderLayout());
        setIconImage(ImageIO.read(getClass().getResourceAsStream("/img.png")));

        final Container buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.NORTH);

        final Container trackPanel = new JPanel(new BorderLayout());
        add(trackPanel, BorderLayout.CENTER);

        button = createTheButton();
        buttonPanel.add(button);

        JTable track = createTrack();
        final Component scrollPane = new JScrollPane(track);

        trackPanel.add(scrollPane, BorderLayout.CENTER);
        pack();

        timer.start();
    }

    public void updateButtonText() {
        if (timeTracker.isTracking()) {
            button.setText(timeTracker.currentTrackLength().toPeriod(PeriodType.time()).toString(runningTrackFormatter));
            button.repaint();
        }
    }

    private JTable createTrack() {
        tracksModel = new IntervalsTableModel(resources, timeTracker);
        final JTable trackTable = new JTable(tracksModel);
        trackTable.setName("the.track");
        trackTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        trackTable.setFillsViewportHeight(true);
        return trackTable;
    }

    private JButton createTheButton() throws IOException {
        final JButton button = new JButton();
        final Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPushed();
            }
        };

        button.setAction(action);
        button.setName("the.button");
        button.setText(resources.getString(ButtonResources.BUTTON_BUTTON_IDLE));
        //button.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img.png"))));
        //button.setBackground(Color.WHITE);
        button.setToolTipText(resources.getString("button.tooltip"));
        return button;
    }

    private void buttonPushed() {
        timeTracker.tick();
        tracksModel.fireTableDataChanged();
    }

    private static class IntervalsTableModel extends AbstractTableModel {
        private final ResourceBundle resources;
        private final TimeTracker timeTracker;

        public IntervalsTableModel(ResourceBundle resources, TimeTracker timeTracker) {
            this.resources = resources;
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
        public String getColumnName(int column) {
            if (column == 0) {
                return resources.getString("track.column.a");
            } else {
                return resources.getString("track.column.b");
            }

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

        private String toTimeString(ReadableDateTime instant) {
            return instant.toString("HH:mm:ss");
        }

    }
}
