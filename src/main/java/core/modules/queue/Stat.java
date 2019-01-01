package core.modules.queue;

import java.io.Serializable;

/**
 * @author Arthur Kupriyanov
 */
public class Stat implements Serializable {
    int lastPass;
    int beginTime;
    int peopleCount;
    int passCount;
}
