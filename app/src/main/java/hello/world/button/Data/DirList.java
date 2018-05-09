package hello.world.button.Data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hello.world.button.R;
import hello.world.button.Utils.FileStorageHelper;
import hello.world.button.Utils.SPUtils;

public class DirList {
    public static List<File> getDirList() {
        return dirList;
    }

    public static List<Fruit> getFruitList() {
        return fruitList;
    }

    private static List<Fruit> fruitList = new ArrayList<Fruit>();
    private static List<File> dirList;
    public static void initFruits(Context mContext) {
        List<File> fileList;
        //获取内置存储位置
        String path= FileStorageHelper.getStoragePath(mContext,false);
        File file1 = new File(path+"/conW/");
        if (!file1.exists())file1.mkdir();
        dirList=FileStorageHelper.showDirDir(file1.toString());
        if (dirList==null || dirList.size()==0){
            Toast.makeText(mContext,"无内容，或进入app中添加信息",Toast.LENGTH_LONG).show();
            return;
        }

        Random random = new Random();
        fruitList.clear();
        if (dirList==null) {
            Toast.makeText(mContext,"可能手机连接电脑中，功能暂时无法使用",Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < dirList.size(); i++) {
            fileList= FileStorageHelper.showDirFile(dirList.get(i).toString());
            Fruit mango;
            if (fileList.size()!=0){
                File file=fileList.get(random.nextInt(fileList.size()));
                //传递   联系人名字，是否是删除状态，目录位置
                mango = new Fruit(getDirName(dirList.get(i)), file, false,dirList.get(i));
            }else {
                String dirName=getDirName(dirList.get(i));
                //解决错误文件名问题
                if (dirName!=null){
                    mango = new Fruit(getDirName(dirList.get(i)), R.drawable.ic_menu_cc_am, false,dirList.get(i));
                }else {continue;}
            }
            fruitList.add(mango);
        }
        setIdPath(mContext);
    }

    private static void setIdPath(Context mContext) {
        SPUtils IdPath=new SPUtils("IdPath");
        Map<String,?> map =IdPath.getAll(mContext);
        int n=-1;
        //todo 判断fruitList.size()等于0的情况是挂载U盘还是没文件夹
        //本来没有数据库情况
        if (map.size()==0){
            for (int i=0;i<fruitList.size();i++){
                //key以负数减一方法记录
                IdPath.put(mContext,""+(-i-1),fruitList.get(i).getDir().toString());
            }
        }else {
            //有数据库情况
            if (map.size()>0 && fruitList.size()>0){
                //判断文件夹路径是否有对应map中的数据————————不让就是错误数据，删除
                Collection<String> keySet=map.keySet();
                int minInId=minInArray(keySet.toArray());
                if(minInId>0){
                    //以-1开始添加
                }else {
                    n=minInId-1;
                }
                for (int i=0;i<fruitList.size();i++){
                    if (!(map.values().contains(fruitList.get(i).getDir().toString()))){
                        //没有的话，添加
                        IdPath.put(mContext,""+n,fruitList.get(i).getDir().toString());
                        n--;
                    }
                }
                //判断map中是否有对应文件夹路径的数据————不然的话就是无效数据，删除
                Collection<?> values=map.values();
                ArrayList<String> fruitPath=new ArrayList<String>();
                for (int o=0;o<fruitList.size();o++){
                    fruitPath.add(fruitList.get(o).getDir().toString());
                }
                for (int i=0;i<map.size();i++){
                    String inMapPath=values.toArray()[i].toString();
                    if (!(fruitPath.contains(inMapPath))){
                        List<String> keyList=IdPath.findValue(mContext,inMapPath);
                        for (int p=0;p<keyList.size();p++){
                            IdPath.remove(mContext,keyList.get(p));
                        }

                    }
                }
            }
        }
    }
    //获取数组中最小值
    public static int minInArray(Object[] array){
        int minValue = Integer.valueOf(array[0].toString());
        for (int i = 0; i<array.length;i++){
            if (Integer.valueOf(array[i].toString())<minValue)
                minValue = Integer.valueOf(array[i].toString());
        }
        return minValue;
    }
    //获取联系人名字
    protected static String getDirName(File name) {
        String pathName=name.toString();
        String dirName=pathName.substring(pathName.lastIndexOf("/") + 1);
        //char b=dirName.charAt(dirName.length()-1);
        //boolean a=Character.isDigit(b);
        int i=dirName.length()-1;
        int n=0;
        for (;n<dirName.length();n++){
            if (    Character.isDigit(dirName.charAt(dirName.length()-1-n))){

            }else {
                break;
            }
        }
        try {
            return dirName.substring(0,dirName.length()-n);
        }catch (java.lang.StringIndexOutOfBoundsException e){
            return null;
        }
    }
}
