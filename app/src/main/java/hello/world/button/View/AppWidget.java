package hello.world.button.View;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;

import hello.world.button.SaveContactLR;
import hello.world.button.R;
import hello.world.button.Utils.FileStorageHelper;
import hello.world.button.Utils.SPUtils;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    private static final String CLICK_ACTION = "Click.conw";
    protected static List<File> fileList;
    private static AppWidgetTarget appWidgetTarget;
    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        updateAppWidgetPic(context,views,appWidgetId);

        //获取IdNum里面的数据，来设置拨号信息
        SPUtils IdNum = new SPUtils("IdNum");
        String num = IdNum.get(context, "" + appWidgetId, "").toString();
        views.setOnClickPendingIntent(R.id.appwidget_imageview,
                getPendingSelfIntent(context, CLICK_ACTION,
                        num
                ));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgetInfo(Context context, AppWidgetManager appWidgetManager,
                                           int appWidgetId) {
        views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        updateAppWidgetPic(context,views,appWidgetId);

        SPUtils IdNum = new SPUtils("IdNum");
        String num = IdNum.get(context, "" + appWidgetId, "").toString();
        views.setOnClickPendingIntent(R.id.appwidget_imageview,
                getPendingSelfIntent(context, CLICK_ACTION,
                        num
                ));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void updateAppWidgetPic(Context context, RemoteViews views, int appWidgetId) {
        appWidgetTarget = new AppWidgetTarget(context, R.id.appwidget_imageview, views, appWidgetId);
        Random random = new Random();
        SPUtils IdPath = new SPUtils("IdPath");
        String dirPath = IdPath.get(context, "" + appWidgetId, "").toString();
        if (dirPath == null || dirPath.equals("")) return;
        File file = new File(dirPath);
        fileList = FileStorageHelper.showDirFile(file.toString());
        if ((!file.exists()) || file.toString().equals("")) {
        } else {
            if (fileList.size() == 0) {
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(R.drawable.ic_cab_done_holo_light)
                        .into(appWidgetTarget);
            } else {
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(fileList.get(random.nextInt(fileList.size())))
                        .apply(new RequestOptions().override(300, 400))
                        .into(appWidgetTarget);
            }
        }
    }

    public static void myOnUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SPUtils IdPath = new SPUtils("IdPath");
        SPUtils IdNum = new SPUtils("IdNum");
        for (int appWidgetId : appWidgetIds) {
            IdNum.remove(context, "" + appWidgetId);
            IdPath.remove(context, "" + appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private IntentFilter intentFilter;
    private SaveContactLR localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    //判断widget拨号
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (action.startsWith(CLICK_ACTION)) {
            String num = action.substring(CLICK_ACTION.length());
            Intent call_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
            call_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(call_intent);


            SPUtils IdNum = new SPUtils("IdNum");
            Set<String> idNumList = IdNum.getAll(context).keySet();
            int[] intIdNumList =new int[idNumList.size()];
            for (int i = 0; i < idNumList.size(); i++) {
                intIdNumList[i]=(Integer.valueOf(
                        (idNumList.toArray()[i]).toString()
                ));

            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            myOnUpdate(context, appWidgetManager, intIdNumList);
        }

    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action, String num) {
        Intent intent = new Intent(context, AppWidget.class);
        intent.setAction(CLICK_ACTION + num);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

