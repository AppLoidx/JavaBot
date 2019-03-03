package core.modules.queue;

import core.modules.queue.exceptions.PersonNotFoundException;

/**
 * @author Arthur Kupriyanov
 */
public interface Fixed {
    boolean isFree(int id) throws PersonNotFoundException;
    int getLength();
    void setLength(int length);
}
