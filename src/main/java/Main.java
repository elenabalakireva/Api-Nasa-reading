import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static String urlNasa = "https://api.nasa.gov/planetary/apod?api_key=ifInFmhnBlB2cQFM2RU9Kdt5wKEnbA7rIdDgkn3e";

    public static void main(String[] args) throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(urlNasa);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

        NasaAnswer nasaAnswer = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );

        String[] imageNameParts = nasaAnswer.getUrl().split("/");
        String fileName = imageNameParts[imageNameParts.length - 1];
        File file = new File(fileName);
        URL url = new URL(nasaAnswer.getUrl());
        FileUtils.copyURLToFile(url, file);


    }
}
