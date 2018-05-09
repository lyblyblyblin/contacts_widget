package hello.world.button.View;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.List;
import java.util.Map;

import hello.world.button.Data.DirList;
import hello.world.button.Data.FruitAdapter;
import hello.world.button.R;
import hello.world.button.Utils.SPUtils;

import static hello.world.button.Data.DirList.initFruits;

public class CWidActivity extends AppCompatActivity implements FruitAdapter.OnSuccessListener {
    int appWidgetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cwid);
        initFruits(CWidActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DirList dirList=new DirList();
        dirList.initFruits(CWidActivity.this);
        FruitAdapter adapter = new FruitAdapter(dirList.getFruitList(),FruitAdapter.InWidget);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnSuccessListener(this);
        //5秒内不点击退出
        //不想做接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CWidActivity.this.finish();
            }
        }).start();
    }


    String TAG="CWid";
    //RecyclerView里面内容被点击
    @Override
    public void onAdapterSuccess(String FileDir) {
        //给一个没用的appWidgetId
        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        //Retrieve the App Widget ID from the Intent that launched the Activity//
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


            // If the intent doesn’t have a widget ID, then call finish()//

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
            //写入电话信息
            //避免区号的情况 0750
            String phoneNum=FileDir.substring(FileDir.length()-11,FileDir.length());
            //                          key    value
            SPUtils IdPath=new SPUtils("IdPath");
            Map<String,?> map =IdPath.getAll(this);
            //查询到Id为负数时候删除再新建
            //正数时候新建
            List<String> findKeySet=IdPath.findValue(this,FileDir);
            if (findKeySet.size()==1 && Integer.valueOf(findKeySet.get(0))<1){
                IdPath.remove(this,Integer.valueOf(findKeySet.get(0)).toString());
            }
            IdPath.put(this,""+appWidgetId,FileDir);
            SPUtils IdNum=new SPUtils("IdNum");//id      phonenum
            IdNum.put(CWidActivity.this,""+appWidgetId,phoneNum);
            //TO DO, Perform the configuration and get an instance of the AppWidgetManager//

//            //配置完 Widget 配置,拿到AppWidgetManager实例
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CWidActivity.this);
//            //通过调用updateAppWidget（int,RemoteViews）通过RemoteViews布局更新App Widget
           AppWidget.updateAppWidgetInfo(CWidActivity.this, appWidgetManager, appWidgetId);
            //Create the return intent//
            Intent resultValue = new Intent();
            //Pass the original appWidgetId//
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //Set the results from the ‘Configure’ Activity//
            setResult(RESULT_OK, resultValue);

            //Finish the Activity//
            finish();


        }

    }

    @Override
    public void onAdapterDel(int position) {

    }

    @Override
    public void onFalseIndelete() {

    }

    @Override
    public void onAdapterUpdate(int position) {

    }

    @Override
    public void onAdapterUpdateAll() {

    }

    @Override
    public void onSelPic() {

    }

}
