package core.modules;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.docs.responses.DocUploadResponse;
import com.vk.api.sdk.objects.docs.responses.GetUploadServerResponse;
import com.vk.api.sdk.queries.docs.DocsGetMessagesUploadServerQuery;
import com.vk.api.sdk.queries.docs.DocsGetMessagesUploadServerType;
import com.vk.api.sdk.queries.docs.DocsSaveQuery;
import vk.VKCore;

import java.io.File;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class VKDocUploader {
    private static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }
    }

    public static Doc upload(int peerId, File file, DocsGetMessagesUploadServerType type) throws ClientException, ApiException {
        if (vkCore==null){
            return null;
        }

        GetUploadServerResponse serverResponse = vkCore.getVk().docs().getMessagesUploadServer(vkCore.getActor()).peerId(peerId).type(type).execute();
        DocUploadResponse docUploadResponse = vkCore.getVk().upload().doc(serverResponse.getUploadUrl(),file).execute();
        List<Doc> docsList = vkCore.getVk().docs().save(vkCore.getActor(), docUploadResponse.getFile()).execute();

        return docsList.get(0);
    }

}
