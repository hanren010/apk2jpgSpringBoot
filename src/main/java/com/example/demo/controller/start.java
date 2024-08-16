package com.example.demo.controller;

import java.io.File;
import java.io.IOException;

public class start {

    public static void startDEX(String outPath) throws IOException {

        try {
            String[] names = getFiles(outPath);
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
            }

            if (names.length == 0) {
                System.out.println(" no dex files");
            } else if (names.length == 1) {
                write wr = new write();
                String out = outPath + "\\dex.txt";
                wr.mergeFiles(names.length, names, out);
                System.out.println(out + " 1 dex txt complete");
            } else {
                write w = new write();
                String out = outPath + "\\dex.txt";
                w.mergeFiles(names.length, names, out);
                System.out.println(out + " other dex txt complete");
            }
        } catch (Exception e) {
            System.out.println("Error processing files for " + ". Skipping to the next set of files.");
            // 可以在这里记录错误日志或者执行其他操作
        }
        File file = new File(outPath + "\\dex.txt");
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("  makedir  success");
        } else {
            hexToImage test = new hexToImage();
            String s = test.readFileContent(outPath + "\\dex.txt");
            byte[] bytes = test.hexToByteArray(s);
            if (bytes.length / 3000 == 0) {
                System.out.println("no height");
            } else {
                test.rgbBytesToJpg(bytes, 200, bytes.length / 3000, outPath + "\\dex.jpg");
                System.out.println("  complete");
            }
        }

    }

    public static String[] getFiles(String filepath) {
        File file = new File(filepath);

        File[] filePathLists = file.listFiles();
        String[] path = new String[filePathLists.length];

        for (int i = 0; i < filePathLists.length; i++) {
            path[i] = filePathLists[i].getAbsolutePath();
        }

        return path;
    }




}
