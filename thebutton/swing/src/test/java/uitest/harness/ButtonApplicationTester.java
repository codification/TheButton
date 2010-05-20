package uitest.harness;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.joda.time.Duration;
import thebutton.swing.ButtonFrame;
import thebutton.swing.ButtonResources;
import thebutton.swing.Main;
import thebutton.track.TestingClock;
import thebutton.track.TickerTracker;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.fest.assertions.Assertions.assertThat;

public class ButtonApplicationTester {
    FrameFixture window;
    ResourceBundle resourceBundle;
    TestingClock clock;
    TickerTracker timeTracker;
    ButtonFrame frame;

    public ButtonApplicationTester() {
        clock = new TestingClock();
        timeTracker = new TickerTracker(clock);
        resourceBundle = ButtonResources.lookupResources();
        frame = GuiActionRunner.execute(new GuiQuery<ButtonFrame>() {
            @Override
            protected ButtonFrame executeInEDT() {
                return Main.createFrame(timeTracker, new Timer(1000, null));
            }
        });
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test

    }

    public void assertWindowTitle(String title) {
        assertThat(windowTitle()).isEqualTo(title);
    }

    String windowTitle() {
        return window.component().getTitle();
    }

    JTableFixture theTracks() {
        return window.table("the.track");
    }

    public void requireCaptionIsNotIdle() {
        assertThat(theButton().text()).isNotEqualTo(idleCaption());
    }

    public void requireCaptionIsIdle() {
        theButton().requireText(idleCaption());
    }

    public void fiveSecondsPass() {
        clock.advance(Duration.standardSeconds(5));
        frameTimeIsUpdated();
    }

    public String idleCaption() {
        return resourceBundle.getString(ButtonResources.BUTTON_BUTTON_IDLE);
    }

    public void clickTheButton() {
        theButton().click();
    }

    void frameTimeIsUpdated() {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                frame.updateTime(); // Simulate a timer
            }
        });
    }

    public void hasRows(int noRows) {
        theTracks().requireRowCount(noRows);
    }

    public void oneHourPasses() {
        clock.advance(Duration.standardHours(1));
        frameTimeIsUpdated();
    }

    public void requireTimeSinceStarted(String time) {
        sinceStartedField().requireText(time);
    }

    JTextComponentFixture sinceStartedField() {
        return window.textBox("sum.fromStart");
    }

    public JButtonFixture theButton() {
        return window.button("the.button");
    }

    public void tearDown() {
        window.cleanUp();
    }

    public void requireTracks(int expected) {
        theTracks().requireRowCount(expected);
    }

    public void requireTrackColumns(int expected) {
        theTracks().requireColumnCount(expected);
    }
}