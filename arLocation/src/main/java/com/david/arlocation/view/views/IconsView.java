package com.david.arlocation.view.views;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.david.arlocation.R;
import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.CameraPosition;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.view.model.Marker;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.boundary.ArView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IconsView<T extends ArItem> extends View implements ArView<T> {

    private static final int ONE_KILOMETER = 1000;

    private Set<Marker<T>> markers = new HashSet<>();

    private OnClusterClickListener<T> onClusterClickListener;
    private OnArItemClickListener<T> onArItemClickListener;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public IconsView(Context context) {
        super(context);
        onClusterClickListener = new EmptyClickAction();
        onArItemClickListener = new EmptyClickAction();

        setupPaint();
    }

    public IconsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    @Override
    public void addMarkers(Set<? extends Marker<T>> markers) {
        this.markers.addAll(markers);
        invalidate();
    }

    @Override
    public void clearMarkers() {
        markers.clear();
        invalidate();
    }

    @Override
    public void drawMarkers() {
        invalidate();
    }

    @Override
    public void setMarkerRenderer(MarkerRenderer<T> markerRenderer) {

    }

    @Override
    public void setOnArItemClickListener(OnArItemClickListener<T> onArItemClickListener) {
        this.onArItemClickListener = onArItemClickListener;
    }

    @Override
    public void setOnClusterClickListener(OnClusterClickListener<T> onClusterClickListener) {
        this.onClusterClickListener = onClusterClickListener;
    }

    private void setupPaint() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(getResources().getDimension(R.dimen.icons_text_title));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (markers.isEmpty()) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            return;
        }

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        for(Marker<T> marker : markers) {
            CameraPosition markerPosition = marker.getPosition();

            if (markerPosition.getZ() < 0) {

                float x = (markerPosition.getX() / markerPosition.getW()) * canvas.getWidth();
                float y = (markerPosition.getY() / markerPosition.getW() * canvas.getHeight());

                int iconSize = Math.abs(markerPosition.getZ()) > ONE_KILOMETER ?
                        getResources().getDisplayMetrics().densityDpi / 3 : getResources().getDisplayMetrics().densityDpi / 2;

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    float temp = x;
                    x = -y;
                    y = temp;
                }

                marker.getMarkerOptions().getIcon().setBounds(
                        (int) (centerX + x),
                        (int) (centerY + y),
                        (int) (centerX + x + iconSize),
                        (int) (centerY + y + iconSize));

                marker.getMarkerOptions().getIcon().draw(canvas);

                canvas.drawText(
                        marker.getMarkerOptions().getTitle(),
                        (centerX + x + (iconSize / 2) - paint.measureText(marker.getMarkerOptions().getTitle()) / 2),
                        (centerY + y - 40),
                        paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int TOUCH_SIDE = 10;
        Rect eventPosition = new Rect((int) event.getX(),
                (int) event.getY(), (int) event.getX() + TOUCH_SIDE, (int) event.getY() + TOUCH_SIDE);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Marker<T> marker : markers) {
                if (marker.getMarkerOptions().getIcon().getBounds().intersect(eventPosition)) {
                    if(marker.getSize() == 1) {
                        T item = new ArrayList<>(marker.getItems()).get(0);
                        onArItemClickListener.onClusterItemClick(item);

                    } else {
                        onClusterClickListener.onClusterClick(marker.getCluster());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private class EmptyClickAction implements OnArItemClickListener<T>,
            OnClusterClickListener<T> {


        @Override
        public void onClusterItemClick(T clusterItem) {

        }

        @Override
        public void onClusterClick(Cluster<T> cluster) {

        }
    }
}
