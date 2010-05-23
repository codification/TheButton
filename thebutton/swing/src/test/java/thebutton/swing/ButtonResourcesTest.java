package thebutton.swing;

import org.fest.swing.util.Pair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.fest.assertions.Assertions.assertThat;
import static thebutton.swing.BundledResources.BUTTON_FRAME_TITLE;

/**
 * @author: aavisv
 * @created: 2010-05-19 1:17:27 PM
 */
public class ButtonResourcesTest {
    private ButtonResources englishResources;
    private ButtonResources germanResources;

    @Test
    public void
    formsIdleTitleCorrectly() {
        assertThat(englishResources.idleTitle()).isEqualTo("The Button - Idle");
    }

    @Test
    public void
    translatesTitleCorrectly() {
        assertThat(germanResources.idleTitle()).isEqualTo("Der Druckknopf - Frei");
    }

    @Test
    public void
    formsRunningTitleCorrectly() {
        assertThat(englishResources.runningTitle("00h:00m")).isEqualTo("The Button - 00h:00m");
    }

    @Test
    public void
    translatesRunningTitleCorrectly() {
        assertThat(germanResources.runningTitle("00h:00m")).isEqualTo("Der Druckknopf - 00h:00m");
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        englishResources = new BundledResources(new TestingResourceBundle(
                resource(BUTTON_FRAME_TITLE, "The Button"),
                resource("button.button.title.idle", "Idle")
        ));
        germanResources = new BundledResources(new TestingResourceBundle(
                resource(BUTTON_FRAME_TITLE, "Der Druckknopf"),
                resource("button.button.title.idle", "Frei")
        ));
    }

    private static Pair<String, String> resource(String key, String value) {
        return new Pair<String, String>(key, value);
    }

    private static class TestingResourceBundle extends ResourceBundle {
        private Map<String, String> resources = new HashMap<String, String>();

        public TestingResourceBundle(Pair<String, String>... pairs) {
            for (Pair<String, String> pair : pairs) {
                resources.put(pair.i, pair.ii);
            }
        }

        @Override
        protected Object handleGetObject(String s) {
            return resources.get(s);
        }

        @Override
        public Enumeration<String> getKeys() {
            throw new TestingFailure();
        }

        private static class TestingFailure extends UnsupportedOperationException {
        }

    }
}
