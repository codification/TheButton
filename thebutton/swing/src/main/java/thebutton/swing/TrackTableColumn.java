package thebutton.swing;

/**
 * @author: aavisv
 * @created: 2010-05-20 12:52:20 PM
 */
@SuppressWarnings({"UnusedDeclaration"})
public enum TrackTableColumn {
    START("start"),
    STOP("stop"),
    TASK("task");

    TrackTableColumn(String key) {
        this.key = key;
    }

    private String key;

    public String resourceKey() {
        return key;
    }

    public static int numberOfColumns() {
        return values().length;
    }

    public static TrackTableColumn columnByNumber(int column) {
        TrackTableColumn[] columns = values();
        if (-1 < column && column < columns.length) {
            return columns[column];
        } else {
            throw new EnumConstantNotPresentException(TrackTableColumn.class, "column number " + column);
        }
    }
}
