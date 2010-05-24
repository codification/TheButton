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

import thebutton.track.Ticker;
import thebutton.track.TickerTracker;
import thebutton.track.TimeFormat;
import thebutton.track.TimeTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOError;
import java.io.IOException;

public class ButtonFrame extends JFrame {
    private final TimeTracker timeTracker;
    private TracksTableModel tracksModel;
    private JButton button;
    private JTextField sinceStarted;

    private final TimeFormat timeFormat = new TimeFormat();
    private ButtonResources resources;
    private JTextField taskField;
    private final Ticker ticker;

    public ButtonFrame(TickerTracker timeTracker, Ticker ticker, Timer timer, ButtonResources resources) throws HeadlessException {
        super("");
        this.resources = resources;
        this.timeTracker = timeTracker;
        this.ticker = ticker;
        timer.addActionListener(updateAction());

        try {
            init();
        } catch (IOException e) {
            throw new IOError(e);
        }
        setTitle(title(timeTracker));
    }

    private String title(TimeTracker timeTracker) {
        if (timeTracker.isIdle()) {
            return resources.idleTitle();
        } else {
            return resources.runningTitle(timeFormat.usingMinutes(timeTracker.runningTime()));
        }

    }

    private ActionListener updateAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        };
    }


    protected void init() throws IOException {
        setLayout(new BorderLayout());
        setIconImage(resources.appIconImage());

        final Container buttonPanel = new JPanel(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);

        button = createTheButton();
        buttonPanel.add(button, BorderLayout.CENTER);

        JPanel taskPanel = new JPanel();
        JLabel taskLabel = new JLabel(resources.taskLabel());
        taskLabel.setName("task.label");
        taskPanel.add(taskLabel);
        taskField = new JTextField(30);
        taskField.setName("task.name");
        taskPanel.add(taskField);

        buttonPanel.add(taskPanel, BorderLayout.SOUTH);

        JPanel sinceStartedPanel = new JPanel();
        sinceStartedPanel.add(new JLabel(resources.sinceStartedLabel()));
        sinceStarted = new JTextField(8);
        sinceStarted.setName("sum.fromStart");
        sinceStartedPanel.add(sinceStarted);

        final Container trackPanel = new JPanel(new BorderLayout());
        add(trackPanel, BorderLayout.CENTER);

        JTable track = createTracks();
        final Component scrollPane = new JScrollPane(track);

        trackPanel.add(scrollPane, BorderLayout.CENTER);
        trackPanel.add(sinceStartedPanel, BorderLayout.SOUTH);

        pack();

    }

    public void updateTime() {
        if (timeTracker.isIdle()) {
            button.setText(resources.idle());
        } else {
            button.setText(runningTimeRepresentation());
        }
        sinceStarted.setText(totalDuration());

        button.repaint();
        sinceStarted.repaint();
        setTitle(title(timeTracker));
    }

    private String runningTimeRepresentation() {
        return timeFormat.usingSeconds(timeTracker.runningTime());
    }

    private String totalDuration() {
        return timeFormat.usingMinutes(timeTracker.sinceStarted());
    }

    private JTable createTracks() {
        tracksModel = new TracksTableModel(resources, timeFormat);
        timeTracker.setTrackFollower(tracksModel);

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
        button.setText(resources.idle());
        button.setToolTipText(resources.buttonTooltip());
        return button;
    }

    private void buttonPushed() {
        ticker.tic(taskField.getText());
        tracksModel.fireTableDataChanged();
        updateTime();
    }

}
