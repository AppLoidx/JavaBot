package core.commands;

import core.common.KeysReader;
import core.modules.queue.SimpleQueue;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class QueueSimpleHandler implements QueueHandler<SimpleQueue>{
    public String handle(SimpleQueue queue, String... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        return null;
    }

}
