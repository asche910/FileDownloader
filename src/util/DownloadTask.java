package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static util.PrintUtils.println;

public class DownloadTask implements Runnable{

    private String sourceUrl;
    private String outputDir; // 默认运行目录
    private String fileName;

    private int threadId;
    private int startIndex;
    private int endIndex;

    private static AtomicInteger atomicInteger;

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


    public static AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public static void setAtomicInteger(AtomicInteger atomicInteger) {
        DownloadTask.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        parseUrl();

        try {
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            connection.setRequestProperty("Range", String.format("bytes=%d-%d", startIndex, endIndex));

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

    /**
     * 从sourceUrl中解析出fileName
     */
    private void parseUrl(){
        String[] strs = sourceUrl.split("/");
        String name = strs[strs.length - 1];
        int index;
        if ((index = name.indexOf('?')) != -1)
            name = name.substring(0, index);
        println("Get fileName: " + name);

        if (name == null || name.equals("")){
            fileName = "file";
        }else{
            fileName = name;
        }
    }
}
