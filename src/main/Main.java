package main;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static util.PrintUtils.println;

/**
 * @author Asche
 * @date 2019年7月12日
 * @github asche910
 */
public class Main {

    private static String sourceDir = "source.txt";
    private static String targetDir = "target.txt";

    public static String SRC = "https://d1.music.126.net/dmusic/2001/2019520193036/cloudmusicsetup2.5.3.197682.exe";

    private static int sum = 0;
    public static int THREAD_SIZE = 8;
    public static final String LOGO = "FileDownloader";

    public static AtomicInteger atomicInteger = new AtomicInteger(THREAD_SIZE);

    public static void main(String[] args) {
        println(String.format("%s start...\n", LOGO));

        InitDownload initDownload = new InitDownload(SRC);
        initDownload.start();
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
