package core.commands.special.vk;

import core.commands.Command;
import vk.VKCore;
import vk.VKServer;

/**
 * @author Arthur Kupriyanov
 */
public abstract class VkCommand  extends Command {
    protected VKCore vkCore;
    public VkCommand(VKCore vkCore){
        this.vkCore = VKServer.vkCore;
    }
}
