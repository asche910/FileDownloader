package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static main.Main.LOGO;
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
            File threadFile = new File(String.format("%s%ctemp_%d", LOGO, File.separatorChar, threadId));
            RandomAccessFile threadRandomFile = new RandomAccessFile(threadFile, "rwd");

            String line = threadRandomFile.readLine();
            if (line != null && !line.equals("")){
                int index = Integer.parseInt(line);
                startIndex = index;
            }

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
            int len, total = 0;
            while ((len = inputStream.read(bytes)) != -1){
                randomAccessFile.write(bytes, 0, len);
                total += len;

                threadRandomFile.seek(0);
                threadRandomFile.write((startIndex + total + "").getBytes());
            }
            inputStream.close();
            randomAccessFile.close();
            threadRandomFile.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        atomicInteger.decrementAndGet();
    }

}
