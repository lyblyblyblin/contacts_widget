package hello.world.button;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import static hello.world.button.FruitAdapter.InPic;

public class CpicActivity extends ConWActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conw_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getIntent().getStringExtra("name")+"的图片");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        initFruits(1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FruitAdapter(fruitList, InPic);
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
        Toast.makeText(CpicActivity.this, "添加或删除图片", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

}
