package com.example.demo.controller;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class write {
    public static void writeManifest(String outPath) throws IOException {
        write w = new write();
        try {
            String s = w.readFile(outPath + "\\temp.txt");
            w.writeFile(outPath + "\\pic.txt", s, 0, s.length() / 2);
            System.out.println(outPath + "\\pic.txt");
        } catch (IOException e) {
            System.out.println("Error reading file " + outPath + ".txt. Skipping to the next file.");
            // 可以在这里记录错误日志或者执行其他操作
        }
        File test = new File(outPath + "\\pic.txt");
        if (!test.exists()) {
            System.out.println("没有文件");
        } else if (test.length() == 0) {
            System.out.println("  no txt Color.");
        } else {
            hexToImage h = new hexToImage();
            String s = h.readFileContent(outPath + "\\pic.txt");
            byte[] bytes = h.hexToByteArray(s);
            if (bytes.length / 600 <= 0) {
                System.out.println("data is small, can be ignored");
            } else {
                h.rgbBytesToJpg(bytes, 200, bytes.length / 600, outPath + "\\manifest.jpg");
            }
        }

    }

    public void mergeFiles(int threadNum, String[] inpath, String outpath) throws IOException {
        if (threadNum >= 1 && threadNum <= inpath.length) {
            List<HashMap<String, Integer>> hashMaps = new ArrayList<>();
            List<String> results = new ArrayList<>();

            // 获取所有输入文件的信息和内容
            for (int i = 0; i < threadNum; i++) {
                hashMaps.add(getInfo(inpath[i]));
                results.add(readFile(inpath[i]));
            }

            // 写入信息到输出文件
            for (int i = 0; i < threadNum; i++) {
                writeFile(outpath, results.get(i), hashMaps.get(i).get("stringIdOff"), hashMaps.get(i).get("stringIdSize"));
                writeFile(outpath, results.get(i), hashMaps.get(i).get("typeIdOff"), hashMaps.get(i).get("typeIdSize"));
                writeFile(outpath, results.get(i), hashMaps.get(i).get("protoIdOff"), hashMaps.get(i).get("protoIdSize"));
                writeFile(outpath, results.get(i), hashMaps.get(i).get("fieldIdOff"), hashMaps.get(i).get("fieldIdSize"));
                writeFile(outpath, results.get(i), hashMaps.get(i).get("methodIdOff"), hashMaps.get(i).get("methodIdSize"));
                writeFile(outpath, results.get(i), hashMaps.get(i).get("classIdOff"), hashMaps.get(i).get("classIdSize"));
            }

            // 处理特殊情况，写入额外数据到输出文件
            if (threadNum == 1) {
                writeFile(outpath, results.get(0), 0, results.get(0).length() / 10);
            }
        } else {
            throw new IllegalArgumentException("Invalid threadNum: " + threadNum);
        }

    }

    public String readFile(String inpath) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(inpath));

        String result = "";
        byte[] b = new byte[20000];
        while (in.read(b) != -1 && result.length() < 4000000) {
            result += DatatypeConverter.printHexBinary(b);
        }
        return result;

    }

    public void writeFile(String out, String str, int offset, int size) throws IOException {
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(out, true));
        BufferedWriter writer = new BufferedWriter(write);
        writer.write(str.substring(offset * 2, size * 2 + offset * 2));
        writer.flush();
        write.close();
        writer.close();

    }

    public HashMap<String, Integer> getInfo(String path) throws IOException {
        HashMap<String, Integer> info = new HashMap<>();
        DataInputStream in = new DataInputStream(new FileInputStream(path));

        ArrayList<String> result = new ArrayList<>();
        byte[] b = new byte[1];
        while (in.read(b) != -1 && result.size() < 112) {
            result.add(DatatypeConverter.printHexBinary(b));
        }

        info.put("stringIdSize", HexToInt(new ArrayList(result.subList(56, 60))));
        info.put("stringIdOff", HexToInt(new ArrayList(result.subList(60, 64))));
        info.put("typeIdSize", HexToInt(new ArrayList(result.subList(64, 68))));
        info.put("typeIdOff", HexToInt(new ArrayList(result.subList(68, 72))));
        info.put("protoIdSize", HexToInt(new ArrayList(result.subList(72, 76))));
        info.put("protoIdOff", HexToInt(new ArrayList(result.subList(76, 80))));
        info.put("fieldIdSize", HexToInt(new ArrayList(result.subList(80, 84))));
        info.put("fieldIdOff", HexToInt(new ArrayList(result.subList(84, 88))));
        info.put("methodIdSize", HexToInt(new ArrayList(result.subList(88, 92))));
        info.put("methodIdOff", HexToInt(new ArrayList(result.subList(92, 96))));
        info.put("classIdSize", HexToInt(new ArrayList(result.subList(96, 100))));
        info.put("classIdOff", HexToInt(new ArrayList(result.subList(100, 104))));

        in.close();

        return info;
    }

    public static int HexToInt(ArrayList<String> arrayList) {
        Collections.reverse(arrayList);
        String a = "";
        for (int i = 0; i < arrayList.size(); i++) {
            a += arrayList.get(i);
        }
        return Integer.parseInt(a, 16);
    }

}
