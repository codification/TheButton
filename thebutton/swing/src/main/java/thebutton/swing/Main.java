/**
 * Purpose
 * @author aavisv
 * @created 4 Feb, 2010
 * $Id$
 */
package thebutton.swing;

import thebutton.track.Clock;
import thebutton.track.TimeTracker;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
        TimeTracker timeTracker = new TimeTracker(Clock.SYSTEMCLOCK);
        final ButtonFrame buttonFrame = new ButtonFrame(ButtonResources.lookupResources(), timeTracker);
        buttonFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buttonFrame.pack();
        setLookAndFeel();
        buttonFrame.setVisible(true);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
