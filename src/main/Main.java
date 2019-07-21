package main;

import org.apache.commons.cli.*;
import util.HttpUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static util.PrintUtils.println;

/**
 * @author Asche
 * @date 2019年7月12日
 * @github asche910
 */
public class Main {

    public static String SRC = "https://www.picpick.org/releases/latest/picpick_inst.exe";

    public static long sum = 0;
    public static long FILE_SIZE;
    public static int THREAD_SIZE = 8;
    public static final String NAME = "FileDownloader";
    public static final String LOGO = "   _____ __    ___                  __             __       \n" +
            "  / __(_) /__ / _ \\___ _    _____  / /__  ___ ____/ /__ ____\n" +
            " / _// / / -_) // / _ \\ |/|/ / _ \\/ / _ \\/ _ `/ _  / -_) __/\n" +
            "/_/ /_/_/\\__/____/\\___/__,__/_//_/_/\\___/\\_,_/\\_,_/\\__/_/   \n" +
            "                                                            ";

    public static AtomicInteger atomicInteger = new AtomicInteger(THREAD_SIZE);

    public static void main(String[] args) throws ParseException {
        println(LOGO);

        Options options = new Options();
        options.addOption(new Option("c", false, "加上表明关闭断点续传，默认开启"));
        options.addOption(new Option("H", "Header", true, "添加请求头部，格式：\"header=value\"，多个可叠加使用该H参数"));
        options.addOption(new Option("U", "User-Agent", true, "添加User-Agent标识头"));
        options.addOption(new Option("n", "num", true, "开启的线程数量，默认为8"));
        options.addOption(new Option("h", "help", false, "使用说明"));

        HelpFormatter formatter = new HelpFormatter();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(NAME + " [options ...] <url>", options );
            return;
        }

        if (cmd.hasOption('h')){
            formatter.printHelp(NAME + " [options ...] <url>", options );
            return;
        }
        String[] cmdArgs = cmd.getArgs();
        // println("CmdArgs length: " + cmdArgs.length);

        if (cmdArgs.length != 1){
            formatter.printHelp(NAME + " [options ...] <url>", options );
            return;
        }else {
            SRC = cmdArgs[0];
        }

        InitDownload initDownload = new InitDownload(SRC);

        if (cmd.hasOption("c")) {
            initDownload.setDisableCache(true);
        }
        if (cmd.hasOption('n')){
            String strN = cmd.getOptionValue('n');
            try {
                THREAD_SIZE = Integer.parseInt(strN);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
        }
        if (cmd.hasOption('H')){
            parseHeader(cmd.getOptionValues('H'));
        }
        if (cmd.hasOption('U')){
            HttpUtils.getHeaderMap().put("User-Agent", cmd.getOptionValue('U'));
        }

        initDownload.start();
    }

    private static void parseHeader(String[] headers){
        for (String header: headers){
            String[] strings = header.split("=");
            if (strings.length == 2){
                Map<String, String> headerMap = HttpUtils.getHeaderMap();
                headerMap.put(strings[0].trim(), strings[1].trim());
            }
        }
    }
}
