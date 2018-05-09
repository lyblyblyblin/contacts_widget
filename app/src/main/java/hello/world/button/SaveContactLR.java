package hello.world.button;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

import hello.world.button.Utils.SPUtils;

import static hello.world.button.Data.FruitAdapter.onLocalSucc;
import static hello.world.button.View.AppWidget.myOnUpdate;

public class SaveContactLR extends BroadcastReceiver {

    //选择本地联系人时候的本地广播LocalReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        String name=intent.getStringExtra("name");
        String image=intent.getStringExtra("image");
        String dir=intent.getStringExtra("dir");
        onLocalSucc(name, dir,image);
    }
}

        //本来计划是使用Alarm定时器用本地广播刷新的
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        SPUtils IdNum = new SPUtils("IdNum");
//        Set<String> idNumList = IdNum.getAll(context).keySet();
//        int[] intIdNumList =new int[idNumList.size()];
//        for (int i = 0; i < idNumList.size(); i++) {
//            intIdNumList[i]=(Integer.valueOf(
//                    (idNumList.toArray()[i]).toString()
//            ));
//        }
//
//        myOnUpdate(context, appWidgetManager, intIdNumList);
