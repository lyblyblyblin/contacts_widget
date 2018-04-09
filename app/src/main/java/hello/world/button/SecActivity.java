package hello.world.button;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView)findViewById(R.id.textview);
        textView.setText("         可以手动在sd卡或本机的存储下的conW文件夹中添加文件夹（如conW/李小龙13888888888），并放入图片，软件将自动压缩，也可以使用本软件");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
