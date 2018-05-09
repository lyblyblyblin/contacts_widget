package hello.world.button.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import hello.world.button.R;
import hello.world.button.Utils.FileStorageHelper;

public class SecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView)findViewById(R.id.textview);
        textView.setText("         可以手动在sd卡或本机的存储下的conW文件夹中添加文件夹（如conW/" +
                "小明13888888888），并放入图片，软件将自动压缩，并改后缀名，也可以使用本软件来进行设置");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //获取内置存储位置
        String path=FileStorageHelper.getStoragePath(SecActivity.this,false);
        File file1 = new File(path+"/conW");
        //创建失败，应该是运行过
        if (file1.mkdirs()){
            //下面代码是将raw中的文件复制到/conW下
            FileStorageHelper.copyFilesFromRaw(this,R.raw.ithome,
                    ".ithome.jpg",file1.toString()+"/小明13888888888");
            FileStorageHelper.copyFilesFromRaw(this,R.raw.baidu,
                    ".baidu.jpg",file1.toString()+"/小明13888888888");
        }
        //todo 判断文件名是不是隐藏文件名 不是的话，将图片进行压缩
        fab.setImageResource(R.drawable.ic_cab_done_holo_light);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent=new Intent(SecActivity.this,ConWActivity.class);
                startActivity(intent);
            }
        });
    }

}
