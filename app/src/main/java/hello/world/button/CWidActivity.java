package hello.world.button;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CWidActivity extends AppCompatActivity implements FruitAdapter.OnSuccessListener {
    int appWidgetId;
    private List<Fruit> fruitList = new ArrayList<Fruit>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cwid);
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FruitAdapter adapter = new FruitAdapter(fruitList,FruitAdapter.InWidget);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        adapter.setOnSuccessListener(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    private void initFruits() {
        for (int i = 0; i < 1; i++) {
            Fruit apple = new Fruit(getRandomLengthName("林"), R.drawable.apple_pic,false);
            fruitList.add(apple);
            Fruit banana = new Fruit(getRandomLengthName("陈"), R.drawable.banana_pic,false);
            fruitList.add(banana);
            Fruit orange = new Fruit(getRandomLengthName("利"), R.drawable.orange_pic,false);
            fruitList.add(orange);
            Fruit watermelon = new Fruit(getRandomLengthName("李"), R.drawable.watermelon_pic,false);
            fruitList.add(watermelon);
            Fruit pear = new Fruit(getRandomLengthName("P"), R.drawable.apple_pic,false);
            fruitList.add(pear);
            Fruit grape = new Fruit(getRandomLengthName("G"), R.drawable.grape_pic,false);
            fruitList.add(grape);
            Fruit pineapple = new Fruit(getRandomLengthName("朱"), R.drawable.pineapple_pic,false);
            fruitList.add(pineapple);
            Fruit strawberry = new Fruit(getRandomLengthName("哈"), R.drawable.strawberry_pic,false);
            fruitList.add(strawberry);
            Fruit cherry = new Fruit(getRandomLengthName("默"), R.drawable.cherry_pic,false);
            fruitList.add(cherry);
            Fruit mango = new Fruit(getRandomLengthName("语文"), R.drawable.cherry_pic,false);
            fruitList.add(mango);
        }
    }

    private String getRandomLengthName(String name) {
        return name;
    }

    @Override
    public void onAdapterSuccess(String Name) {
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

            //TO DO, Perform the configuration and get an instance of the AppWidgetManager//

            //配置完 Widget 配置,拿到AppWidgetManager实例
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CWidActivity.this);
            SPUtils.put(CWidActivity.this, ""+appWidgetId, Name);
            //通过调用updateAppWidget（int,RemoteViews）通过RemoteViews布局更新App Widget
            AppWidget.updateAppWidget(CWidActivity.this, appWidgetManager, appWidgetId);

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
    public void onAdapterUpdate(int position) {

    }
}
