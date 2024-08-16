package com.example.demo.controller;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.exception.ParserException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class getManifest {
    public static void extractManifest(String path, String outPath) {
        String errorLogPath = "malware_bnifest_error_log.txt"; // 错误日志文件路径
        try (PrintWriter errorLogWriter = new PrintWriter(new FileWriter(errorLogPath, true))) {
            String out = outPath + "\\" + "temp.txt";
            try {
                if (manifestGet(path, out)) {
                    System.out.println("manifest complete");
                } else {
                    File file = new File(out);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    System.out.println("No manifest found for: " + path);
                }
            } catch (IOException e) {
                System.out.println("IO error processing file: " + path);
                errorLogWriter.println("IO error processing file: " + path);
                e.printStackTrace(errorLogWriter);
            } catch (Exception e) {
                System.out.println("Unexpected error processing file: " + path);
                errorLogWriter.println("Unexpected error processing file: " + path);
                e.printStackTrace(errorLogWriter);
            }

        } catch (IOException e) {
            System.out.println("Error opening error log file.");
            e.printStackTrace();
        }
    }

    public static boolean manifestGet(String path, String outpath) throws IOException {
        try {
            ApkFile apkFile = new ApkFile(new File(path));
            String manifestXml = apkFile.getManifestXml();
            try (FileWriter fwriter = new FileWriter(outpath, false)) {
                fwriter.write(manifestXml);
            }
            return true;
        } catch (ParserException e) {
            System.out.println("No manifest in file: " + path);
            return false;
        } catch (IOException e) {
            System.out.println("Error writing file: " + outpath);
            throw e;
        }
    }
}
