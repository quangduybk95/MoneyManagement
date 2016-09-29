package com.btloop.moneymanage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btloop.moneymanage.R;
import com.btloop.moneymanage.entity.Event;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by thede on 4/17/2016.
 */
public class ListEventAdapter extends ArrayAdapter{
    LayoutInflater mInflater ;
    public ListEventAdapter(Context context, List objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        //position là vị tri của mỗi item. nó sẽ chạy hết mảng
        //lấy layout cho từng item
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater
                    .inflate(R.layout.eventitem, viewGroup, false);
            holder = new ViewHolder();
            holder.position = (TextView) convertView.findViewById(R.id.position);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Event item = (Event) getItem(position);
        holder.position.setText(String.valueOf(position+1));
        holder.name.setText(item.getEventName());
        String s = NumberFormat.getIntegerInstance().format(item.getMoney());
        holder.money.setText(s+" VNĐ");

        return convertView;
    }
    public static class ViewHolder {
        TextView position;
        TextView name;
        TextView money;
    }

}

