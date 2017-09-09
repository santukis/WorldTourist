package com.david.worldtourist.aritems.presentation.components;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.view.model.MarkerOptions;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemType;

public class ArClusterRenderer implements MarkerRenderer<AItem> {


    private Context context;

    public ArClusterRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onBeforeItemMarkerRenderer(AItem arItem, MarkerOptions markerOptions) {
        Item item = arItem.getItem();

        markerOptions.withIcon(getDrawable(item.getType().getIcon()));
    }

    @Override
    public void onBeforeClusterMarkerRenderer(Cluster<AItem> cluster, MarkerOptions markerOptions) {
        ItemType itemType = getBestRepresentedType(cluster);

        markerOptions.withIcon(getDrawable(itemType.getArIcon())).withTitle(cluster.getSize() + " items");
    }

    private ItemType getBestRepresentedType(Cluster<AItem> cluster) {

        int culture = 0, nature = 0, entertainment = 0, gastronomy = 0;

        for (AItem aItem : cluster.getItems()) {

            switch (aItem.getItem().getType()) {
                case CULTURE:
                    ++culture;
                    break;

                case NATURE:
                    ++nature;
                    break;

                case ENTERTAINMENT:
                    ++entertainment;
                    break;

                case GASTRONOMY:
                    ++gastronomy;
                    break;
            }
        }

        if (culture >= nature && culture >= entertainment && culture >= gastronomy) {
            return ItemType.CULTURE;

        } else if (nature >= entertainment && nature >= gastronomy) {
            return ItemType.NATURE;

        } else if (entertainment >= gastronomy) {
            return ItemType.ENTERTAINMENT;

        } else {
            return ItemType.GASTRONOMY;
        }
    }

    private Drawable getDrawable(int icon) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return VectorDrawableCompat.create(context.getResources(), icon, null);

        } else {
            Bitmap bitmap = getBitmap(ContextCompat.getDrawable(context, icon));
            return new BitmapDrawable(context.getResources(), bitmap);
        }
    }

    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
