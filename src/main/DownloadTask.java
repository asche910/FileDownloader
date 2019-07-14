package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static main.Main.atomicInteger;
import static util.PrintUtils.println;

public class DownloadTask implements Runnable{

    private String sourceUrl;
    private String outputDir; // 默认运行目录
    private String fileName;

    private int threadId;
    private int startIndex;
    private int endIndex;

    public DownloadTask(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public DownloadTask(String sourceUrl, int threadId, int startIndex, int endIndex) {
        this.sourceUrl = sourceUrl;
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public DownloadTask(String sourceUrl, String fileName, int threadId, int startIndex, int endIndex) {
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
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            connection.setRequestProperty("Range", String.format("bytes=%d-%d", startIndex, endIndex));
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");


            println("Code ------> " + connection.getResponseCode());

            File file = new File(fileName);

            InputStream inputStream = connection.getInputStream();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(startIndex);

            byte [] bytes = new byte[1024];
            int n;
            while ((n = inputStream.read(bytes)) != -1){
                randomAccessFile.write(bytes, 0, n);
            }
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        atomicInteger.decrementAndGet();
    }

}
