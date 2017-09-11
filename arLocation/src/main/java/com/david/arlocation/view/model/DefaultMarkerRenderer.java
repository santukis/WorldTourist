package com.david.arlocation.view.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import com.david.arlocation.R;
import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.Cluster;


public class DefaultMarkerRenderer<T extends ArItem> implements MarkerRenderer<T> {

    private Drawable defaultIcon;


    public DefaultMarkerRenderer(Context context) {
        defaultIcon = getDrawable(context);
    }

    @Override
    public void onBeforeArItemMarkerRenderer(T item, MarkerOptions markerOptions) {
        markerOptions.withIcon(defaultIcon);
    }

    @Override
    public void onBeforeClusterMarkerRenderer(Cluster<T> cluster, MarkerOptions markerOptions) {
        markerOptions.withIcon(defaultIcon).withTitle(String.valueOf(cluster.getSize()));
    }

    private Drawable getDrawable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return VectorDrawableCompat.create(context.getResources(), R.drawable.default_marker, null);

        } else {
            Bitmap bitmap = getBitmap(ContextCompat.getDrawable(context, R.drawable.default_marker));
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
