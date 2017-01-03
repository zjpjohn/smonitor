package com.harlan.smonitor.monitor.common;

import java.io.*;

/**
 * FileUtil
 * Created by harlan on 2016/12/2.
 */
public class FileUtil {
    /**
     * 文件重写
     * @param fileName 文件的绝对路径（目标文件的目录不存在会报错）
     * @param content 内容
     * @throws IOException
     */
    public static void rewrite(String fileName, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(content);
        out.close();
    }
    public static String read(String fileName)throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        StringBuffer sb=new StringBuffer();
        String buf;
        while ((buf = reader.readLine()) != null) {
            sb.append(buf);
        }
        reader.close();
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String file="E:/Sitech/svn/test_temp/monitor/123.json";
//            String content="a";
//            rewrite(file,content);
            String content= read(file);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

