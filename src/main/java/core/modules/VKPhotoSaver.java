package core.modules;

import com.vk.api.sdk.objects.photos.Photo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Arthur Kupriyanov
 */
public class VKPhotoSaver {
    public static void savePhoto(Photo photo, String path, String name){
        URL url;
        URLConnection conn;
        try
        {
                url = new URL(photo.getPhoto604());
                conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                File folder = new File(path);
                if (!folder.exists()){
                    folder.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(new File(path + "/"+name+".jpg"));

                int ch;
                while ((ch = bis.read())!=-1)
                {
                    fos.write(ch);
                }
                bis.close();
                fos.flush();
                fos.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
