package com.mwzhang.java.util.io;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.*;
import java.net.URL;

/**
 * Created by Mingwei Zhang on 2/17/14.
 * File operations.
 *
 * @author Mingwei Zhang
 */
public class FileOp {
    public static boolean folderExists(String location) {
        File f = new File(location);
        if (f.exists() && !f.isDirectory()) {
            return false;
        }
        return f.exists();
    }

    public static boolean createFolder(String location) {
        return (new File(location)).mkdirs();
    }

    public static boolean createFolderIfNotExists(String location) {
        if (folderExists(location)) {
            return true;
        }
        createFolder(location);
        return true;
    }

    public static boolean fileExists(String filepath) {
        File f = new File(filepath);
        return (f.exists() && !f.isDirectory());
    }

    public static File getFile(String filePath, boolean createIfNotExists) {

        File file = null;
        try {
            file = new File(filePath);
            // if file doesnt exists, then create it
            if (!file.exists() && createIfNotExists) {
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File getFile(String folder, String filename, boolean createIfNotExists) {
        String filePath = folder + "/" + filename;
        File file = null;


        try {
            file = new File(filePath);
            // if file doesnt exists, then create it
            if (!file.exists() && createIfNotExists) {
                if (!FileOp.folderExists(folder)) {
                    FileOp.createFolder(folder);
                }
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static BufferedWriter getFileWriter(String filename) {

        BufferedWriter bw = null;

        try {
            // create output file if not exists.
            File f = getFile(filename, true);
            FileWriter fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bw;
    }

    public static BufferedWriter getFileWriter(String folder, String filename) {

        BufferedWriter bw = null;

        try {
            // create output file if not exists.
            File f = getFile(folder, filename, true);
            FileWriter fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bw;
    }

    public static BufferedReader getBufferedReader(String filename) {

        BufferedReader br = null;

        try {
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return br;
    }

    public static BufferedReader getBufferedReader(String folder, String filename) {

        return getBufferedReader(folder + "/" + filename);
    }

    public static DateTime getLastModifiedTime(String filename) {
        DateTime mtime;
        File f = new File(filename);
        if (f.exists() && !f.isDirectory()) {
            mtime = new DateTime(f.lastModified());
        } else {
            mtime = null;
        }
        return mtime;
    }

    public static void downloadFile(String url, String folder, String filename) {
        File file = FileOp.getFile(folder, filename, true);
        try {
            FileUtils.copyURLToFile(new URL(url), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
