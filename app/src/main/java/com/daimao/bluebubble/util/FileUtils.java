package com.daimao.bluebubble.util;

import android.util.Log;


import com.daimao.bluebubble.exception.IllegalFilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 从完全名字获得文件名
     * 
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (path.equals("/")) {
            return "/";
        }
        String name = null;
        int index = 0;
        index = path.lastIndexOf("/");
        if (index == -1 || !path.startsWith("/")) {
            try {
                throw new IllegalFilePath("非法的标准路径：" + path);
            } catch (IllegalFilePath e) {
                e.printStackTrace();
            }
        }
        name = path.substring(index + 1);
        return name;
    }

    public static boolean isHideFile(String fileName) {
        if (fileName.startsWith(".")) {
            return true;
        }
        return false;
    }



    // /* 简单判断文件路径是否合法 */
    // public static boolean isLegalPath(String path) {
    //
    // if (path == null) {
    // Log.e(TAG, "文件路径是空值");
    // return false;
    // }
    // if (path.startsWith("/")) {
    // return true;
    // }
    // return false;
    // }

    /* 获取文件大小，如果是文件夹则返回子目录大小，文件则返回字节数 */
    public static long getChildCount(String path) {
        File file = new File(path);
        return getChildCount(file);
    }

    public static long getChildCount(File file) {
        if (!file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            } else {
                return files.length;
            }
        }
        return file.length();
    }



    /**
     * 判断路径是否合法
     * 
     * @param path
     * @return
     */
    public static boolean isLegalPath(String path) {
        if (path == null) {
            Log.e(TAG, "文件路径是空值");
            return false;
        }
        String regex = "(?:[/\\\\][^/\\\\:*?\"<>|]{1,255})+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        boolean ret = matcher.matches();
        return ret;
    }


    /**重命名文件
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean rename(String oldPath, String newPath) {
        File file = new File(oldPath);
        File fileNew = new File(newPath);
        boolean result = false;
        try {
            result = file.renameTo(fileNew);
        } catch (Exception e) {
            Log.e(TAG, "重命名失败");
        }
        return result;
    }

    public static boolean moveTo(File oldFile, File newFile){
        boolean result = false;
        long time = System.currentTimeMillis();
        try {
            result = oldFile.renameTo(newFile);
        } catch (Exception e) {
            Log.e(TAG, "重命名失败");
        }
        if(!result){
            int length = 2097152;
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(oldFile);
                out = new FileOutputStream(newFile);
                FileChannel inC = in.getChannel();
                FileChannel outC = out.getChannel();
                ByteBuffer b = null;
                while(true){
                    if( inC.position() == inC.size()){
                        inC.close();
                        outC.close();
                        Log.d(TAG, "移动文件成功！time:"+ String.valueOf(System.currentTimeMillis() - time));
                        oldFile.delete();
                        return true;
                    }
                    if((inC.size()-inC.position()) < length){
                        length = (int)(inC.size() - inC.position());
                    }else{
                        length = 2097152;
                    }
                    b = ByteBuffer.allocateDirect(length);
                    inC.read(b);
                    b.flip();
                    outC.write(b);
                    outC.force(false);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return false;
        }
        return result;

    }




    // 递归
    public static long getFileSize(File file) {
        long size = 0;

        if (file.isFile()) {
            return size += file.length();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    size = size + getFileSize(files[i]);
                }
            }
        }
        return size;
    }

    /**
     * 获取指定路径文件的总大小
     * 
     * @param files
     * @return
     */
    public static long getFileSize(List<String> files) {
        long size = 0;

        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            if (file.exists() && file.canRead()) {
                size += getFileSize(file);
            }
        }
        return size;
    }

    public static String getNewFolderPath(String oldPath) {
        String path = oldPath + "-副本";
        if (new File(path).exists()) {
            path = getNewFolderPath(path);
        }
        return path;
    }

    public static String getNewFileName(String newPath) {
        String filePath = null;

        int index = newPath.lastIndexOf(".");
        if (index == -1 || index == 0 || (index == newPath.length() - 1)) {
            filePath = newPath + "-副本";
            if (new File(filePath).exists()) {
                filePath = getNewFileName(filePath);
            }
        } else {
            filePath = newPath.substring(0, index) + "-副本" + newPath.substring(index);
            if (new File(filePath).exists()) {
                filePath = getNewFileName(filePath);
            }
        }
        return filePath;

    }

    /**
     * 移动
     * 
     * @param sourceFiles
     * @param targetPath
     * @return
     */
    public static int moveTo(List<String> sourceFiles, String targetPath) {
        int fail = 0;
        for (int i = 0; i < sourceFiles.size(); i++) {

            File oldFile = new File(sourceFiles.get(i));
            if (targetPath.endsWith("/")) {
                targetPath = targetPath + oldFile.getName();
            } else {
                targetPath = targetPath + File.separator + oldFile.getName();
            }
            File newFile = new File(targetPath);
            boolean flag = oldFile.renameTo(newFile);
            if (!flag) {
                fail++;
            }
        }
        return fail;
    }



    // 检测文件是否合法
    public static void checkFile(ArrayList<String> paths) {

        for (int i = 0; i < paths.size(); i++) {

            File file = new File(paths.get(i));
            if (!file.exists()) {
                paths.remove(i);
                i--;
            }
        }
    }

    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 生成路径栈
     * 
     * @param path
     * @return
     */
    public static List<String> generatePathStack(String path) {

        File file = new File(path);
        List<String> list = new ArrayList<String>();
        String[] fileNames = path.split("/");
        int count = fileNames.length;
        if (file.exists() && file.isFile()) {
            count--;
        }
        StringBuilder builder = new StringBuilder();
        list.add("/");
        for (int i = 1; i < count; i++) {
            builder.append("/" + fileNames[i]);
            list.add(builder.toString());
        }
        return list;
    }
}