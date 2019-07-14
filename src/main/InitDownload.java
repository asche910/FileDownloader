package main;

import util.HttpUtils;

import static main.Main.THREAD_SIZE;
import static main.Main.atomicInteger;
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

        long start = System.currentTimeMillis();
        try {
            int length = HttpUtils.getResponseSize(sourceUrl);
            int single = length / THREAD_SIZE;

            for(int i = 0; i < THREAD_SIZE; i++){
                int startIndex = i * single;
                int endIndex = (i + 1) * single - 1;
                if (i == THREAD_SIZE - 1) {
                    endIndex = length - 1;
                }
                println(startIndex + "---" + endIndex);

                DownloadTask task = new DownloadTask(sourceUrl, fileName, i, startIndex, endIndex);
                Thread thread = new Thread(task, "Task-" + i);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true){
            if (atomicInteger.get() == 0){
                long end = System.currentTimeMillis();
                println("Total time ------> " + (end - start));
                break;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从sourceUrl中解析出fileName
     */
    private void parseUrl(){
        String name = sourceUrl.substring(sourceUrl.lastIndexOf('/'));
        int index;
        if ((index = name.indexOf('?')) != -1)
            name = name.substring(0, index);
        println("Get fileName: " + name);

        if (name.equals("")){
            fileName = "file";
        }else{
            fileName = name;
        }
    }
}
