package hello.world.button.View;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hello.world.button.Data.Fruit;
import hello.world.button.Data.FruitAdapter;
import hello.world.button.R;
import hello.world.button.Utils.FileStorageHelper;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.PendingIntent.getActivity;
import static hello.world.button.Data.FruitAdapter.InPic;

public class CPicActivity extends AppCompatActivity implements FruitAdapter.OnSuccessListener  {
    private List<Fruit> fruitList = new ArrayList<Fruit>();
    protected boolean indelete;
    private FruitAdapter.ViewHolder holder;
    private String dirPath;//当前联系人路径
    private String contactName;
    public static final int CHOOSE_PHOTO = 2;
    public static final int EDIT_PHOTO = 3;
    protected FruitAdapter adapter;
    protected RecyclerView recyclerView;
    protected StaggeredGridLayoutManager layoutManager;
    protected Fruit addfruit;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //用来接受剪切后图片
        localBroadcastManager = LocalBroadcastManager.getInstance(this); // 获取实例
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactName=getIntent().getStringExtra("name");
        setTitle(contactName + "的图片");
        dirPath = getIntent().getStringExtra("path");

        initFruits();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FruitAdapter(fruitList, InPic);
        layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnDelListener(this);
        adapter.setOnUpdateListener(this);
        adapter.setOnUpdateAllListener(this);
        adapter.setOnSelPic(this);
        addfruit = new Fruit("add", R.drawable.ic_menu_cc_am, false, (new File("")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        indelete = !indelete;
        for (int i = 0; i < fruitList.size(); i++) {
            fruitList.get(i).setInDelete(indelete);
        }
        if (indelete == true) {
            Toast.makeText(CPicActivity.this, "添加或删除图片", Toast.LENGTH_SHORT).show();
            addfruit.setInDelete(true);
            fruitList.add(0, addfruit);
            item.setIcon(R.drawable.ic_cab_done_holo_light);
        } else {
            fruitList.remove(0);
            item.setIcon(R.drawable.ic_menu_edit);
        }

        adapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    protected void initFruits() {
        List<File> fileList = FileStorageHelper.showDirFile(dirPath);
        for (int i = 0; i < fileList.size(); i++) {
            Fruit apple = new Fruit("", fileList.get(i), false, (new File("")));
            fruitList.add(apple);

        }
    }

    @Override
    public void onSelPic() {
        this.holder = holder;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onAdapterSuccess(String FileDir) {

    }

    @Override
    public void onAdapterDel(int position) {
        if (fruitList.get(position).getImageFile().delete()){
            //todo 数据库删除
            //删除图片
            fruitList.remove(position);
            adapter.notifyItemRemoved(position);
            return;
        }
        Toast.makeText(this,"撞邪了，没法删除图片",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFalseIndelete() {

    }

    @Override
    public void onAdapterUpdate(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onAdapterUpdateAll() {
        adapter.notifyDataSetChanged();
    }


    String newFilePath = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Intent intent = new Intent("pic_path");
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String filePath;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        filePath=handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        filePath=handleImageBeforeKitKat(data);
                    }
                    newFilePath=dirPath+"/."+getSysNowTime()+".jpg";
                    if (FileStorageHelper.copyFile(filePath,newFilePath)) {
                        //编辑图片
                        Intent editPho = new Intent("com.android.camera.action.CROP");
                        editPho.setType("image/*");
                        List<ResolveInfo> list = getPackageManager().queryIntentActivities(editPho, 0);
                        int size = list.size();
                        if (size == 0) {
                            //压缩图片
                            Luban.with(this).load(newFilePath).ignoreBy(100)
                                    .setTargetDir((new File(newFilePath)).getParent()).setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    if (!file.toString().equals(newFilePath)){
                                        (new File(newFilePath)).delete();
                                        file.renameTo((new File(newFilePath)));
                                    }
                                    intent.putExtra("path", newFilePath);
                                    localBroadcastManager.sendBroadcast(intent); // 发送本地广播
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            }).launch();
                        } else {
                            //使用工具编辑图片
                            editPho.setType("image/*");
                            Uri imageUri = FileProvider.getUriForFile(CPicActivity.this,
                                    "hello.world.button.fileprovider",
                                    new File(newFilePath));
                            //通过FileProvider创建一个content类型的Uri
                            editPho.setData(imageUri);
                            editPho.putExtra("crop", "true");//选择剪切
                            editPho.putExtra("scale", false);//不按比例
                            editPho.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);//不能设置其他uri，来什么，输出什么
                            editPho.putExtra("outputFormat",
                                    Bitmap.CompressFormat.JPEG.toString());
                            editPho.putExtra("noFaceDetection", false); // 打开人脸检测
                            editPho.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//允许读写
                            editPho.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            //不指定apk打开
                            startActivityForResult(editPho, EDIT_PHOTO);
                        }
                    }
                }
                break;
            case EDIT_PHOTO:
                if (resultCode == RESULT_OK) {
                    //压缩图片
                    Luban.with(this).load(newFilePath).ignoreBy(100)
                            .setTargetDir((new File(newFilePath)).getParent()).setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            if (!file.toString().equals(newFilePath)){
                                (new File(newFilePath)).delete();
                                file.renameTo((new File(newFilePath)));
                            }
                            intent.putExtra("path", newFilePath);
                            localBroadcastManager.sendBroadcast(intent); // 发送本地广播
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    }).launch();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    // 获取现在系统时间
    public String getSysNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date());
    }

}
