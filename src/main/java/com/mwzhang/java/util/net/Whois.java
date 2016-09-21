package com.mwzhang.java.util.net;

import com.mwzhang.java.util.io.FileOp;
import com.mwzhang.java.util.io.Output;
import org.apache.commons.net.whois.WhoisClient;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mingwei on 10/8/15.
 * <p>
 * Whois utility function using Team Cymru's Whois service.
 * <p>
 * RIPE response:
 * route:        8.0.0.0/8
 * origin:       AS3356
 * descr:        LEVEL3 - Level 3 Communications, Inc.,US
 * lastupd-frst: 2015-05-12 06:31Z  80.81.192.194@rrc12
 * lastupd-last: 2015-10-08 23:37Z  80.81.192.14@rrc12
 * seen-at:      rrc00,rrc01,rrc03,rrc04,rrc05,rrc06,rrc07,rrc10,rrc11,rrc12,rrc13,rrc14,rrc15
 * num-rispeers: 114
 * source:       RISWHOIS
 * <p>
 * ... (repeat)
 * <p>
 * Cymru response:
 * AS      | IP               | BGP Prefix          | AS Name
 * 15169   | 8.8.8.8          | 8.8.8.0/24          | GOOGLE - Google Inc.,US
 */
public class Whois {

    WhoisClient whois;

    public Whois() {
        whois = new WhoisClient();
    }

    public static void main(String[] args) throws IOException {

        Whois w = new Whois();

        w.processFile("/Users/mingwei/sourceIps.txt", "/Users/mingwei/sourceASes.csv");

    }

    public void processFile(String inputName, String outputName) {

        List<String> ipList = readIpList(inputName);

        try {

            List<Triplet<String, String, String>> triplets = bunchQueryASDetails(ipList);
            outputTriplet(triplets, outputName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private List<String> readIpList(String filename) {

        List<String> resList = new ArrayList<>();

        try {

            BufferedReader reader = FileOp.getBufferedReader(filename);
            String line;
            Output.outputTime = false;
            while ((line = reader.readLine()) != null) {
                resList.add(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resList;
    }

    private void bunchQueryAS(String filename) {
        try {

            BufferedReader reader = FileOp.getBufferedReader("/Users/mingwei/", filename);
            String line;
            Output.outputTime = false;
            Map<String, String> ases = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                Output.pl(line);
                whois.connect("riswhois.ripe.net");
                String res = whois.query(line + "/32");
                whois.disconnect();
                Pattern pattern = Pattern.compile(".*origin:.*AS(.*)\n");
                Matcher matcher = pattern.matcher(res);
                if (matcher.find()) {
                    Output.pl(matcher.group(1));
                    ases.put(line, matcher.group(1));
                }
            }
            reader.close();

            BufferedWriter writer = FileOp.getFileWriter("/Users/mingwei/" + filename + ".ases");
            for (String ip : ases.keySet()) {
                writer.write(String.format("%s,%s\n", ip, ases.get(ip)));
                Output.pl(String.format("%s,%s", ip, ases.get(ip)));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Triplet<String, String, String>> bunchQueryASDetails(List<String> ipAddresses) throws IOException {

        Output.outputTime = false;

        List<Triplet<String, String, String>> entries = new ArrayList<>();

        for (String ip : ipAddresses) {
            Output.pl(ip);
            whois.connect("riswhois.ripe.net");
            String res = whois.query(ip + "/32");
            whois.disconnect();
            Pattern pattern1 = Pattern.compile(".*origin:(.*)\n");
            Matcher matcher1 = pattern1.matcher(res);
            Pattern pattern2 = Pattern.compile(".*descr:(.*)\n");
            Matcher matcher2 = pattern2.matcher(res);

            String asn = "", descr = "";
            if (matcher1.find()) {
                asn = matcher1.group(1);
            }

            if (matcher2.find()) {
                descr = matcher2.group(1);
            }

            entries.add(new Triplet<>(ip, asn, descr));
        }

        return entries;
    }

    public void outputTriplet(List<Triplet<String, String, String>> entries, String filename) throws IOException {

        BufferedWriter writer = FileOp.getFileWriter(filename);
        for (Triplet<String, String, String> triplet : entries) {
            writer.write(String.format("%s,%s,%s\n", triplet.getValue0(), triplet.getValue1(), triplet.getValue2()));
            // Output.pl(String.format("%s,%s,%s", triplet.getValue0(), triplet.getValue1(), triplet.getValue2()));
        }
        writer.close();

    }
}
