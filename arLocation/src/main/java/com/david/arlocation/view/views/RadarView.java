package com.david.arlocation.view.views;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.david.arlocation.R;
import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.CameraPosition;
import com.david.arlocation.view.model.Marker;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.boundary.ArView;

import java.util.HashSet;
import java.util.Set;

public class RadarView<T extends ArItem> extends View implements ArView<T> {

    private final int RADAR_RADIUS = getResources().getDimensionPixelSize(R.dimen.radar_radius);
    private final int POINT_RADIUS = getResources().getDimensionPixelSize(R.dimen.radar_point_radius);
    private final int PADDING = getResources().getDimensionPixelSize(R.dimen.radar_padding);
    private final int CENTER = RADAR_RADIUS + PADDING;

    private Set<Marker<T>> markers = new HashSet<>();

    private Paint radarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addMarkers(Set<? extends Marker<T>> markers) {
        this.markers.addAll(markers);
        invalidate();
    }

    @Override
    public void drawMarkers() {
        invalidate();
    }

    @Override
    public void clearMarkers() {
        markers.clear();
        invalidate();
    }

    @Override
    public void setMarkerRenderer(MarkerRenderer<T> markerRenderer) {

    }

    @Override
    public void setOnArItemClickListener(OnArItemClickListener<T> listener) {

    }

    @Override
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (markers.isEmpty()) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            return;
        }
        drawRadar(canvas);
        drawRadarPoints(canvas);
    }

    private void drawRadar(Canvas canvas) {

        //Radar Fill
        radarPaint.setStyle(Paint.Style.FILL);
        radarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorRadar));
        canvas.drawCircle(CENTER, CENTER, RADAR_RADIUS, radarPaint);

        //Radar Concentric Circumferences
        radarPaint.setStyle(Paint.Style.STROKE);
        radarPaint.setStrokeWidth(2);
        radarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorRadarLines));
        canvas.drawCircle(CENTER, CENTER, RADAR_RADIUS - 20, radarPaint);
        canvas.drawCircle(CENTER, CENTER, RADAR_RADIUS - 40, radarPaint);

        //Radar Perpendicular Lines
        canvas.drawLine(CENTER, CENTER + RADAR_RADIUS, CENTER, CENTER - RADAR_RADIUS, radarPaint);
        canvas.drawLine(CENTER - RADAR_RADIUS, CENTER, CENTER + RADAR_RADIUS, CENTER, radarPaint);

        //Radar User View
        radarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        radarPaint.setStrokeWidth(3);
        canvas.drawLine(
                CENTER,
                CENTER,
                (float) (CENTER + Math.cos(Math.toRadians(270 - 35)) * RADAR_RADIUS),
                (float) (CENTER + (Math.sin(Math.toRadians(270 - 35))) * RADAR_RADIUS),
                radarPaint);
        canvas.drawLine(
                CENTER,
                CENTER,
                (float) (CENTER + Math.cos(Math.toRadians(270 + 35)) * RADAR_RADIUS),
                (float) (CENTER + (Math.sin(Math.toRadians(270 + 35))) * RADAR_RADIUS),
                radarPaint);

        //Radar Stroke
        radarPaint.setColor(Color.BLACK);
        canvas.drawCircle(CENTER, CENTER, RADAR_RADIUS, radarPaint);

    }

    private void drawRadarPoints(Canvas canvas) {

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        double maxDistance = getMaxDistance();

        for (Marker<T> marker : markers) {
            CameraPosition markerPosition = marker.getPosition();

            if (markerPosition.getX() < -maxDistance) markerPosition.setX((float) -maxDistance);

            else if (markerPosition.getX() > maxDistance) markerPosition.setX((float) maxDistance);

            if (markerPosition.getZ() < -maxDistance) markerPosition.setZ((float) -maxDistance);

            else if (markerPosition.getZ() > maxDistance) markerPosition.setZ((float) maxDistance);

            if (markerPosition.getY() < -maxDistance) markerPosition.setY((float) -maxDistance);

            else if (markerPosition.getY() > maxDistance) markerPosition.setY((float) maxDistance);

            float x = (float) (markerPosition.getX() * RADAR_RADIUS / maxDistance);
            float y = (float) (markerPosition.getZ() * RADAR_RADIUS / maxDistance);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                x = (float) - (markerPosition.getY() * RADAR_RADIUS / maxDistance);
            }

            double arc = Math.atan(y / x);

            canvas.drawCircle(
                    CENTER - (float) (x * Math.cos(Math.abs(arc))),
                    CENTER + (float) (y * Math.sin(Math.abs(arc))),
                    POINT_RADIUS,
                    circlePaint);
        }
    }

    private double getMaxDistance() {
        double distance = 0;

        for(Marker<T> marker : markers) {
            if(Math.abs(marker.getPosition().getZ()) > distance) {
                distance = Math.abs(marker.getPosition().getZ());
            }
        }
        return distance;
    }
}
