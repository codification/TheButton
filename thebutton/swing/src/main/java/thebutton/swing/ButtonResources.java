package thebutton.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: aavisv
 * @created: 2010-05-21 6:02:58 PM
 */
public interface ButtonResources {
    String sinceStartedLabel();

    String buttonTooltip();

    String columnName(TrackTableColumn column);

    BufferedImage appIconImage() throws IOException;

    String idleTitle();

    String idle();

    String runningTitle(String time);

    String taskLabel();
}
