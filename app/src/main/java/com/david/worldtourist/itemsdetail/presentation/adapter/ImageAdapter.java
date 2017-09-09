package com.david.worldtourist.itemsdetail.presentation.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.Photo;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends PagerAdapter {


    private Context context;
    private List<Photo> photos = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    ////////////////////////PageAdapter implementation//////////////////////////
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = layoutInflater.inflate(R.layout.element_image, container, false);

        ImageView image = (ImageView) itemView.findViewById(R.id.item_image);

        Glide.with(context)
                .load(photos.get(position).getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(image);

        ((ViewGroup) image.getParent()).removeView(image);

        container.addView(image);

        return image;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    public void insertData(List<Photo> photos){
        this.photos.clear();
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }
}