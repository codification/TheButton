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

package uitest;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uitest.harness.ButtonApplicationTester;

public class ButtonBehaviour {
    private ButtonApplicationTester buttonApp;

    @BeforeMethod
    public void setUp() {
        buttonApp = new ButtonApplicationTester();
    }

    @Test
    public void hasAButtonLabeledIdleThatIsFocused() {
        buttonApp.theButton().requireEnabled();
        buttonApp.theButton().requireFocused();
        buttonApp.requireCaptionIsIdle();
    }

    @Test
    public void hasAnEmptyTableWithTwoColumns() {
        buttonApp.requireTracks(0);
        buttonApp.requireTrackColumns(2);
    }

    @Test
    public void noRowsForOneClickOnTheButton() {
        buttonApp.clickTheButton();
        buttonApp.requireTracks(0);
    }

    @Test
    public void buttonTextChangesFromIdleAndBack() {
        buttonApp.clickTheButton();
        buttonApp.fiveSecondsPass();
        buttonApp.requireCaptionIsNotIdle();
        buttonApp.clickTheButton();
        buttonApp.requireCaptionIsIdle();

    }

    @Test
    public void newRowAfterTwoClicksOnTheButton() {
        buttonApp.clickTheButton();
        buttonApp.oneHourPasses();
        buttonApp.clickTheButton();
        buttonApp.hasRows(1);
    }

    @Test
    public void updatesTimeFromStart() {
        buttonApp.requireTimeSinceStarted("");
        buttonApp.clickTheButton();
        buttonApp.oneHourPasses();
        buttonApp.requireTimeSinceStarted("01h:00m");
        buttonApp.clickTheButton();
        buttonApp.oneHourPasses();
        buttonApp.requireTimeSinceStarted("02h:00m");
    }

    @Test
    public void windowTitleIsInitiallyIdle() throws Exception {
        String title = "The Button - " + buttonApp.idleCaption();
        buttonApp.assertWindowTitle(title);
    }

    @Test
    public void windowTitleUpdatesWithRunningTrack() throws Exception {
        buttonApp.clickTheButton();
        buttonApp.assertWindowTitle("The Button - " + "00h:00m");
        buttonApp.oneHourPasses();
        buttonApp.assertWindowTitle("The Button - " + "01h:00m");
    }

    @AfterMethod
    public void tearDown() {
        buttonApp.tearDown();
    }
}
