package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public interface Dated {

    public void setEndTime(int time);
    public boolean isEnded();
}
