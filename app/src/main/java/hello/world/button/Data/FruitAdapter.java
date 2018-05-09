package hello.world.button.Data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import hello.world.button.OnSettingListener;
import hello.world.button.R;
import hello.world.button.SaveContactLR;
import hello.world.button.Utils.FileStorageHelper;
import hello.world.button.Utils.MyDialog;
import hello.world.button.View.CPicActivity;
import hello.world.button.View.ConWActivity;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> implements OnSettingListener {

    private static List<Fruit> mFruitList;
    private Context mContext;
    private int inWhere;
    public static final int InActivity = 0;
    public static final int InPic = 1;
    public static final int InWidget = 2;

    //添加空联系人成功执行回调
    @Override
    public void onSetSucc(String name, String num) {
        //获取内置存储位置
        String path = FileStorageHelper.getStoragePath(mContext, false);
        File file1 = new File(path + "/conW/" + name + num);
        file1.mkdir();
        mFruitList.add(1, new Fruit(name, R.drawable.ic_menu_cc_am, true, file1));
        onSuccessListener.onAdapterUpdateAll();
    }

    //添加本地联系人成功执行回调
    public static void onLocalSucc(String name, String dir, String image) {
        File file = new File(image);
        mFruitList.add(1, new Fruit(name, file, true, new File(dir)));
        onSuccessListener.onFalseIndelete();
        myDialog.closeDialog();
        localBroadcastManager.unregisterReceiver(saveContactLR);
        //onSuccessListener.onAdapterUpdateAll();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        ImageView fruitImage;
        ImageView deleteImg;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
            deleteImg = (ImageView) view.findViewById(R.id.delete);
        }
    }

    public FruitAdapter(List<Fruit> fruitList, int inWhere) {
        this.inWhere = inWhere;
        mFruitList = fruitList;

    }

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private static SaveContactLR saveContactLR;
    private static LocalBroadcastManager localBroadcastManager;
    private ViewHolder viewHolder;
    static MyDialog myDialog;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != -1 && position < mFruitList.size()) {
                    Fruit fruit = mFruitList.get(position);
                    switch (inWhere) {
                        case InActivity:
                            //todo 长按调整电话或名字
                            if (!mFruitList.get(0).getinDelete()) {
                                Intent intent = new Intent(mContext, CPicActivity.class);
                                intent.putExtra("name", fruit.getName());
                                intent.putExtra("path", fruit.getDir().toString());
                                mContext.startActivity(intent);

                            } else {
                                if (position == 0) {
                                    //选择本地联系人监听器
                                    intentFilter = new IntentFilter();
                                    intentFilter.addAction("77777");
                                    saveContactLR = new SaveContactLR();
                                    localBroadcastManager = LocalBroadcastManager.getInstance(mContext); // 获取实例
                                    localBroadcastManager.registerReceiver(saveContactLR, intentFilter); // 注册本地广播监听器
                                    myDialog = MyDialog.getInstance();
                                    myDialog.onSetSuccListener(FruitAdapter.this);
                                    myDialog.showCustomizeDialog(mContext);
                                } else {
                                    onSuccessListener.onAdapterDel(position);
                                }
                            }
                            break;
                        case InPic:
                            if (!mFruitList.get(0).getinDelete()) {
                            } else {
                                if (position == 0) {
                                    viewHolder = holder;
                                    localBroadcastManager = LocalBroadcastManager.getInstance(mContext); // 获取实例
                                    intentFilter = new IntentFilter();
                                    intentFilter.addAction("pic_path");
                                    localReceiver = new LocalReceiver();
                                    localBroadcastManager.registerReceiver(localReceiver, intentFilter); // 注册本地广播监听器
                                    //选择图片
                                    onSuccessListener.onSelPic();
                                } else {

                                    onSuccessListener.onAdapterDel(position);
                                }
                            }
                            break;
                        case InWidget:
                            //配置桌面插件时候传递
                            String dir = fruit.getDir().toString();
                            onSuccessListener.onAdapterSuccess(dir);


                            break;
                        default:
                            break;
                    }
                }
            }
        });
        return holder;
    }

    //不同界面的界面设置
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        switch (inWhere) {
            case InActivity:
                if (fruit.getName().length() != 1) {
                    holder.fruitName.setTextSize(40);
                }
                if (fruit.getinDelete() && position != 0) {
                    holder.deleteImg.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteImg.setVisibility(View.GONE);
                }
                holder.fruitName.setText(fruit.getName());
                if (fruit.getImageId() != 0) {
                    Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
                } else {
                    Glide.with(mContext).load(fruit.getImageFile()).into(holder.fruitImage);
                }
                break;
            case InPic:
                if (fruit.getinDelete() && position != 0) {
                    holder.deleteImg.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteImg.setVisibility(View.GONE);
                }
                if (fruit.getImageId() != 0) {
                    Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
                } else {
                    Glide.with(mContext).load(fruit.getImageFile()).into(holder.fruitImage);
                }
                //todo 已经IdPath里面的Id大于0x显示灰色，表示已经创建过
                break;
            case InWidget:
                if (fruit.getName().length() != 1) {
                    holder.fruitName.setTextSize(40);
                }
                holder.fruitName.setText(fruit.getName());
                Glide.with(mContext).load(fruit.getImageFile()).into(holder.fruitImage);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }


    //等待用户选择图片后发送本地广播添加图片
    //添加图片监听器
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            localBroadcastManager.unregisterReceiver(localReceiver);
            File file = new File(intent.getStringExtra("path"));
            mFruitList.add(1, new Fruit(null, file, true, file));
            viewHolder.deleteImg.setVisibility(View.GONE);
            onSuccessListener.onAdapterUpdateAll();
        }

    }


    //创建接口，成功时候回调
    private static OnSuccessListener onSuccessListener;

    public interface OnSuccessListener {
        void onAdapterSuccess(String FileDir);

        void onAdapterDel(int position);

        void onFalseIndelete();

        void onAdapterUpdate(int position);

        void onAdapterUpdateAll();

        void onSelPic();

    }

    public void setOnFalseIndelete(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnDelListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnUpdateListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnUpdateAllListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnSelPic(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

}