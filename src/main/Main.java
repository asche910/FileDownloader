package main;

import org.apache.commons.cli.*;
import util.ProgressBar;

import java.io.*;
import java.util.Date;
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

    public static String SRC = "https://www.picpick.org/releases/latest/picpick_inst.exe";

    public static long sum = 0;
    public static long FILE_SIZE;
    public static int THREAD_SIZE = 8;
    public static final String LOGO = "FileDownloader";

    public static AtomicInteger atomicInteger = new AtomicInteger(THREAD_SIZE);

    public static void main(String[] args) throws ParseException {
        println(String.format("%s start...", LOGO));

//        InitDownload initDownload = new InitDownload(SRC);
//        initDownload.start();

        Options options = new Options();
//        options.addOption(new Option("h", "header", true, "add header"));
        options.addOption(new Option("c", false, "if using download from the break point"));
        options.addOption(new Option("H", "Header", true, "add header \"header=value\""));
        options.addOption(new Option("U", "User-Agent", true, "user-agent header"));
        options.addOption(new Option("h", "help", false, "output the help"));

        // print usage
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(LOGO + " [options ...] <url>", options );
        System.out.println();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(LOGO + " [options ...] <url>", options );
        }

        // check the options have been set correctly
        System.out.println(cmd.getOptionValue("H"));
        System.out.println(cmd.getOptionValue("U"));
        if (cmd.hasOption("c")) {
            System.out.println(new Date());
        }

        String[] str = cmd.getArgs();
        int length = str.length;
        System.out.println("length="+length);
        System.out.println("Str[0]="+str[0]);
    }
}
