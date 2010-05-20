package thebutton.swing;

import org.testng.annotations.Test;

/**
 * @author: aavisv
 * @created: 2010-05-20 12:59:25 PM
 */
public class TrackColumnTest {

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void
    exceptionForIndexMinusOne() {
        TrackTableColumn.columnByNumber(-1);
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void
    exceptionForIndexOffByOne() {
        TrackTableColumn.columnByNumber(TrackTableColumn.values().length);
    }

    @Test
    public void
    allColumns() throws Exception {
        for (int columnNo = TrackTableColumn.numberOfColumns(); columnNo < TrackTableColumn.numberOfColumns(); columnNo++) {
            TrackTableColumn.columnByNumber(columnNo);
        }
    }
}
