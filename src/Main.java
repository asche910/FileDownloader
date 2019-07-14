import util.DownloadTask;
import util.HttpUtils;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static util.PrintUtils.println;

public class Main {

    private static String sourceDir = "source.txt";
    private static String targetDir = "target.txt";

    public static String SRC = "https://github-production-release-asset-2e65be.s3.amazonaws.com/23216272/88a18380-89d0-11e9-8cd3-ee4334db7683?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20190713%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190713T021531Z&X-Amz-Expires=300&X-Amz-Signature=13200391349aa244064e78f9182e3398c09900f7dfcf7b09726fb4698fdd478c&X-Amz-SignedHeaders=host&actor_id=13347412&response-content-disposition=attachment%3B%20filename%3DGit-2.22.0-64-bit.exe&response-content-type=application%2Foctet-stream";

    private static int sum = 0;
    private static int SIZE = 5;
    public static AtomicInteger atomicInteger = new AtomicInteger(5);

    public static void main(String[] args) {
        println("Project start...");


        long start = System.currentTimeMillis();


        try {
//            download();
//            resume();

            int length = HttpUtils.getResponseSize(SRC);
            int single = length / SIZE;

            for(int i = 0; i < SIZE; i++){
                int startIndex = i * single;
                int endIndex = (i + 1) * single - 1;
                if (i == SIZE - 1) {
                    endIndex = length - 1;
                }
                println(startIndex + "---" + endIndex);

                DownloadTask task = new DownloadTask(SRC, i, startIndex, endIndex);
                DownloadTask.setAtomicInteger(atomicInteger);
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

    public static void download() throws IOException {
        FileInputStream fis = new FileInputStream(new File(sourceDir));
        FileOutputStream fos = new FileOutputStream(new File(targetDir));

        int n;
        int sum = 0;
        byte[] bytes = new byte[2];
        while((n = fis.read(bytes)) != -1){
            fos.write(bytes, 0, n);
            sum += n;

            if(sum >= 6){
                throw new IOException();
            }
        }
        fis.close();
        fos.close();
    }

    public static void resume() throws IOException {
        RandomAccessFile sourceFile = new RandomAccessFile(sourceDir, "r");
        RandomAccessFile targetFile = new RandomAccessFile(targetDir, "rw");

        sourceFile.seek(2);
        targetFile .seek(2);
        int n;
        byte[] bytes = new byte[2];
        while((n = sourceFile.read(bytes)) != -1){
            targetFile.write(bytes, 0, n);
        }
    }
}
