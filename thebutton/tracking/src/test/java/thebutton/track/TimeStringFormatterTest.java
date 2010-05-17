package thebutton.track;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.PatternMatcher;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.text.pattern.Patterns.*;
import static org.joda.time.Duration.standardMinutes;

/**
 * @author: aavisv
 * @created: 2010-05-17 8:19:05 PM
 */
public class TimeStringFormatterTest {
    private TimeStringFormatter timeStringFormatter;
    private PatternMatcher hmsPattern;
    private PatternMatcher timeOfDayPattern;

    @Test
    public void
    conformsToPattern() {
        assertThat("using minutes", timeStringFormatter.usingMinutes(standardMinutes(5)), hmsPattern);
        assertThat("using seconds", timeStringFormatter.usingSeconds(standardMinutes(5)), hmsPattern);
        assertThat("time of day", timeStringFormatter.timeOfDay(new DateTime()), timeOfDayPattern);
    }

    @Test
    public void
    noSecondsIncludedInMinuteFormat() {
        assertThat(timeStringFormatter.usingMinutes(standardMinutes(5)), endsWith("m"));
    }

    @Test
    public void
    secondsIncludedInSecondFormat() {
        assertThat(timeStringFormatter.usingSeconds(standardMinutes(5)), endsWith("s"));
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        timeStringFormatter = new TimeStringFormatter();
        PatternComponent twoDigits = exactly(2, anyCharacterInCategory("Digit"));
        PatternComponent unitOfTime = sequence(twoDigits, anyCharacterIn("hms"));
        hmsPattern = new PatternMatcher(listOf(unitOfTime).separatedBy(":"));
        timeOfDayPattern = new PatternMatcher(listOf(twoDigits).separatedBy(":"));
    }
}
