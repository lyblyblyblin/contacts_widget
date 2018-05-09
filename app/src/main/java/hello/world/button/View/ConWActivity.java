package hello.world.button.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.io.File;

import hello.world.button.Data.DirList;
import hello.world.button.Data.Fruit;
import hello.world.button.Data.FruitAdapter;
import hello.world.button.R;
import hello.world.button.Utils.FileStorageHelper;

import static hello.world.button.Data.FruitAdapter.InActivity;

public class ConWActivity extends AppCompatActivity implements FruitAdapter.OnSuccessListener {
    protected FruitAdapter adapter;
    protected RecyclerView recyclerView;
    protected StaggeredGridLayoutManager layoutManager;
    protected  boolean indelete = false;
    protected Fruit addfruit;
    DirList dirList;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.conw_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(ConWActivity.this, SecActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    String TAG = "TAG";

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
//        dirList.initFruits(ConWActivity.this);
//        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        dirList = new DirList();
        dirList.initFruits(ConWActivity.this);
        adapter = new FruitAdapter(dirList.getFruitList(), InActivity);
        layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnDelListener(this);
        adapter.setOnUpdateListener(this);
        adapter.setOnUpdateAllListener(this);
        addfruit = new Fruit("add", R.drawable.ic_menu_cc_am, true, new File(""));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item=item;
        indelete = !indelete;
        for (int i = 0; i < dirList.getFruitList().size(); i++) {
            dirList.getFruitList().get(i).setInDelete(indelete);
        }
        if (indelete == true) {
            Toast.makeText(ConWActivity.this, "添加或删除对应联系人", Toast.LENGTH_SHORT).show();
            addfruit.setInDelete(true);
            dirList.getFruitList().add(0, addfruit);
            item.setIcon(R.drawable.ic_cab_done_holo_light);
        } else {
            dirList.getFruitList().remove(0);
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


    @Override
    public void onAdapterSuccess(String Name) {

    }


    @Override
    public void onAdapterDel(final int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ConWActivity.this);
        normalDialog.setTitle("真的删除？");
        normalDialog.setMessage("删除   " + dirList.getFruitList().get(position).getName());
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if (FileStorageHelper.deleteDirectory(dirList.getFruitList().get(position).getDir().toString())) {
                            //todo 数据库删除内容
                            //删除图片文件夹
                            dirList.getFruitList().remove(position);
                            adapter.notifyItemRemoved(position);
                            return;
                        }
                        Toast.makeText(ConWActivity.this, "撞邪了，没法删除图片", Toast.LENGTH_LONG).show();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        return;
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    public void onFalseIndelete() {
        indelete=false;
        if (item!=null){
            item.setIcon(R.drawable.ic_menu_edit);
        }
    }

    @Override
    public void onAdapterUpdate(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onAdapterUpdateAll() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSelPic() {

    }


}
