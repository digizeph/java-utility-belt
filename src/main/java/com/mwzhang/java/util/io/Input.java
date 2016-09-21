package com.mwzhang.java.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mingwei Zhang on 2/18/15.
 * <p/>
 * Utility class that process files and generate data structure back.
 */
public class Input {

    /**
     * Read file and get all columns.
     *
     * @param folder   the folder of the file
     * @param filename the name of the file
     * @return a Map with column title as key and column values as a list
     */
    public static Map<String, List<Double>> getColumns(String folder, String filename) {
        Map<String, List<Double>> columns = new HashMap<>();
        String line;
        String[] titles = new String[0];

        BufferedReader br = FileOp.getBufferedReader(folder, filename);
        try {
            // read title first.
            if ((line = br.readLine()) != null) {
                // parse tsv
                titles = line.trim().split("\\t");
                for (String title : titles) {
                    if (!columns.containsKey(title)) {
                        columns.put(title, new ArrayList<Double>());
                    }
                }
            }
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] valueStrs = line.trim().split("\\t");
                for (int i = 0; i < valueStrs.length; i++) {
                    double value = Double.parseDouble(valueStrs[i]);
                    columns.get(titles[i]).add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        columns.remove("count");
        return columns;
    }

    public static void main(String[] args) {
        Map<String, List<Double>> map = getColumns(".", "2006_12_26_195.69.144.34_raw_mon_values.tsv");
        for (String title : map.keySet()) {
            Output.pl(title);
        }

    }

}
