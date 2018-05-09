package hello.world.button;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hello.world.button.Utils.PermissionHelper;
import hello.world.button.Utils.PermissionInterface;
import hello.world.button.View.SecActivity;

public class MainActivity extends AppCompatActivity implements PermissionInterface {
    private PermissionHelper mPermissionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //初始化并发起权限申请
                mPermissionHelper = new PermissionHelper(MainActivity.this, MainActivity.this);
                mPermissionHelper.requestPermissions();

            }
        });

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
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    public void requestPermissionsSuccess() {
        finish();
        Intent intent = new Intent(MainActivity.this, SecActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestPermissionsFail() {
        Toast.makeText(MainActivity.this,"没权限",Toast.LENGTH_SHORT).show();
        finish();
    }
}
