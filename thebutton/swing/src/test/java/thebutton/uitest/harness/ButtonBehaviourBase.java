package thebutton.uitest.harness;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * @author: aavisv
 * @created: 2010-05-20 12:40:33 PM
 */
public abstract class ButtonBehaviourBase {
    protected ButtonApplicationTester app;

    @BeforeMethod
    public void setUp() {
        app = new ButtonApplicationTester();
    }

    @AfterMethod
    public void tearDown() {
        app.tearDown();
    }
}
