package com.example.demo.controller;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class getDex {
    public static void extractDexFiles(String zipPath, String outPath) {
        // 创建File对象
        File directory = new File(outPath);
        // 确保这是一个目录
        if (directory.isDirectory()) {
            // 获取目录中的所有文件和子目录
            File[] files = directory.listFiles();

            if (files != null) {
                // 遍历每个文件并删除
                for (File file : files) {
                    deleteFile(file);
                }
            }

            System.out.println("目录已清空。");
        } else {
            System.out.println("指定路径不是一个目录。");
        }


        try (BufferedWriter errorLog = new BufferedWriter(new FileWriter("error_log.txt", true))) {
                File file = new File(zipPath);
                if (!file.isFile()) {
                    errorLog.write("File not found: " + zipPath);
                    errorLog.newLine();
                }

                try (ZipFile zipFile = new ZipFile(file)) {
                    Enumeration<? extends ZipEntry> files = zipFile.entries();
                    ZipEntry entry = null;
                    File outFile = null;
                    BufferedInputStream bin = null;
                    BufferedOutputStream bout = null;
                    while (files.hasMoreElements()) {
                        entry = files.nextElement();
                        outFile = new File(outPath + File.separator + entry.getName());
                        if (entry.isDirectory()) {
                            outFile.mkdirs();
                            continue;
                        }
                        if (!outFile.getParentFile().exists()) {
                            outFile.getParentFile().mkdirs();
                        }
                        outFile.createNewFile();
                        if (!outFile.canWrite()) {
                            continue;
                        }
                        try {
                            bin = new BufferedInputStream(zipFile.getInputStream(entry));
                            bout = new BufferedOutputStream(new FileOutputStream(outFile));
                            byte[] buffer = new byte[1024];
                            int readCount = -1;
                            while ((readCount = bin.read(buffer)) != -1) {
                                bout.write(buffer, 0, readCount);
                            }
                        } finally {
                            try {
                                if (bin != null) bin.close();
                                if (bout != null) {
                                    bout.flush();
                                    bout.close();
                                }
                            } catch (Exception e) {
                                // Ignored
                            }
                        }
                    }
                } catch (IOException e) {
                    errorLog.write("Error processing file: " + zipPath + " - " + e.getMessage());
                    errorLog.newLine();
                }


                String path = outPath;
                file = new File(path);
                if (file.isDirectory()) {
                    File[] fs = file.listFiles();
                    if (fs != null) {
                        for (File f : fs) {
                            if (f.isDirectory()) {
                                deleteDir(f);
                            } else if (!f.getName().endsWith("dex")) {
                                f.delete();
                            }
                        }
                    }
                }
                System.out.println( " delete complete");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 删除文件或目录的方法
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            // 递归删除子目录中的内容
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteFile(subFile);
                }
            }
        }
        // 删除文件或空目录
        if (file.delete()) {
            System.out.println("已删除: " + file.getAbsolutePath());
        } else {
            System.out.println("无法删除: " + file.getAbsolutePath());
        }
    }
}
