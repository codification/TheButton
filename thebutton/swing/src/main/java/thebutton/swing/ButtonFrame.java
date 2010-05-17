/*
 * Copyright (c) 2010, Ville Svärd
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    private IntervalsTableModel tracksModel;
    private JButton button;
    private JTextField sinceStarted;

    private static final PeriodFormatter PERIOD_FORMATTER_MINUTES;
    private static final PeriodFormatter PERIOD_FORMATTER_SECONDS;

    static {
        PeriodFormatterBuilder formatterBuilder = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSuffix("h")
                .appendSeparator(":")
                .appendMinutes()
                .appendSuffix("m");
        PERIOD_FORMATTER_MINUTES = formatterBuilder.toFormatter();
        formatterBuilder = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSuffix("h")
                .appendSeparator(":")
                .appendMinutes()
                .appendSuffix("m");
        PERIOD_FORMATTER_SECONDS = formatterBuilder
                .appendSeparator(":")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
    }

    public ButtonFrame(ResourceBundle resources, TimeTracker timeTracker, Timer timer) throws HeadlessException {
        super("");
        this.resources = resources;
        this.timeTracker = timeTracker;
        timer.addActionListener(updateAction());

        try {
            init();
        } catch (IOException e) {
            throw new IOError(e);
        }
        updateTitle(resources);
    }

    private void updateTitle(ResourceBundle resources) {
        setTitle(title(resources));
    }

    private String title(ResourceBundle resources) {
        if (timeTracker.isIdle()) {
            return formTitle(resources.getString(ButtonResources.BUTTON_FRAME_TITLE), resources.getString(ButtonResources.BUTTON_BUTTON_IDLE));
        } else {
            return formTitle(resources.getString(ButtonResources.BUTTON_FRAME_TITLE), timeTracker.currentTrackLength().toPeriod(PeriodType.time()).toString(PERIOD_FORMATTER_MINUTES));
        }

    }

    private String formTitle(String leading, String trailing) {
        return leading + " - " + trailing;
    }

    private ActionListener updateAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        };
        return actionListener;
    }


    protected void init() throws IOException {
        setLayout(new BorderLayout());
        setIconImage(ImageIO.read(getClass().getResourceAsStream("/img.png")));

        final Container buttonPanel = new JPanel(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);

        button = createTheButton();
        buttonPanel.add(button, BorderLayout.CENTER);

        JPanel fieldsPanel = new JPanel();
        buttonPanel.add(fieldsPanel, BorderLayout.SOUTH);
        fieldsPanel.add(new JLabel(resources.getString("button.total.sinceStart.title")));
        sinceStarted = new JTextField(8);
        sinceStarted.setName("sum.fromStart");
        fieldsPanel.add(sinceStarted);

        final Container trackPanel = new JPanel(new BorderLayout());
        add(trackPanel, BorderLayout.CENTER);

        JTable track = createTrack();
        final Component scrollPane = new JScrollPane(track);

        trackPanel.add(scrollPane, BorderLayout.CENTER);

        pack();

    }

    public void updateTime() {
        if (timeTracker.isTracking()) {
            button.setText(timeTracker.currentTrackLength().toPeriod(PeriodType.time()).toString(PERIOD_FORMATTER_SECONDS));
        } else {
            button.setText(resources.getString("button.button.title.idle"));
        }
        button.repaint();
        sinceStarted.setText(timeTracker.sinceStarted().toPeriod(PeriodType.time()).toString(PERIOD_FORMATTER_MINUTES));
        sinceStarted.repaint();
        updateTitle(resources);
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
                updateTime();
            }
        };

        button.setAction(action);
        button.setName("the.button");
        button.setText(resources.getString(ButtonResources.BUTTON_BUTTON_IDLE));
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
                return resources.getString("button.track.column.a");
            } else {
                return resources.getString("button.track.column.b");
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
