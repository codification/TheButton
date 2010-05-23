package thebutton.track;

/**
 * @author: aavisv
 * @created: 2010-05-21 3:07:58 PM
 */
public interface Ticker {
    /**
     * Starts or ends a time tracking
     */
    void tic();

    /**
     * Starts or ends a time tracking, attaching the task name as input
     *
     * @param taskname
     */
    void toc(String taskname);
}
