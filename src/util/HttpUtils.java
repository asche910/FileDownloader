package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static util.PrintUtils.println;

public class HttpUtils {
    public static int getResponseSize(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

        if (conn.getResponseCode() == 200){
            int length = conn.getContentLength();
            println("Length -------> " + length);

            return length;
        }
        return 0;
    }
}
