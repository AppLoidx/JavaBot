package core.modules.queue;

import java.io.Serializable;

/**
 * @author Arthur Kupriyanov
 */
public class Stat implements Serializable {
    public int lastPass;
    public int beginTime;
    public int peopleCount;
    public int passCount;
}
