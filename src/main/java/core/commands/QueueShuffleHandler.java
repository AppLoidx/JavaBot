package core.commands;

import core.common.KeysReader;
import core.modules.queue.ShuffleQueue;
import core.modules.queue.SimpleQueue;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class QueueShuffleHandler extends QueueSimpleHandler {

    @Override
    public <T extends SimpleQueue> String handle(T queue, String... args) {
        ShuffleQueue sq = (ShuffleQueue) queue;
        Map<String, String> keyMap = KeysReader.readKeys(args);

        if (keyMap.containsKey("-l") || keyMap.containsKey("--shuffle")){
            sq.shuffle();
        }
        return super.handle(sq, args);
    }
}
