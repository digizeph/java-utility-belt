package com.mwzhang.java.util.io;

import com.google.common.base.Joiner;
import com.mwzhang.java.util.TimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mingwei Zhang on 2/17/14.
 * <p/>
 * Output wrappers and utility functions.
 *
 * @author Mingwei Zhang
 */
public class Output {

    /**
     * whether to output the time stamp before each output line.
     */
    public static boolean outputTime = true;
    private static BufferedWriter bufferedWriter = null;

    /**
     * Initiate file output writer
     *
     * @param event the event name for naming the log file.
     */
    public static void initialWriter(String event) {
        /*
        Get correct form of date time.
         */
        DateTime dt = TimeUtil.getLocalTime();
        DateTimeFormatter dtf_month = DateTimeFormat.forPattern("yyyy.MM.dd.HH.mm");
        String datetime = dtf_month.print(dt);

        String filename = event + "@" + datetime + ".txt";

        bufferedWriter = FileOp.getFileWriter("log/", filename);
    }

    /**
     * Close the writer's file handler.
     */
    public static void closeWriter() {
        try {
            // bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Print out messages on both the stdout and the output file.
     *
     * @param str the message to be outputted.
     */
    public static void pl(String str) {
        print(str);
    }

    /**
     * Print out message
     *
     * @param str a array of strings
     */
    @SuppressWarnings("Since15")
    public static void pl(String[] str) {
        print(String.join(" ", str));
    }

    /**
     * Print out the message with more complex arguments.
     *
     * @param str  the message string with formatting symbols.
     * @param args the arguments for the formatting symbols.
     */
    public static void pl(String str, Object... args) {
        String display = String.format(str, args);
        print(display);
    }

    /**
     * Print out the message with more complex arguments plus padding in the front
     *
     * @param str  the message string with formatting symbols.
     * @param args the arguments for the formatting symbols.
     */
    public static void pl(int padding, String str, Object... args) {

        for (int i = 0; i < padding; i++) {
            str = "\t" + str;
        }
        String display = String.format(str, args);

        print(display);
    }

    /**
     * a generic print function
     *
     * @param display the string to be displayed
     */
    private static void print(String display) {
        DateTime dt = TimeUtil.getLocalTime();

        if (outputTime) {
            // if outputTime is true, add the current time stamp at the beginning of each line.
            display = dt.toString() + " : " + display;
        }

        System.out.println(display);
        System.out.flush();

        if (bufferedWriter != null) {
            try {
                bufferedWriter.write(display + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void writeMap(String filename, Map<String, Double> map) {
        try {
            BufferedWriter bw = FileOp.getFileWriter(filename);
            for (String key : map.keySet()) {
                bw.write(String.format("%s,%f\n", key, map.get(key)));
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMapInt(String filename, Map<String, Integer> map) {
        try {
            BufferedWriter bw = FileOp.getFileWriter(filename);
            for (String key : map.keySet()) {
                bw.write(String.format("%s,%d\n", key, map.get(key)));
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write a list of values to a file.
     *
     * @param folder    folder of the file
     * @param filename  file name
     * @param list      the list of Double values.
     * @param withCount whether to write a count index to the file or not.
     */
    public static void writeList(String folder, String filename, List<Double> list, boolean withCount) {
        try {
            BufferedWriter bw = FileOp.getFileWriter(folder, filename + ".tsv");
            if (withCount)
                bw.write(String.format("count\t"));
            bw.write(String.format("value\n"));

            int count = 0;
            for (double value : list) {
                if (withCount)
                    bw.write(String.format("%d\t", count++));
                bw.write(String.format("%f\n", value));
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a list of values to a file.
     *
     * @param filename  file name
     * @param list      the list of Double values.
     * @param withCount whether to write a count index to the file or not.
     */
    public static void writeList(String filename, List<Double> list, boolean withCount) {
        try {
            BufferedWriter bw = FileOp.getFileWriter(filename + ".tsv");
            if (withCount)
                bw.write(String.format("count\t"));
            bw.write(String.format("value\n"));

            int count = 0;
            for (double value : list) {
                if (withCount)
                    bw.write(String.format("%d\t", count++));
                bw.write(String.format("%f\n", value));
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the list of Map to files. The Map has the format of <String, Double>
     *
     * @param folder    file folder
     * @param filename  file name
     * @param maps      the list of maps to output
     * @param withCount whether to output the index values
     */
    public static void writeListofMaps(String folder, String filename, List<Map<String, Double>> maps, String[] attrNames, boolean withCount) {
        try {
            List<String> outputArray = new ArrayList<>();
            Joiner joiner = Joiner.on("\t");

            BufferedWriter bw = FileOp.getFileWriter(folder, filename + ".tsv");

            if (withCount) {
                bw.write("count\t");
            }
            for (String attr : attrNames) {
                outputArray.add(String.format("%s", attr));
            }
            bw.write(joiner.join(outputArray));
            bw.write("\n");

            int count = 0;
            for (Map<String, Double> map : maps) {
                outputArray.clear();
                if (withCount) {
                    outputArray.add(String.format("%d", count++));
                }
                for (String attr : attrNames) {
                    double value = map.get(attr);
                    outputArray.add(String.format("%.3f", value));
                }
                bw.write(joiner.join(outputArray));
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
