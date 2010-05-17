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

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.joda.time.Duration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import thebutton.track.TestingClock;
import thebutton.track.TimeTracker;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.fest.assertions.Assertions.assertThat;

public class ButtonBehaviour {
    private FrameFixture window;
    private ResourceBundle resourceBundle;
    private TestingClock clock;
    private TimeTracker timeTracker;
    private ButtonFrame frame;

    @BeforeMethod
    public void setUp() {
        clock = new TestingClock();
        timeTracker = new TimeTracker(clock);
        resourceBundle = ButtonResources.lookupResources();
        frame = GuiActionRunner.execute(new GuiQuery<ButtonFrame>() {
            @Override
            protected ButtonFrame executeInEDT() {
                return new ButtonFrame(resourceBundle, timeTracker, new Timer(1000, null));
            }
        });
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test

    }

    @Test
    public void hasAButtonLabeledIdle() {
        theButton().requireEnabled();
        theButton().requireFocused();
        requireCaptionIsIdle();
    }

    @Test
    public void hasATableWithTwoColumns() {
        theTracks().requireRowCount(0);
        theTracks().requireColumnCount(2);
    }

    private JTableFixture theTracks() {
        return window.table("the.track");
    }

    @Test(dependsOnMethods = {"hasAButtonLabeledIdle", "hasATableWithTwoColumns"})
    public void noRowsForOneClickOnTheButton() {
        clickTheButton();
        theTracks().requireRowCount(0);
    }

    @Test(dependsOnMethods = {"hasAButtonLabeledIdle"})
    public void buttonTextChangesFromIdleAndBack() {
        clickTheButton();
        fiveSecondsPass();
        frameTimeIsUpdated();
        requireCaptionIsNotIdle();
        clickTheButton();
        requireCaptionIsIdle();

    }

    @Test(dependsOnMethods = {"hasAButtonLabeledIdle", "hasATableWithTwoColumns"})
    public void newRowAfterTwoClicksOnTheButton() {
        clickTheButton();
        oneHourPasses();
        clickTheButton();
        hasRows(1);
    }

    @Test(dependsOnMethods = {"hasAButtonLabeledIdle"})
    public void updatesTimeFromStart() {
        requireTimeSinceStarted("");
        clickTheButton();
        oneHourPasses();
        frameTimeIsUpdated();
        String oneHour = "01h:00m";
        requireTimeSinceStarted(oneHour);
        clickTheButton();
        oneHourPasses();
        requireTimeSinceStarted(oneHour);
        frameTimeIsUpdated();
        requireTimeSinceStarted("02h:00m");
    }

    @Test
    public void windowTitleIsInitiallyIdle() throws Exception {
        assertThat(window.component().getTitle()).isEqualTo("The Button - " + idleCaption());
    }

    @Test
    public void windowTitleUpdatesWithRunningTrack() throws Exception {
        clickTheButton();
        oneHourPasses();
        frameTimeIsUpdated();
        assertThat(window.component().getTitle()).isEqualTo("The Button - " + "01h:00m");
    }

    private void requireCaptionIsNotIdle() {
        assertThat(theButton().text()).isNotEqualTo(idleCaption());
    }

    private void requireCaptionIsIdle() {
        theButton().requireText(idleCaption());
    }

    private void fiveSecondsPass() {
        clock.advance(Duration.standardSeconds(5));
    }

    private String idleCaption() {
        return resourceBundle.getString(ButtonResources.BUTTON_BUTTON_IDLE);
    }


    private void clickTheButton() {
        theButton().click();
    }

    private void frameTimeIsUpdated() {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                frame.updateTime(); // Simulate a timer
            }
        });
    }

    private void hasRows(int noRows) {
        theTracks().requireRowCount(noRows);
    }

    private void oneHourPasses() {
        clock.advance(Duration.standardHours(1));
    }

    private void requireTimeSinceStarted(String time) {
        sinceStartedField().requireText(time);
    }

    private JTextComponentFixture sinceStartedField() {
        return window.textBox("sum.fromStart");
    }

    private JButtonFixture theButton() {
        return window.button("the.button");
    }

    @AfterMethod
    public void tearDown() {
        window.cleanUp();
    }
}
