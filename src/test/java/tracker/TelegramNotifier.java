package tracker;

import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TelegramNotifier {
	//private static final String BOT_TOKEN = "8369414618:AAFJVI-DCrp5KI8UywJ47u-_9RbMtqTbmx4";
   // private static final String CHAT_ID = "898999018";
    private static final   String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
  private static final String CHAT_ID = System.getenv("TELEGRAM_CHAT_ID");

    public static void sendTextMessage(String message) {
        try {
        	//System.out.println("BOT token starts with: " + BOT_TOKEN.substring(0, 5));
        	//System.out.println("Chat ID length: " + CHAT_ID.length());
            String urlString = String.format("https://api.telegram.org/bot%s/sendMessage", BOT_TOKEN);
            @SuppressWarnings("deprecation")
			URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String payload = "chat_id=" + CHAT_ID + "&text=" + URLEncoder.encode(message, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Telegram message sent successfully.");
            } else {
                System.out.println("❌ Telegram message failed with code: " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("🔴 Error from Telegram API: " + errorResponse);
                }
            }
        } catch (Exception e) {
            System.out.println("❗ Exception while sending Telegram message:");
            e.printStackTrace();
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
