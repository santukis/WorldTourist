package com.david.worldtourist.itemsmap.presentation.gmaps;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemType;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class GItemRenderer extends DefaultClusterRenderer<GItem> {

    private Context context;

    public GItemRenderer(Context context, GoogleMap map, ClusterManager<GItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onBeforeClusterItemRendered(GItem gItem, MarkerOptions markerOptions) {
        Item item = gItem.getItem();

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(item.getType().getIcon())))
                .title(item.getName())
                .snippet(item.getType().toString());

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<GItem> cluster, MarkerOptions markerOptions) {

        ItemType itemType = getBestRepresentedType(cluster);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(itemType.getArIcon())));

    }

    private ItemType getBestRepresentedType(Cluster<GItem> cluster) {

        int culture = 0, nature = 0, entertainment = 0, gastronomy = 0;

        for(GItem gItem : cluster.getItems()) {

            switch (gItem.getItem().getType()) {
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

        if(culture >= nature && culture >= entertainment && culture >= gastronomy) {
            return ItemType.CULTURE;

        } else if (nature >= entertainment && nature >= gastronomy) {
            return ItemType.NATURE;

        } else if (entertainment >= gastronomy) {
            return ItemType.ENTERTAINMENT;

        } else {
            return ItemType.GASTRONOMY;
        }

    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 1;
    }

    private Bitmap getBitmap(int icon) {
        Drawable drawable = ContextCompat.getDrawable(context, icon);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
