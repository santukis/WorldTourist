package com.david.worldtourist.items.presentation.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.ItemDate;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.presentation.component.click.ItemClick;
import com.david.worldtourist.items.presentation.component.click.EmptyClickAction;
import com.david.worldtourist.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {


    private Context context;
    private List<Item> items = new ArrayList<>();
    private ItemClick itemClick = new EmptyClickAction();
    private GeoCoordinate userLocation = GeoCoordinate.EMPTY_COORDINATE;

    public ItemsAdapter(Context context) {
        this.context = context;
    }

    public void setClickAction(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    /////////////////////////RecyclerView.Adapter<> Implementation//////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.element_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item item = items.get(position);

        showName(holder, item);

        showImageIfAvailable(holder, item);

        showCityIfAvailable(holder, item);

        showDistance(holder, item);

        hideDateRowIfEmpty(holder, item);

        showDateIfAvailable(holder, item);

        showIcon(holder, item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(item);
            }
        });
    }

    private void showName(ViewHolder holder, Item item) {
        holder.itemName.setText(item.getName());
    }

    private void showImageIfAvailable(ViewHolder holder, Item item) {
        if (!item.getPhotos().isEmpty()) {
            Glide.with(context)
                    .load(item.getPhotos().get(0).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.no_image);
        }

        holder.itemImage.setAdjustViewBounds(true);
    }

    private void showCityIfAvailable(ViewHolder holder, Item item) {
        if(item.getCity().isEmpty()) {
            holder.itemCity.setVisibility(View.GONE);

        } else {
            holder.itemCity.setText(item.getCity());

            holder.itemCity.setCompoundDrawables(setDrawableColor(
                    holder.itemCity.getCompoundDrawables()[0], android.R.color.darker_gray),
                    null, null, null);
        }
    }

    private void hideDateRowIfEmpty(ViewHolder holder, Item item) {
        if(item.getStartDate() == ItemDate.EMPTY_DATE) {
            holder.dateRow.setVisibility(View.GONE);
        }
    }

    private void showDateIfAvailable(ViewHolder holder, Item item) {
        final String HOURS = " h";

        if (item.getStartDate() == ItemDate.EMPTY_DATE) {
            holder.itemDate.setVisibility(View.INVISIBLE);
            holder.itemTime.setVisibility(View.INVISIBLE);

        } else {
            holder.itemDate.setText(StringUtils.getConvertedDate(item.getStartDate().getDate()));
            holder.itemTime.setText(StringUtils.getFormattedDate(item.getStartDate().getTime(),
                    StringUtils.HOUR_MINUTES).concat(HOURS));

            holder.itemDate.setCompoundDrawables(setDrawableColor(
                    holder.itemDate.getCompoundDrawables()[0], android.R.color.darker_gray),
                    null, null, null);
            holder.itemTime.setCompoundDrawables(setDrawableColor(
                    holder.itemTime.getCompoundDrawables()[0], android.R.color.darker_gray),
                    null, null, null);
        }
    }

    private void showDistance(ViewHolder holder, Item item) {
        holder.itemDistance.setText(StringUtils.getFormattedDistance(
                GeoCoordinate.getDistance(userLocation, item.getCoordinate())));
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
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public void replaceData(List<Item> updatedList, GeoCoordinate userPosition) {
        userLocation = userPosition;
        items.clear();
        items.addAll(updatedList);
        notifyDataSetChanged();
    }

    private Drawable setDrawableColor(Drawable iconDrawable, int colorResource) {
        iconDrawable.setColorFilter(ContextCompat.getColor(context, colorResource),
                PorterDuff.Mode.SRC_IN);
        return iconDrawable;
    }

/////////////////////////////////////////////////////////////////////////////////////////

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_row) TableRow dateRow;
        @BindView(R.id.item_image) ImageView itemImage;
        @BindView(R.id.item_icon) ImageView itemIcon;
        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.item_city) TextView itemCity;
        @BindView(R.id.item_distance) TextView itemDistance;
        @BindView(R.id.item_date) TextView itemDate;
        @BindView(R.id.item_time) TextView itemTime;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}