package thebutton.uitest;

import org.testng.annotations.Test;
import thebutton.uitest.harness.ButtonBehaviourBase;

/**
 * @author: aavisv
 * @created: 2010-05-20 12:41:44 PM
 */
public class TrackTaskBehaviour extends ButtonBehaviourBase {
    @Test
    public void
    hasAColumnForTaskName() {
        app.requireTrackColumns("Started", "Stopped", "Task");
    }

}
