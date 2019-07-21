package util;

import main.Main;

import java.util.concurrent.TimeUnit;

import static main.Main.atomicInteger;

public class ProgressBar {

    private long index = 0;
    private String finish;
    private String unFinish;
    private String target = String.format("%3d%%[%s%s]", index, finish, unFinish);

    private DownloadListener downloadListener;


    // 进度条粒度
    private final int PROGRESS_SIZE = 50;
    private int BITE = 2;

    private String getNChar(long num, char ch) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public void initProgress() throws InterruptedException {
        System.out.print("Progress:");

        finish = getNChar(index / BITE, '█');
        unFinish = getNChar(PROGRESS_SIZE - index / BITE, '─');
        String target = String.format("%3d%%[%s%s]", index, finish, unFinish);
        System.out.print(target);

        while (true){
            long index = Main.sum * 100 / Main.FILE_SIZE;
            setProgress(index);
            if (atomicInteger.get() == 0){
                setProgress(100);
                downloadListener.onFinish();
                break;
            }
            TimeUnit.MILLISECONDS.sleep(300);
        }

/*        while (index <= 100){
            finish = getNChar(index / BITE, '█');
            unFinish = getNChar(PROGRESS_SIZE - index / BITE, '─');

            target = String.format("%3d%%├%s%s┤", index, finish, unFinish);
            System.out.print(getNChar(PROGRESS_SIZE + 6, '\b'));
            System.out.print(target);

            Thread.sleep(50);
            index++;
        }*/
    }

    private void setProgress(long index) {
        if (index > 100)
            index = 100;
        this.index = index;
        draw();
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    private void draw() {
        finish = getNChar(index / BITE, '█');
        unFinish = getNChar(PROGRESS_SIZE - index / BITE, '─');

        target = String.format("%3d%%├%s%s┤", index, finish, unFinish);
        System.out.print(getNChar(PROGRESS_SIZE + 6, '\b'));
        System.out.print(target);
    }
}
