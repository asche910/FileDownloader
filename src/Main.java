import java.io.*;

import static util.PrintUtils.println;

public class Main {

    private static String sourceDir = "source.txt";
    private static String targetDir = "target.txt";

    private static int sum = 0;

    public static void main(String[] args) {
        println("Project start...");

        try {
//            download();
            resume();
        } catch (IOException e) {
            e.printStackTrace();
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
