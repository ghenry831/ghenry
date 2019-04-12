package com.filestudy;

import java.io.*;

public class FileOperationStudy {
    public static void main(String[] args) {

        try {

            String path = "/home/ghenry/progame/ghenry/study/222.txt";
            String content = "写入文件";

            //writeFile(path,content);//写入文件
            String fileContent = readFile(path);//读取文件
            System.out.println(fileContent);//打印读取的文件内容

            //复制文件
            String oldPath = "/home/ghenry/progame/ghenry/study/222.txt";
            String newPath = "/home/ghenry/progame/ghenry/study/222new.txt";
            copyFile(oldPath,newPath);
            System.out.println(readFile(newPath));//打印读取的文件内容


        }catch (IOException e){
            e.printStackTrace();
        }


    }

    //写入文件
    public static void writeFile(String path,String content) throws IOException {
        File file = new File(path);
        if (!file.exists()){
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PrintStream printStream = new PrintStream(fileOutputStream);
        printStream.println(content);

        printStream.close();
        fileOutputStream.close();
    }

    //读取文件
    public static String readFile(String path) throws IOException{
        File file = new File(path);

        FileInputStream fileInputStream = new FileInputStream(file);
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bytes = new byte[1024];

        while ((fileInputStream.read(bytes)) != -1){
            stringBuffer.append(new String(bytes));
            bytes = new byte[1024];
        }

        fileInputStream.close();

        return stringBuffer.toString();
    }

    //文件流方式复制文件
    public static void copyFile(String oldPath,String newPath) throws IOException{
        //读取旧文件
        FileInputStream fileInputStream = new FileInputStream(oldPath);

        //生成新文件
        File file = new File(newPath);
        if (!file.exists()){
            file.createNewFile();
        }

        //生成输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int c;

        while ((c=fileInputStream.read(bytes)) != -1){
            for(int i = 0;i<c;i++){
                fileOutputStream.write(bytes[i]);
            }
        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
