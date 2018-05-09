package hello.world.button.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import hello.world.button.Data.MyContacts;
import hello.world.button.Data.ContactsAdapter;
import hello.world.button.R;
import hello.world.button.SaveContactLR;
import hello.world.button.Utils.FileStorageHelper;
import hello.world.button.Utils.PermissionHelper;
import hello.world.button.Utils.PermissionInterface;

public class LocConActivity extends AppCompatActivity implements PermissionInterface {
    private static PermissionHelper mPermissionHelper;
    private List<MyContacts> contactList = new ArrayList<MyContacts>();
    ContactsAdapter adapter;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(LocConActivity.this, this);
        mPermissionHelper.requestPermissions();
        localBroadcastManager  = LocalBroadcastManager.getInstance(LocConActivity.this); // 获取实例
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("用户同意权限", "user granted the permission!");
                    requestPermissionsSuccess();
                } else {
                    // permission denied, boo! Disable the
                    // 用户不同意 可以给一些友好的提示
                    Log.i("用户不同意权限", "user denied the permission!");
                    requestPermissionsFail();
                }
                return;
            }
        }
    }

    @Override
    public int getPermissionsRequestCode() {
        //设置权限请求requestCode，只有不跟onRequestPermissionsResult方法中的其他请求码冲突即可。
        return 1000;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.READ_CONTACTS
        };
    }

    @Override
    public void requestPermissionsSuccess() {
        initcontacts(); // 初始化水果数据
        adapter = new ContactsAdapter(LocConActivity.this, R.layout.contact_item, contactList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MyContacts contact = contactList.get(position);
                String path= FileStorageHelper.getStoragePath(LocConActivity.this,false);
                File file1 = new File(path+"/conW/"+contact.getName()+contact.getNum());
                if (!file1.exists())file1.mkdir();
                try {
                    save(contact.getImageBitmap(),file1+"/image.png", Bitmap.CompressFormat.PNG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LocConActivity.this, contact.getName(), Toast.LENGTH_SHORT).show();
                finish();
                SaveContactLR saveContactLR;
                Intent intent = new Intent("77777");
                intent.putExtra("name",contact.getName());
                intent.putExtra("dir",file1.toString());
                intent.putExtra("image",file1+"/image.png");
                localBroadcastManager.sendBroadcast(intent); // 发送本地广播
            }
        });
    }

    private void initcontacts() {


        Cursor cursor = null;
        try {
            // 查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String num=number.replace(" ", "");
                    Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                    MyContacts apple = new MyContacts(displayName, num, getPhoto(this, contactId));
                    contactList.add(apple);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private synchronized Bitmap getPhoto(Context c, Long raw_contact_id) {
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
        Cursor dataCursor = c.getContentResolver().query(dataUri,
                //获取 当在mimetype_id的数据为vnd.android.cursor.item/photo与
                // raw_contact_id数据为raw_contact_id的列
                // 里面的data15有数据的数据
                new String[]{"data15"},
                //       "mimetype='vnd.android.cursor.item/photo' AND raw_contact_id like '%"+raw_contact_id +"%'",
                "mimetype='vnd.android.cursor.item/photo' AND raw_contact_id=" + raw_contact_id,
                null, null);
        Bitmap bitmap = null;
        while (dataCursor.moveToNext()) {
            byte[] str = dataCursor.getBlob(0);
            if (str != null) {
                InputStream inputStream = new ByteArrayInputStream(str);
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            continue;
        }
        if (bitmap==null){
            bitmap=BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_cab_done_holo);
        }
        return bitmap;
    }
    @Override
    public void requestPermissionsFail() {
        Toast.makeText(LocConActivity.this, "没权限", Toast.LENGTH_SHORT).show();
    }



    /**
     * 保存图片
     *
     * @param src 源图片
     * @param filePath 要保存到的文件路径
     * @param format 格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format) throws IOException {
        return save(src, (new File(filePath)), format);
    }

    /**
     * 保存图片
     *
     * @param src 源图片
     * @param file 要保存到的文件
     * @param format 格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format) throws IOException {
        return save(src, file, format, false);
    }

    /**
     * 保存图片
     *
     * @param src 源图片
     * @param filePath 要保存到的文件路径
     * @param format 格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format, boolean recycle) throws IOException {
        return save(src, (new File(filePath)), format, recycle);
    }

    /**
     * 保存图片
     *
     * @param src 源图片
     * @param file 要保存到的文件
     * @param format 格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) throws IOException {
        if (isEmptyBitmap(src) || file.exists()) return false;
        System.out.println(src.getWidth() + ", " + src.getHeight());
        OutputStream os = null;
        boolean res = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            res = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            os.close();
        }
        return res;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
