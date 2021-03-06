package hello.world.button.Utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lybly on 2018/2/8.
 * 增删改查文件管理
 */

public class FileStorageHelper {

//    **
//    * 复制res/raw中的文件到指定目录
//    * @param context 上下文
//    * @param id 资源ID
//    * @param fileName 文件名
//    * @param storagePath 目标文件夹的路径
//    */

    /*
    demo
    FileStorageHelper.copyFilesFromRaw(this,R.raw.doc_test,"doc_test",path + "/" + "mufeng");
    上面代码是将raw中的doc_test复制到/mufeng下
    */
    public static void copyFilesFromRaw(Context context, int id, String fileName, String storagePath) {
        InputStream inputStream = context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        readInputStream(storagePath + File.separator + fileName, inputStream);
    }

    /**
     * 读取输入流中的数据写入输出流
     *
     * @param storagePath 目标文件路径
     * @param inputStream 输入流
     */
    public static void readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        try {
            if (!file.exists()) {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //删除一个文件 (不是文件夹)
    public static boolean delAFile(String delAFilePath) {
        //判断文件存不存在 不存在写入日志
        //有无斜杠判断
        //删除失败与文件不存在的日志情况
        //这里写了两次日志
        if (!delAFilePath.endsWith(File.separator)) {
            File file = new File(delAFilePath);
            if (file.delete())
                return true;
        } else {
            //写入日志
            String msm = "delAFile: " + delAFilePath + "  no a file,is dir";
            Log.d(TAG, msm);

        }
        //写入日志
        String msm = "delAFile: " + delAFilePath + "  del false";
        Log.d(TAG, msm);
        return false;
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    //更改
    ///sdcard/aaaaaa是不合格的
    ///sdcard/aaaaaa/才行
    //写入日志
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileStorageHelper.delAFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileStorageHelper.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！" + dir);
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }


    //改地方 调用自己的删除
    //遍历删除文件夹下包含指定字符的文件
    //传入  文件路径
    //文件名
    //从那开始判断
    public static void travDeleteFilesLikeName(String dirPath, String likeName, int startNum) {
        File file = new File(dirPath);
        if (file.isFile()) {
            //是文件
            String temp = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (temp.indexOf(likeName) == startNum) {
                file.delete();
            }
        } else {
            //是目录
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                travDeleteFilesLikeName(files[i].toString(), likeName, startNum);
            }
        }
    }

    //不遍历删除文件夹下包含指定字符的文件
    //传入  文件路径
    //文件名
    //从那开始判断
    public static void deleteFilesLikeName(String dirPath, String likeName, int startNum) {
        File file = new File(dirPath);
        if (file.isFile()) {
            //是文件
            String temp = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (temp.indexOf(likeName) == startNum) {
                file.delete();
            }
        } else {
            //是目录
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {

                if (files[i].toString().indexOf(likeName) == (startNum + dirPath.length())) {
                    deleteDirectory(files[i].toString());
                    Log.d(TAG, files[i].toString());
                }
            }
        }
    }


    //获取内置外置存储卡
    //true外置
    //false内置
    private  static String rempath = null; //= getStoragePath(MainActivity.this, true);
    private static String norempath=null;//= getStoragePath(MainActivity.this, false);

    public static String getStoragePath(Context mContext, boolean is_removale) {
        if (is_removale) {
            if (rempath != null)
                return rempath;
        } else {
            if (norempath != null)
                return norempath;
        }
        return StoragePath(mContext, is_removale);
    }

    public static String StoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    if (is_removale)rempath=path;else norempath=path;
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回文件夹里面的文件(不包括文件夹与文件夹里面的内容)
    public static List<File> showDirFile(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("目录：" + dir + "不正确");
            return null;
        }
        System.out.println("目录：" + dir);
        File[] files = dirFile.listFiles();
        List<File> returnFiles = new ArrayList<File>();
        int n = 0;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                returnFiles.add(files[i]);
                n++;
            }
        }
        return returnFiles;
    }

    //返回文件夹里面的文件夹,不包括文件
    public static List<File> showDirDir(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("目录：" + dir + "不正确");
            return null;
        }

        File[] files = dirFile.listFiles();
        List<File> returnFiles = new ArrayList<File>();
        int n = 0;
        if (files==null){
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                returnFiles.add(files[i]);
                n++;
            }
        }
        return returnFiles;
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            //e.printStackTrace();
        }
        return false;

    }
}


