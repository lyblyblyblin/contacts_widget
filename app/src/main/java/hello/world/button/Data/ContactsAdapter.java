package hello.world.button.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hello.world.button.R;

public class ContactsAdapter extends ArrayAdapter<MyContacts> {

    private int resourceId;

    public ContactsAdapter(Context context, int textViewResourceId,
                           List<MyContacts> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyContacts contact = getItem(position); // 获取当前项的contact实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contactImage = (ImageView) view.findViewById (R.id.contact_image);
            viewHolder.contactName = (TextView) view.findViewById (R.id.contact_name);
            viewHolder.contactNum = (TextView) view.findViewById (R.id.contact_num);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.contactNum.setText(contact.getNum());
        viewHolder.contactImage.setImageBitmap(contact.getImageBitmap());
        viewHolder.contactName.setText(contact.getName());
        return view;
    }

    class ViewHolder {

        ImageView contactImage;

        TextView contactName;
        TextView contactNum;
    }

}
