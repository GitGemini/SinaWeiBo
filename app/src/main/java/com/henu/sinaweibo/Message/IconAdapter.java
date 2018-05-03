package com.henu.sinaweibo.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henu.sinaweibo.R;

import java.util.List;

/**
 * Created by L on 2017/5/13.
 */

public class IconAdapter extends ArrayAdapter<MessageIcon> {
    private int resourceID;

    public IconAdapter(Context context, int textViewResourceId, List<MessageIcon> iconList) {
        super(context,textViewResourceId,iconList);
        this.resourceID = textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup viewGroup)
    {
        MessageIcon messageIcon = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceID,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.imageView);
            viewHolder.textView = (TextView)view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        }else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();//重新获取viewHolder
        }
        viewHolder.imageView.setImageResource(messageIcon.getImageID());
        viewHolder.textView.setText(messageIcon.getName());
        return view;
    }


}
class ViewHolder {
    ImageView imageView;
    TextView textView;
}

