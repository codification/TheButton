/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import java.util.ResourceBundle;

public class ButtonResources {
    public static final String BUTTON_FRAME_TITLE = "button.frame.title";
    public static final String BUTTON_BUTTON_IDLE = "button.button.title.idle";

    public static ResourceBundle lookupResources() {
        return ResourceBundle.getBundle("resources");
    }
}
