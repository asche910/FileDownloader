package main;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static util.PrintUtils.println;

public class Main {


    private static String sourceDir = "source.txt";
    private static String targetDir = "target.txt";

    public static String SRC = "https://github-production-release-asset-2e65be.s3.amazonaws.com/23216272/88a18380-89d0-11e9-8cd3-ee4334db7683?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20190714%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190714T021342Z&X-Amz-Expires=300&X-Amz-Signature=31967ef4dd935c172747f17b15d2d0a5323f52cc28fae42c474485038915271c&X-Amz-SignedHeaders=host&actor_id=13347412&response-content-disposition=attachment%3B%20filename%3DGit-2.22.0-64-bit.exe&response-content-type=application%2Foctet-stream";

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
