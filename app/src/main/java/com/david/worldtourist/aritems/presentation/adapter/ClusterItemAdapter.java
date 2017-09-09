package com.david.worldtourist.aritems.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.Item;

import java.util.ArrayList;
import java.util.List;


public class ClusterItemAdapter extends BaseAdapter {

    private Context context;
    private List<? extends ItemAdapter> items = new ArrayList<>();

    public ClusterItemAdapter(Context context, List<? extends ItemAdapter> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.element_cluster_item, null, true);
        Item item = ((ItemAdapter) getItem(position)).getItem();

        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        TextView name = (TextView) view.findViewById(R.id.item_name);

        icon.setImageResource(item.getType().getIcon());
        name.setText(item.getName());

        return view;
    }

    public interface ItemAdapter {

        Item getItem();

    }
}
