import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStreamImpl;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
            HttpGet infoGet = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=TMFgGOItXmWfGaLLSOxBlHs5XHFgi9JYkYAKe91U");
            CloseableHttpResponse response = client.execute(infoGet);
            Info info = objectMapper.readValue(response.getEntity().getContent(), Info.class);
            String imageUrl = info.getUrl();
            String[] arrayUrlImage = imageUrl.split("/");
            File file = new File(arrayUrlImage[arrayUrlImage.length - 1]);
            HttpGet imageGet = new HttpGet(imageUrl);
            CloseableHttpResponse image = client.execute(imageGet);
            ImageIO.write(ImageIO.read(image.getEntity().getContent()), "jpg", file);
            } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
