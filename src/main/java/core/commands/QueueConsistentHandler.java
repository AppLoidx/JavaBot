package core.commands;

import core.modules.queue.ConsistentQueue;

/**
 * @author Arthur Kupriyanov
 */
public class QueueConsistentHandler implements QueueHandler<ConsistentQueue> {
    @Override
    public String handle(ConsistentQueue queue, String... args) {
        return null;
    }
}
