package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static Map<String, String> headerMap = new HashMap<>();

    static {
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
    }

    public static long getResponseSize(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

        for (String key: headerMap.keySet()){
            conn.setRequestProperty(key, headerMap.get(key));
        }

        if (conn.getResponseCode() == 200){
            long length = conn.getContentLength();
            // println("Length -------> " + length);

            return length;
        }
        return 0;
    }

    public static Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
