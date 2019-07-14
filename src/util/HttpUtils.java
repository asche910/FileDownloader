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
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        if (conn.getResponseCode() == 200){
            int length = conn.getContentLength();
            println("Length -------> " + length);

            return length;
        }
        return 0;
    }
}
