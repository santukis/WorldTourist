package com.david.worldtourist.useritems.presentation.adapter;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.presentation.component.click.ItemClick;
import com.david.worldtourist.items.presentation.component.click.EmptyClickAction;
import com.david.worldtourist.items.presentation.component.click.ItemLongClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserItemsAdapter extends RecyclerView.Adapter<UserItemsAdapter.ViewHolder> {


    private Context context;
    private List<Item> items = new ArrayList<>();
    private ItemClick itemClick = new EmptyClickAction();
    private ItemLongClick itemLongClick = new EmptyClickAction();


    public UserItemsAdapter(Context context) {
        this.context = context;
    }

    public void setClickAction(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setLongClickAction(ItemLongClick longClick) {
        itemLongClick = longClick;
    }

    /////////////////////////RecyclerView.Adapter<> Implementation//////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.element_user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item item = items.get(position);

        setBackgroundColor(holder);

        showImageIfAvailable(holder, item);

        showName(holder, item);

        showDescription(holder, item);

        showIcon(holder, item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(item);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setBackgroundColor(holder.itemView);
                itemLongClick.onItemLongClick(item.getId());
                return true;
            }
        });
    }

    private void showIcon(ViewHolder holder, Item item) {
        switch (item.getType()) {
            case CULTURE:
                holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(
                        context, R.drawable.ic_culture));

                break;
            case NATURE:
                holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(
                        context, R.drawable.ic_nature));

                break;
            case ENTERTAINMENT:
                holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(
                        context, R.drawable.ic_entertainment));

                break;
            case GASTRONOMY:
                holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(
                        context, R.drawable.ic_gastronomy));
        }
    }

    private void showDescription(ViewHolder holder, Item item) {
        holder.itemDescription.setText(item.getDescription());
    }

    private void showName(ViewHolder holder, Item item) {
        holder.itemName.setText(item.getName());
    }

    private void showImageIfAvailable(ViewHolder holder, Item item) {
        if(!item.getPhotos().isEmpty()) {
            Glide.with(context)
                    .load(item.getPhotos().get(0).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.itemImage);

            holder.itemImage.setAdjustViewBounds(true);
        }
    }

    private void setBackgroundColor(ViewHolder holder) {
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context,
                android.R.color.white));
    }

    private void setBackgroundColor(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context,
                getBackgroundColor(view.getBackground())));
    }

    private int getBackgroundColor(Drawable background) {
        ColorDrawable colorDrawable = (ColorDrawable) background;

        if(colorDrawable.getColor() ==
                ContextCompat.getColor(context, android.R.color.white)) {
            return R.color.colorRadar;

        } else {
            return android.R.color.white;
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public void replaceData(List<Item> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    public void clearMemory() {
        Glide.get(context).clearMemory();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image) ImageView itemImage;
        @BindView(R.id.item_icon) ImageView itemIcon;
        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.item_description) TextView itemDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}