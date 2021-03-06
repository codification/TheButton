package thebutton.uitest;

import org.testng.annotations.Test;
import thebutton.uitest.harness.ButtonBehaviourBase;

/**
 * @author: aavisv
 * @created: 2010-05-20 12:41:44 PM
 */
public class TrackTaskBehaviour extends ButtonBehaviourBase {
    private static final String TASK = "fun";

    @Test
    public void
    hasAColumnForTaskName() {
        app.requireTrackColumns("Started", "Stopped", "Task");
    }

    @Test
    public void
    taskNameInitiallyEmpty() {
        app.requireTaskFieldEmpty();
    }

    @Test
    public void
    canEnterTaskName() throws Exception {
        app.enterTaskName(TASK);
    }

    @Test
    public void
    taskNameAppearsInTrackRowInTheCorrectColumn() throws Exception {
        String task = TASK;
        app.enterTaskName(task);
        app.clickTheButton();
        app.fiveSecondsPass();
        app.clickTheButton();
        app.requireTrackTask(0, task);
    }

}
