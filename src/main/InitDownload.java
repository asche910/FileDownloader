package main;

import util.DownloadListener;
import util.HttpUtils;
import util.ProgressBar;

import java.io.File;

import static main.Main.*;
import static util.PrintUtils.println;

public class InitDownload {

    private String sourceUrl;
    private String outputDir; // 默认运行目录
    private String fileName;

    public InitDownload(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void start(){
        parseUrl();
        createCache();

        long start = System.currentTimeMillis();
        try {
            FILE_SIZE = HttpUtils.getResponseSize(sourceUrl);
            long single = FILE_SIZE / THREAD_SIZE;

            printSize(FILE_SIZE);

            new Thread(() -> {
                ProgressBar progressBar = new ProgressBar();
                try {
                    progressBar.setDownloadListener(() -> {
                        long end = System.currentTimeMillis();
                        println(String.format("\nTotal time: %dS", (end - start) / 1000));

                        deleteCache();
                    });
                    progressBar.initProgress();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            for(int i = 0; i < THREAD_SIZE; i++){
                long startIndex = i * single;
                long endIndex = (i + 1) * single - 1;
                if (i == THREAD_SIZE - 1) {
                    endIndex = FILE_SIZE - 1;
                }
                // println(startIndex + "---" + endIndex);

                DownloadTask task = new DownloadTask(sourceUrl, fileName, i, startIndex, endIndex);
                Thread thread = new Thread(task, "Task-" + i);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从sourceUrl中解析出fileName
     */
    private void parseUrl(){
        String name = sourceUrl.substring(sourceUrl.lastIndexOf('/') + 1);
        int index;
        if ((index = name.indexOf('?')) != -1)
            name = name.substring(0, index);
        println("FileName: " + name);

        if (name.equals("")){
            fileName = "file";
        }else{
            fileName = name;
        }
    }

    private void printSize(long size){
        if (size < 1024){
            println(String.format("File Size: %dB", size));
        }else if(size < 1_048_576){
            println(String.format("File Size: %.2fK", size / 1024.0));
        }else if (size < 1_073_741_824){
            println(String.format("File Size: %.2fM", size / 1024.0 / 1024.0));
        }else {
            println(String.format("File Size: %.2fG", size / 1024.0 / 1024.0 / 1024.0));
        }
    }

    private void createCache(){
        File dir = new File(LOGO);
        if (!dir.exists()){
            dir.mkdir();
        }
    }

    private void deleteCache(){
        File dir = new File(LOGO);
        if (dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            for(File file: files){
                if (file.isFile()){
                    file.delete();
                }
            }
            dir.delete();
        }
    }

}
