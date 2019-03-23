package core.commands;

import com.vk.api.sdk.objects.base.Link;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import com.vk.api.sdk.objects.photos.Photo;
import core.commands.VKCommands.VKCommand;
import core.modules.VKPhotoSaver;
import core.modules.tesseract.Tesseract;
import core.modules.tracer.StringBcompProgrammReader;

import java.io.File;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class Tess extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "tess";
    }

    @Override
    public String exec(Message message) {
        if (message.getUserId() != 255396611){
            return "Эта команда пока не доступна";
        }
        final String IMG_PATH = "src/main/resources/img";
        List<MessageAttachment> attachments = message.getAttachments();
        if (attachments==null || attachments.isEmpty()){
            return "Вы не вложили ни одного изображения";
        }
        for (MessageAttachment attach : attachments){
            MessageAttachmentType type = attach.getType();
            if (type == MessageAttachmentType.PHOTO){
                Photo photo = attach.getPhoto();
                //if (link.)
                //System.out.println(link.getUrl());
                VKPhotoSaver.savePhoto(photo, IMG_PATH, message.getUserId().toString());
                String filePath = IMG_PATH + "/" + message.getUserId() + ".jpg";
                File file = new File(filePath);
                String text = new Tesseract().getEngText(file);
                file.delete();

                return text;
            }
        }

        return null;


    }

    @Override
    public String getManual() {
        return "Программа для приведения изображений в текст.\n\n" +
                "Используйте ключевое слово tess вместе с вложенным изображением.\n\n" +
                "К примеру вы можете вложить в сообщение изображение с программой или таблицей и " +
                "получить его текстовую интерпретацию.\n\n" +
                "Работает пока только с английской сигнатурой\n\n" +
                "Находится в тестовом режиме, поэтому результат может отличаться от оригинала\n" +
                "Подробнее читайте:\n" +
                "https://github.com/AppLoidx/JavaBot/wiki/команда-Tess";
    }

    @Override
    public String getDescription() {
        return "Команда для приведения изображений в текст";
    }
}
