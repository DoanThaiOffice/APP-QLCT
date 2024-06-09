package com.hynguyen.chitieucanhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hynguyen.chitieucanhan.R;

import java.util.ArrayList;
import java.util.List;

public class ChonThangAdapter extends BaseAdapter {

    private List<String> listMonth;
    private LayoutInflater inflater;
    private Context context;


    public ChonThangAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        listMonth = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            listMonth.add(String.valueOf(i));
        }
        listMonth.add("Cả năm");
    }

    @Override
    public int getCount() {
        return listMonth.size();
    }

    @Override
    public Object getItem(int position) {
        return listMonth.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_month, null);
            holder = new ViewHolder();
            holder.txtMonth = convertView.findViewById(R.id.txtMonthNumber);
            holder.txtTitleMonth = convertView.findViewById(R.id.txtTitleMonth);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position<12) {
            holder.txtMonth.setText(listMonth.get(position));
        } else {
            holder.txtMonth.setText(listMonth.get(position));
            holder.txtTitleMonth.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView txtMonth;
        TextView txtTitleMonth;
    }
}
