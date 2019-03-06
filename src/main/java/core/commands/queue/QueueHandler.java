package core.commands.queue;

import core.modules.queue.Queue;

/**
 * @author Arthur Kupriyanov
 */
public interface QueueHandler<T extends Queue> {
    String handle(T queue, String... args);
}
