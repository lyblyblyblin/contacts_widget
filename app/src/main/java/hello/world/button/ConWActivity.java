package hello.world.button;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static hello.world.button.FruitAdapter.InActivity;

public class ConWActivity extends AppCompatActivity implements FruitAdapter.OnSuccessListener {
    protected List<Fruit> fruitList = new ArrayList<Fruit>();
    protected FruitAdapter adapter;
    protected RecyclerView recyclerView;
    protected StaggeredGridLayoutManager layoutManager;
    protected boolean indelete;
    protected Fruit addfruit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conw_activity);
        indelete = false;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ConWActivity.this, SecActivity.class);
                startActivity(intent);
            }
        });
        initFruits(1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FruitAdapter(fruitList, InActivity);
        layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnDelListener(this);
        adapter.setOnUpdateListener(this);
        addfruit=new Fruit("add",R.drawable.ic_menu_cc_am,false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        indelete = !indelete;
        for (int i = 0; i < fruitList.size(); i++) {
            fruitList.get(i).setInDelete(indelete);
        }
        if (indelete==true){
            fruitList.add(0,addfruit);
        }else {
            fruitList.remove(addfruit);
        }

        adapter.notifyDataSetChanged();
        Log.d("abc", "onOptionsItemSelected: " + fruitList.size());
        Toast.makeText(ConWActivity.this, "添加或删除对应联系人", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    protected void initFruits(int num) {
        for (int i = 0; i < num; i++) {
            Fruit apple = new Fruit(getRandomLengthName("林"), R.drawable.apple_pic, false);
            fruitList.add(apple);
            Fruit banana = new Fruit(getRandomLengthName("陈"), R.drawable.banana_pic, false);
            fruitList.add(banana);
            Fruit orange = new Fruit(getRandomLengthName("利"), R.drawable.orange_pic, false);
            fruitList.add(orange);
            Fruit watermelon = new Fruit(getRandomLengthName("李"), R.drawable.watermelon_pic, false);
            fruitList.add(watermelon);
            Fruit pear = new Fruit(getRandomLengthName("P"), R.drawable.example_appwidget_preview, false);
            fruitList.add(pear);
            Fruit grape = new Fruit(getRandomLengthName("G"), R.drawable.grape_pic, false);
            fruitList.add(grape);
            Fruit pineapple = new Fruit(getRandomLengthName("朱"), R.drawable.pineapple_pic, false);
            fruitList.add(pineapple);
            Fruit strawberry = new Fruit(getRandomLengthName("哈"), R.drawable.strawberry_pic, false);
            fruitList.add(strawberry);
            Fruit cherry = new Fruit(getRandomLengthName("默"), R.drawable.cherry_pic, false);
            fruitList.add(cherry);
            Fruit mango = new Fruit(getRandomLengthName("语文"), R.drawable.watermelon_pic, false);
            fruitList.add(mango);
        }
    }

    private String getRandomLengthName(String name) {
        return name;
    }

    @Override
    public void onAdapterSuccess(String Name) {

    }

    @Override
    public void onAdapterDel(int position) {
        fruitList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onAdapterUpdate(int position) {
        adapter.notifyItemRemoved(position);
    }
}
