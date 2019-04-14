package core.commands.modes;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import core.commands.Mode;
import core.common.VoidAction;
import core.modules.UsersDB;
import core.modules.modes.queue.QueueMode;
import core.modules.queuev2.QueueDB;
import core.modules.queuev2.QueueFormatter;
import core.modules.res.MenheraSprite;
import core.modules.session.UserIOStream;
import core.modules.vkSDK.request.KeyboardPostRequest;
import core.modules.vkSDK.request.keyboard.Keyboard;
import core.modules.vkSDK.request.keyboard.VKButton;
import vk.VKManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Queue implements Mode {
    private static QueueDB db =  new QueueDB();
    private UserIOStream input;
    private UserIOStream output;
    private int userId;
    private boolean keyboardInstalled = false;

    @Override
    public void setUserID(int id) {
        userId = id;
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getResponse(String input) {
        return null;
    }

    @Override
    public String getResponse(Message message) {
        if (!keyboardInstalled){
            sendKeyboard(message.getUserId());
            keyboardInstalled = true;
        }
        try {
            String body = message.getBody().toLowerCase();

            if (body.equals("list")) {
                return QueueFormatter.getString(db.getNames());
            }

            if (body.matches("create .+")){
                createCommand(message);
                return null;
            }

            if (body.matches("drop .+")){
                dropCommand(message);
                return null;
            }

            input.writeln(message.getBody());
            while (!output.available()){
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return output.readString();
        } catch (SQLException e){
            return e.getMessage();
        }
    }

    @Override
    public void setOutput(UserIOStream output) {
        this.output = output;
    }

    @Override
    public void setInput(UserIOStream input) {
        this.input = input;
    }

    @Override
    public UserIOStream getInputStream() {
        return this.input;
    }

    @Override
    public UserIOStream getOutputStream() {
        return this.output;
    }


    @Override
    public void run() {
        QueueMode queueMode = new QueueMode(output, userId);
        queueMode.init(input);
    }

    private void dropCommand(Message message){
        String command = message.getBody();
        String context = command.split(" ", 2)[1];

        System.out.println(command);
        System.out.println(context);

        try {
            QueueDB db = new QueueDB();
            VKManager vk = new VKManager();

            // delete with regex

            if (context.matches("all .+")) {
                String regex = context.split(" ", 2)[1];
                List<String> queues = db.getNames();
                for (String name: queues
                     ) {

                    if (name.matches(regex)){
                        checkIsSuperUser(db.get(name), message.getUserId(), () -> {
                            db.delete(name);
                            vk.sendMessage("Удалена очередь " + name, message.getUserId());
                        });
                    }

                }
            } else {
                // delete by name
                for (String name : context.split(" ")) {
                    core.modules.queuev2.Queue q = new QueueDB().get(name);
                    if (q != null) {
                        checkIsSuperUser(q, message.getUserId(), () -> {
                            db.delete(q.getName());
                            vk.sendMessage("Вы удалили очередь " + q.getName(), message.getUserId());
                        });
                    } else {
                        vk.sendMessage("Очередь " + name + " не найдена", message.getUserId());
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ApiException | ClientException ignored) {
        }
    }
    private void createCommand(Message message){
        String command = message.getBody();

        String context = command.split(" ", 2)[1];
        if (context.matches(".+ for .+")){
            String regex = context.split(" ")[2];
            core.modules.queuev2.Queue q = new core.modules.queuev2.Queue(userId);
            q.setName(context.split(" ")[0]);
            try {
                Map<Integer, String> map =  new UsersDB().getVKIDListWithGroup();
                VKManager vk = new VKManager();
                String name = VKManager.getUserInfo(message.getUserId()).getFirstName();
                for (int vkid: map.keySet()){
                    if (map.get(vkid).matches(regex)){
                        q.add(vkid);
                        vk.sendMessage(String.format("Вы добавлены в очередь %s пользователем %s",q.getName(), name), vkid);
                        try {
                            vk.getSendQuery().attachment(MenheraSprite.HI_SPRITE).userId(vkid).execute();
                        } catch (ClientException | ApiException ignored){}
                        }
                }
                new QueueDB().save(q);

                try {
                    vk.getSendQuery().userId(message.getUserId()).message("Очередь создана!\n" + QueueFormatter.getString(q)
                    + "\n").attachment(MenheraSprite.READY_SPRITE).execute();
                } catch (ApiException | ClientException ignored) {}

            } catch (SQLException e) {
                e.printStackTrace();
                new VKManager().sendMessage(e.getMessage(), message.getUserId());
            }
        } else {
            core.modules.queuev2.Queue queue = new core.modules.queuev2.Queue(message.getUserId());
            queue.setName(context.split(" ")[1]);
            try {
                new QueueDB().save(queue);
            } catch (SQLException e) {
                e.printStackTrace();
                new VKManager().sendMessage(e.getMessage(), message.getUserId());
            }

        }
    }

    private void checkIsSuperUser(core.modules.queuev2.Queue q, int id, VoidAction action) throws ClientException, ApiException {
        if (q.isSuperUser(id)) {
            try {
                action.action();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            VKManager vk = new VKManager();
            vk.sendMessage("Вы не имеете прав на удаление очереди", id);
                vk.getSendQuery().attachment(MenheraSprite.MM_SPRITE).execute();

        }
    }

    private void sendKeyboard(int id){

        ArrayList<VKButton> buttons1 = new ArrayList<>();
        ArrayList<VKButton> buttons2 = new ArrayList<>();
        ArrayList<VKButton> buttons3 = new ArrayList<>();
        ArrayList<VKButton> buttons4 = new ArrayList<>();
        Keyboard kb = new Keyboard();
        buttons1.add(new VKButton().setLabel("Список%20команд"));
        kb.addButtons(buttons1, 0);
        buttons2.add(new VKButton().setLabel("list"));
        buttons2.add(new VKButton().setLabel("create"));
        buttons2.add(new VKButton().setLabel("drop"));
        kb.addButtons(buttons2, 1);

        buttons3.add(new VKButton().setLabel("br"));
        buttons3.add(new VKButton().setLabel("swap"));
        kb.addButtons(buttons3, 2);

        buttons4.add(new VKButton().setLabel("show"));
        buttons4.add(new VKButton().setLabel("exit"));
        kb.addButtons(buttons4, 3);
        try {
            new KeyboardPostRequest().sendKeyboard(kb, id);
        } catch (IOException | ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
