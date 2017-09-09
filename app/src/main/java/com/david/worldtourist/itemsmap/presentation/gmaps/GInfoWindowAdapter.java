package com.david.worldtourist.itemsmap.presentation.gmaps;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.ItemType;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class GInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View contentsView;

    public GInfoWindowAdapter(Context context) {
        this.context = context;
        contentsView = LayoutInflater.from(context).inflate(R.layout.element_info_content, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        TextView contentTitle = (TextView) contentsView.findViewById(R.id.content_title) ;
        contentTitle.setText(marker.getTitle());

        if(marker.getSnippet() != null && !marker.getSnippet().isEmpty()) {
            ItemType itemType = ItemType.valueOf(marker.getSnippet());
            contentTitle.setTextColor(ContextCompat.getColor(context, itemType.getColor()));
            contentsView.setBackgroundResource(itemType.getBackground());
        }

        return contentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
