package com.david.worldtourist.itemsdetail.presentation.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.david.worldtourist.R;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.utils.Constants;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StreetViewFragment extends Fragment {

    StreetViewPanorama panorama;

    LatLng panoramaLocation = new LatLng(0, 0);

    @BindView(R.id.progress_bar_layout) RelativeLayout progressBarLayout;


    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_street_view, container, false);

        if (getArguments() != null) {
            GeoCoordinate coordinates =
                    (GeoCoordinate) getArguments().getSerializable(Constants.COORDINATES_KEY);
            if (coordinates != null)
                panoramaLocation =
                        new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
        }

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {

        FragmentManager fragmentManager = getChildFragmentManager();
        StreetViewPanoramaFragment panoramaFragment =
                (StreetViewPanoramaFragment) fragmentManager.findFragmentByTag("panoramaFragment");

        if(panoramaFragment == null) {
            panoramaFragment = new StreetViewPanoramaFragment();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.streetview_container, panoramaFragment, "panoramaFragment");
            fragmentTransaction.commit();
        }

        panoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                panorama = streetViewPanorama;
                initializePanorama();
            }
        });
    }

    private void initializePanorama() {
        progressBarLayout.setVisibility(View.GONE);
        panorama.setStreetNamesEnabled(true);
        panorama.setUserNavigationEnabled(true);
        panorama.setPosition(panoramaLocation);
    }
}