package main;

import util.HttpUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static main.Main.*;
import static util.PrintUtils.println;

public class DownloadTask implements Runnable{

    private String sourceUrl;
    private String outputDir; // 默认运行目录
    private String fileName;

    private int threadId;
    private long startIndex;
    private long endIndex;

    public DownloadTask(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public DownloadTask(String sourceUrl, int threadId, int startIndex, int endIndex) {
        this.sourceUrl = sourceUrl;
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public DownloadTask(String sourceUrl, String fileName, int threadId, long startIndex, long endIndex) {
        this.sourceUrl = sourceUrl;
        this.fileName = fileName;
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public void run() {
        try {
            File threadFile = new File(String.format("%s%ctemp_%d", NAME, File.separatorChar, threadId));
            RandomAccessFile threadRandomFile = new RandomAccessFile(threadFile, "rwd");

            String line = threadRandomFile.readLine();
            if (line != null && !line.equals("")){
                int index = Integer.parseInt(line);
                sum += index - startIndex;
                startIndex = index;
                if (startIndex > endIndex){
                    atomicInteger.decrementAndGet();
                    threadRandomFile.close();
                    return;
                }
            }

            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            Map<String, String> headerMap = HttpUtils.getHeaderMap();

            for(String key: headerMap.keySet()){
                connection.setRequestProperty(key, headerMap.get(key));
            }
            connection.setRequestProperty("Range", String.format("bytes=%d-%d", startIndex, endIndex));

            // println("Code ------> " + connection.getResponseCode());

            if (connection.getResponseCode() == 206){
                File file = new File(fileName);

                InputStream inputStream = connection.getInputStream();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.seek(startIndex);

                byte [] bytes = new byte[1024];
                int len, total = 0;
                while ((len = inputStream.read(bytes)) != -1){
                    randomAccessFile.write(bytes, 0, len);
                    total += len;
                    sum += len;

                    threadRandomFile.seek(0);
                    threadRandomFile.write((startIndex + total + "").getBytes());
                }
                inputStream.close();
                randomAccessFile.close();
                threadRandomFile.close();
            }else {
                println(String.format("Thread %d 连接失败！", threadId));
            }

        } catch (Exception e) {
             // e.printStackTrace();
            println(String.format("Thread %d 连接失败！", threadId));
        }
        atomicInteger.decrementAndGet();
    }
}
