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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class ButtonResources {
    public static final String BUTTON_FRAME_TITLE = "button.frame.title";
    public static final String BUTTON_BUTTON_IDLE = "button.button.title.idle";
    private ResourceBundle resourceBundle;

    public ButtonResources(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public static ResourceBundle lookupResources() {
        return ResourceBundle.getBundle("resources");
    }

    public String sinceStartedLabel() {
        return resourceBundle.getString("button.total.sinceStart.title");
    }

    public String buttonTooltip() {
        return resourceBundle.getString("button.tooltip");
    }

    public String columnName(TrackTableColumn column) {
        return resourceBundle.getString(format("button.track.column.%s", column.resourceKey()));
    }

    public BufferedImage appIconImage() throws IOException {
        return ImageIO.read(ButtonResources.class.getResourceAsStream("/img.png"));
    }

    public String idleTitle() {
        return format("%s - %s",
                resourceBundle.getString(BUTTON_FRAME_TITLE),
                idle());
    }

    public String idle() {
        return resourceBundle.getString("button.button.title.idle");
    }

    public String runningTitle(String time) {
        return format("%s - %s",
                resourceBundle.getString(BUTTON_FRAME_TITLE), time);
    }

    static ButtonResources createResources() {
        return new ButtonResources(lookupResources());
    }

    public String taskLabel() {
        return resourceBundle.getString("button.task.label");
    }
}
