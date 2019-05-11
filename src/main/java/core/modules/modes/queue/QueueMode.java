package core.modules.modes.queue;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.common.YesNoAction;
import core.modules.AskYesNo;
import core.modules.queuev2.Person;
import core.modules.queuev2.Queue;
import core.modules.queuev2.QueueDB;
import core.modules.queuev2.QueueFormatter;
import core.modules.res.MenheraSprite;
import core.modules.session.UserIOStream;
import core.modules.vkSDK.AskYesNoVK;
import vk.VKManager;

import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class QueueMode {
    private String queueName = "none";
    private QueueDB db =  new QueueDB();
    private UserIOStream outputStream;
    private int userId;
    private boolean exitStatus = false;

    public QueueMode(UserIOStream outputStream, int userId){
        this.outputStream = outputStream;
        this.userId = userId;
    }

    public void init(UserIOStream inputStream){
        while(!exitStatus) {
            String request;
            while (true) {
                if (inputStream.available()) {
                    request = inputStream.readString();
                    break;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String command = getCommand(request);

            switch (command) {
                case "br":
                    branchCommand(request);
                    break;
                case "show":
                    showCommand();
                    break;
                case "swap":
                    swapCommand(request);
                    break;
                case "shuffle":
                    shuffleCommand();
                    break;
                case "exit":
                case "e":
                    exitStatus = true;
                    break;
                    default:
                        outputStream.writeln("Команда " + command + " не распознана");
            }

        }
    }


    private String getCommand(String req){
        return req.split(" ")[0];
    }

    private void shuffleCommand(){
        if (checkNone()) return;

        Queue q = getQueue();
        if (q == null) return;
        q.shuffle();
        try {
            db.save(q);
            outputStream.writeln("Очередь перемешана:\n" + QueueFormatter.getString(q));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private synchronized void swapCommand(String request){
        VKManager vk = new VKManager();
        if (checkNone()) return;
        Integer place;
        if ((place=getId(request))!=null){
            Queue q = getQueue();
            if (q == null) return;

            Person thisP = q.getPerson(new Person(userId));
            Person anotherP = q.getPerson(place);
            if (anotherP.equals(thisP)) return;
            anotherP.addSwapRequest(thisP);
            UserXtrCounters info = VKManager.getUserInfo(thisP.getId());
            String name = info==null?"неизвестная печенька":info.getFirstName();
            AskYesNoVK.answer(anotherP.getId(), new YesNoAction() {
                @Override
                public void yesAction() {
                    thisP.addSwapRequest(anotherP);
                    q.safeSwap(thisP, anotherP);

                    try {
                        new VKManager().getSendQuery().attachment(MenheraSprite.OK_FLAG).peerId(anotherP.getId()).execute();
                    } catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }

                    outputStream.writeln("Вы поменялись местами. Теперь ваше место " + place);
                    new VKManager().sendMessage("Вы поменялись местами. Теперь ваше место " + q.getPlace(anotherP), anotherP.getId());
                    try {
                        db.save(q);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void noAction() {
                    try {
                        new VKManager().getSendQuery().attachment(MenheraSprite.NO_FLAG).peerId(anotherP.getId()).execute();
                    } catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (thisP.canSwapWith(anotherP)){
                q.safeSwap(thisP, anotherP);
                outputStream.writeln("Вы поменялись местами. Теперь ваше место " + place);
                new VKManager().sendMessage("Вы поменялись местами. Теперь ваше место " + q.getPlace(anotherP), anotherP.getId());
            }
            else{
                vk.sendMessage("Пользователь " +
                        name +
                        " хочет поменяться с вами в очереди.", anotherP.getId());
                outputStream.writeln("Ваша заявка отправлена");
            }
            try {
                db.save(q);
            } catch (SQLException e) {
                outputStream.writeln(e.getMessage());
            }
        } else {
            outputStream.writeln("Введите команду:\nswap место_в_очереди\n" +
                    "после того как другой пользователь сделает тоже самое, вы " +
                    "поменяетесь местами");
        }

    }

    private Integer getId(String swapCommandContext){
        if (swapCommandContext.matches("swap [0-9]+")){
            return Integer.parseInt(swapCommandContext.split(" ")[1]);
        } return null;
    }
    private void showCommand(){
        if (queueName.equals("none")){
            outputStream.writeln("Перейдите к какой нибудь очереди, чтобы получить список");
            return;
        }

        try {

            Queue q = db.get(queueName);
            outputStream.writeln(QueueFormatter.getString(q));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private boolean checkNone(){
        try {
            if (!new QueueDB().getNames().contains(queueName)) queueName = "none";
            if (queueName.equals("none")){
                outputStream.writeln("Перейдите к какой нибудь очереди, чтобы получить список");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void branchCommand(String request){
        String branchName;
        if ((branchName = getBranchName(request))!=null){
            branch(branchName);
        } else {
            outputStream.writeln("Введите br имя_очереди, чтобы перейти к управлению " +
                    "или просмотру очереди");
        }
    }
    private String getBranchName(String req){
        if (req.matches("br .+")){
            return req.split(" ")[1];
        }

        return null;
    }

    private void branch(String queueName){
        try {
            Queue q = db.get(queueName);
            System.out.println(queueName);
            if (q != null){
                this.queueName = q.getName();
                outputStream.writeln("Вы перешли на очередь: " + queueName);
            } else {
                outputStream.writeln("Такой очереди не существует");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputStream.writeln(e.getMessage());
        }
    }

    private Queue getQueue(){
        try {
            return db.get(queueName);
        } catch (SQLException e) {
            outputStream.writeln(e.getMessage());
            return null;
        }
    }

}
