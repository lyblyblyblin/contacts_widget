package hello.world.button;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> mFruitList;
    private Context mContext;
    private int inWhere;
    public static final int InActivity=0;
    public static final int InPic=1;
    public static final int InWidget=2;

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        this.inWhere=inWhere;
        mFruitList = fruitList;

    }

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
                Fruit fruit = mFruitList.get(position);
                switch (inWhere){
                    case InActivity:
                        if (!mFruitList.get(mFruitList.size()-1).getinDelete()  &&  mFruitList.size()!=1) {
                            Intent intent = new Intent(mContext, CpicActivity.class);
                            intent.putExtra("name", fruit.getName());
                            mContext.startActivity(intent);
                        } else {
                            if (position == 0) {
                                mFruitList.add(mFruitList.size(), new Fruit("abc", R.drawable.ic_cab_done_holo_light, true));
                                onSuccessListener.onAdapterUpdate(mFruitList.size() - 1);
                            } else {
                                onSuccessListener.onAdapterDel(position);
                            }
                        }
                        break;
                    case InPic:
                        if (!mFruitList.get(mFruitList.size()-1).getinDelete()  &&  mFruitList.size()!=1) {

                        } else {
                            if (position == 0) {
                                mFruitList.add(mFruitList.size(), new Fruit("abc", R.drawable.ic_cab_done_holo_light, true));
                                onSuccessListener.onAdapterUpdate(mFruitList.size() - 1);
                            } else {
                                onSuccessListener.onAdapterDel(position);
                            }
                        }
                        break;
                    case InWidget:
                        onSuccessListener.onAdapterSuccess(fruit.getName());
                        break;
                    default:
                        break;
                }
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        switch (inWhere){
            case InActivity:
                if (fruit.getName().length() != 1) {
                    holder.fruitName.setTextSize(40);
                }
                if (fruit.getinDelete()) {
                    holder.deleteImg.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteImg.setVisibility(View.GONE);
                }
                holder.fruitName.setText(fruit.getName());
                Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
                break;
            case InPic:
                if (fruit.getinDelete()) {
                    holder.deleteImg.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteImg.setVisibility(View.GONE);
                }
                Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
                break;
            case InWidget:
                if (fruit.getName().length() != 1) {
                    holder.fruitName.setTextSize(40);
                }
                holder.fruitName.setText(fruit.getName());
                Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    //创建接口，成功时候回调
    private OnSuccessListener onSuccessListener;

    public interface OnSuccessListener {
        void onAdapterSuccess(String Name);

        void onAdapterDel(int position);

        void onAdapterUpdate(int position);

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
}