package thebutton.track;

/**
 * Accepts notifications for when to start or stop time tracking
 *
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
     * @param taskname name of the task that was performed when the tic was 'ticked'
     */
    void tic(String taskname);
}
