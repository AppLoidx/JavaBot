package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public interface Dated {

    void setEndTime(int time);
    boolean isEnded();
}
