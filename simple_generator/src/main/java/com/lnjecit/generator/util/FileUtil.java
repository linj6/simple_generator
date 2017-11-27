package com.lnjecit.generator.util;

import java.io.*;

/**
 * 文件操作工具类
 */
public class FileUtil {

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        String tempString = "";
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                buffer.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return buffer.toString();
    }

    public static String readFileByChar(String fileName) {
        StringBuffer content = new StringBuffer();
        StringBuffer buffer = new StringBuffer();
        int iCharNum = 0;
        Reader in = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                iCharNum += 1;
                buffer.append((char) ch);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        content.append(buffer);
        System.out.println(content);
        return content.toString();
    }

    public static void main(String[] args) {
        //readFileByLines("E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\service.html");
        readFileByChar("E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\service.html");
    }
}
