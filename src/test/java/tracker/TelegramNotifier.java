package tracker;

import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import java.io.File;

public class TelegramNotifier {
	private static final String BOT_TOKEN = "8369414618:AAFJVI-DCrp5KI8UywJ47u-_9RbMtqTbmx4";
    private static final String CHAT_ID = "898999018";

    public static void sendTextMessage(String message) throws Exception {
        String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

        HttpPost post = new HttpPost(url);
        String json = "{\"chat_id\":\"" + CHAT_ID + "\",\"text\":\"" + message + "\"}";

        post.setHeader("Content-type", "application/json");
        post.setEntity(new StringEntity(json));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(post);
        }
    }

    public static void sendPhoto(File photoFile) throws Exception {
        String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendPhoto";

        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("chat_id", CHAT_ID);
        builder.addBinaryBody("photo", photoFile);

        post.setEntity(builder.build());

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(post);
        }
    }

}
