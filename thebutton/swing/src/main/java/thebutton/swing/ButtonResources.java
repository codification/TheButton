/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import java.io.IOError;
import java.util.ResourceBundle;

public class ButtonResources {
    public static final String BUTTON_FRAME_TITLE = "button.frame.title";
    public static final String BUTTON_BUTTON_TITLE = "button.button.title";

    public static ResourceBundle lookupResources() {
        return ResourceBundle.getBundle("resources");
    }
}
