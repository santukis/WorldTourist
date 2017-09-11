package com.david.worldtourist.itemsmap.presentation.view;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.aritems.presentation.adapter.ClusterItemAdapter;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.itemsmap.di.components.DaggerItemsMapComponent;
import com.david.worldtourist.itemsmap.di.components.ItemsMapComponent;
import com.david.worldtourist.itemsmap.domain.usecase.model.Address;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.Step;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;
import com.david.worldtourist.itemsmap.presentation.adapter.RouteAdapter;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapPresenter;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapView;
import com.david.worldtourist.itemsmap.presentation.gmaps.GInfoWindowAdapter;
import com.david.worldtourist.itemsmap.presentation.gmaps.GItem;
import com.david.worldtourist.itemsmap.presentation.gmaps.GItemRenderer;
import com.david.worldtourist.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsMapFragment extends PermissionFragment implements ItemsMapView,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        ClusterManager.OnClusterClickListener<GItem>,
        ClusterManager.OnClusterItemClickListener<GItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<GItem> {

    private static final int MAX_ZOOM = 18;
    private static final int MIN_ZOOM = 7;
    private static final int START_ZOOM = 16;

    private View navigationView;
    private ItemsMapPresenter<ItemsMapView> presenter;

    private Menu menu;

    private GoogleMap map;
    private GItem selectedMarker;
    private Polyline selectedPolyline;
    private List<GItem> markers = new ArrayList<>();

    private ClusterManager<GItem> clusterManager;

    private RouteAdapter routeAdapter;
    private BottomSheetBehavior sheetBehavior;

    @BindView(R.id.fab_directions) FloatingActionButton fabDirections;
    @BindView(R.id.fab_driving) FloatingActionButton fabDriving;
    @BindView(R.id.fab_walking) FloatingActionButton fabWalking;
    @BindView(R.id.bottom_sheet_layout) LinearLayout bottomLayout;
    @BindView(R.id.item_name) TextView routeName;
    @BindView(R.id.icon_mode) ImageView iconMode;
    @BindView(R.id.distance) TextView routeDistance;
    @BindView(R.id.duration) TextView routeDuration;
    @BindView(R.id.button_remove) Button removeRoute;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);

        navigationView = inflater.inflate(R.layout.fragment_items_map, container, false);

        ItemsMapComponent component = DaggerItemsMapComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getItemsMapPresenter();
        presenter.setView(this);
        presenter.onCreate();

        setHasOptionsMenu(true);

        return navigationView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceSate) {
        super.onActivityCreated(saveInstanceSate);

        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentByTag("mapFragment");

        if(mapFragment == null) {
            mapFragment = new MapFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_layout, mapFragment, "mapFragment");
            fragmentTransaction.commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onDestroyView() {
        GeoCoordinate mapCoordinates = new GeoCoordinate(map.getCameraPosition().target.latitude,
                map.getCameraPosition().target.longitude);
        presenter.cacheMapCoordinates(mapCoordinates);

        clearMap();
        clearMarkers();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        releaseMapViews();
        super.onDestroy();
    }

    private void releaseMapViews() {
        clusterManager = null;
        selectedPolyline = null;
        selectedMarker = null;
        markers = null;
        map = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.startItemsSearch(getString(R.string.address_search));
            }
        } else if (requestCode == Constants.FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocationListeners();
            }
        }
    }

    ////////////////////////////Menu options//////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_voice, menu);
        inflater.inflate(R.menu.menu_detail, menu);

        menu.getItem(1).getIcon().setColorFilter(ContextCompat.getColor(getActivity(),
                android.R.color.white), PorterDuff.Mode.SRC_IN);

        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;

            case R.id.menu_voice:
                presenter.startItemsSearch(getString(R.string.address_search));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilteringPopUpMenu() {
        PopupMenu popupMenu =
                new PopupMenu(getActivity(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_category, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GeoCoordinate coordinates = new GeoCoordinate(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());

                switch (item.getItemId()) {
                    case R.id.events:
                        presenter.cacheItemCategory(ItemCategory.EVENT);
                        presenter.loadItems(coordinates);
                        setActionBarTitle();
                        return true;

                    case R.id.sites:
                        presenter.cacheItemCategory(ItemCategory.SITE);
                        presenter.loadItems(coordinates);
                        setActionBarTitle();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    ////////////////////////BaseView implementation////////////////////////////
    @Override
    public void initializeViewComponents() {
        ButterKnife.bind(this, navigationView);

        RecyclerView recyclerView = (RecyclerView) navigationView.findViewById(R.id.recycler_view);
        routeAdapter = new RouteAdapter(getActivity());
        recyclerView.setAdapter(routeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        sheetBehavior = BottomSheetBehavior.from(bottomLayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        setActionBarTitle();

        super.initializeViewComponents();
    }

    private void setActionBarTitle() {
        final String INITIAL_PARENTHESIS = " (";
        final String FINAL_PARENTHESIS = ")";

        String title = getString(R.string.virtual_tour)
                .concat(INITIAL_PARENTHESIS)
                .concat(getString(presenter.getItemName(ItemNameFilter.PLURAL)))
                .concat(FINAL_PARENTHESIS);
        ((HomeActivity) getActivity()).setActionBarTitle(title);
    }

    @Override
    public void initializeViewListeners() {
        fabDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabDriving.show();
                fabWalking.show();
            }
        });

        fabDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadRoute(selectedMarker.getItem(), getOrigin(), TravelMode.DRIVING);
            }
        });

        fabWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadRoute(selectedMarker.getItem(), getOrigin(), TravelMode.WALKING);
            }
        });

        removeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPolyline != null) {
                    presenter.deleteRoute((Route) selectedPolyline.getTag());
                    selectedPolyline.remove();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
    }

    public GeoCoordinate getOrigin() {
        return new GeoCoordinate(map.getMyLocation().getLatitude(),
                map.getMyLocation().getLongitude());
    }

    ////////////////////////ItemsMapView implementation////////////////////////////
    @Override
    public void showStartingPosition(GeoCoordinate startingPosition) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                startingPosition.getLatitude(), startingPosition.getLongitude()), START_ZOOM));

        presenter.loadItems(startingPosition);
    }

    @Override
    public void showStartingItem(Item item) {
        GItem gItem = new GItem(item);
        clusterManager.addItem(gItem);
        markers.add(gItem);
    }

    @Override
    public void showAddressIcon(Address address) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(address.getCoordinates().getLatitude(),
                        address.getCoordinates().getLongitude()))
                .title(address.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        marker.showInfoWindow();
    }

    @Override
    public void clearMap() {
        map.clear();
    }

    @Override
    public void clearMarkers() {
        markers.clear();
        clusterManager.clearItems();
    }

    @Override
    public void showItems(final List<Item> items) {

        try {
            for (Item item : items) {
                if (isItemAlreadyClustered(item)) {
                    continue;
                }

                GItem gItem = new GItem(item);
                clusterManager.addItem(gItem);
                markers.add(gItem);
            }
            clusterManager.cluster();

        } catch (NullPointerException exception) {
        }
    }

    private boolean isItemAlreadyClustered(Item item) throws NullPointerException {

        for (GItem gItem : markers) {
            if (gItem.getItem().getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadItemDetail() {
        ((HomeActivity) getActivity()).openFragment(FragmentType.ITEM_DETAIL, null);
    }

    @Override
    public void drawRoute(Route route) {
        List<LatLng> points = new ArrayList<>();

        for (GeoCoordinate routePoint : route.getRoutePoints()) {
            LatLng point = new LatLng(routePoint.getLatitude(), routePoint.getLongitude());
            points.add(point);
        }

        Polyline polyline = map.addPolyline(new PolylineOptions()
                .addAll(points)
                .color(ContextCompat.getColor(getActivity(), route.getTravelMode().getColor()))
                .clickable(true)
                .width(10));

        polyline.setPattern(getPattern(route.getTravelMode()));
        polyline.setJointType(JointType.BEVEL);
        polyline.setTag(route);

        selectedPolyline = polyline;
    }

    private List<PatternItem> getPattern(TravelMode mode) {
        final Dot DOT = new Dot();
        final Gap GAP = new Gap(5);

        List<PatternItem> pattern = new ArrayList<>();

        switch (mode) {
            case DRIVING:
                pattern = null;  //Default stroke pattern (solid) represented by null
                break;
            case WALKING:
                pattern = Arrays.asList(DOT, GAP);
                break;
        }

        return pattern;
    }

    @Override
    public void showRouteView() {
        map.getUiSettings().setZoomControlsEnabled(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showRouteDestinationName(String destinationName) {
        if(destinationName.isEmpty()) routeName.setVisibility(View.GONE);
        else {
            routeName.setText(destinationName);
            routeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }
    }

    @Override
    public void showRouteIcon(TravelMode mode) {
        if(mode == TravelMode.NONE) iconMode.setVisibility(View.GONE);
        else {
            iconMode.setImageResource(mode.getIcon());
            iconMode.setColorFilter(ContextCompat.getColor(getActivity(), mode.getColor()),
                    PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void showRouteDistance(String distance) {
        if(distance.isEmpty()) {
            routeDistance.setText(R.string.route_error);
        }
        else {
            routeDistance.setText(distance);
        }
    }

    @Override
    public void showRouteDuration(String duration) {
        if(duration.isEmpty()) routeDuration.setVisibility(View.GONE);
        else {
            routeDuration.setText(duration);
        }
    }

    @Override
    public void showRouteInstructions(List<Step> steps) {
        routeAdapter.addSteps(steps);
    }

    @Override
    public void enableMic(boolean enabled) {
        menu.getItem(0).setEnabled(enabled);
    }

    ////////////////////////OnMapReadyCallback implementation////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.clear();

        initializeMapManagers();

        setupMapAttributes();

        setupMapListeners();

        setupLocationListeners();
    }

    private void initializeMapManagers() {
        clusterManager = new ClusterManager<>(getActivity().getApplicationContext(), map);
        clusterManager.setRenderer(new GItemRenderer(getActivity(), map, clusterManager));
    }

    private void setupMapAttributes() {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMaxZoomPreference(MAX_ZOOM);
        map.setMinZoomPreference(MIN_ZOOM);
    }

    private void setupMapListeners() {
        map.setOnMapClickListener(this);
        map.setOnPolylineClickListener(this);
        map.setInfoWindowAdapter(new GInfoWindowAdapter(getActivity().getApplicationContext()));
        map.setOnInfoWindowClickListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    private void setupLocationListeners() {
        try{
            map.setMyLocationEnabled(true);
            map.setOnCameraIdleListener(this);
            presenter.loadStartingPosition();

        }  catch(SecurityException exception) {

        }
    }

    ///////////////////////OnCameraIdleListener implementation////////////////////////
    @Override
    public void onCameraIdle() {
        clusterManager.onCameraIdle();
        GeoCoordinate mapCoordinates = new GeoCoordinate(map.getCameraPosition().target.latitude,
                map.getCameraPosition().target.longitude);

        presenter.loadItems(mapCoordinates);
    }

    ////////////////////////OnMapClickListener implementation/////////////////////////
    @Override
    public void onMapClick(LatLng latLng) {
        fabDirections.hide();
        fabWalking.hide();
        fabDriving.hide();

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    //////////////////////OnPolylineClickListener implementation///////////////////////
    @Override
    public void onPolylineClick(final Polyline polyline) {
        selectedPolyline = polyline;
        presenter.refreshRoute((Route) polyline.getTag());
    }

    /////////////ClusterController.OnClusterClickListener implementation//////////////////
    @Override
    public boolean onClusterClick(Cluster<GItem> cluster) {
        if(isMaxZoomReached()) {
            showDialogWithClusterItems(cluster);
            return true;
        }

        LatLngBounds.Builder builder = LatLngBounds.builder();

        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }

        final LatLngBounds bounds = builder.build();
        final int PADDING = 100;
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, PADDING));

        return true;
    }

    private boolean isMaxZoomReached() {
        float zoom = map.getCameraPosition().zoom;
        return zoom >= MAX_ZOOM;
    }

    private void showDialogWithClusterItems(Cluster<GItem> cluster) {
        final List<GItem> gItems = new ArrayList<>(cluster.getItems());

        ListAdapter adapter = new ClusterItemAdapter(getActivity().getApplicationContext(), gItems);

        new AlertDialog.Builder(getActivity())
                .setTitle(gItems.get(0).getItem().getAddress())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onClusterItemInfoWindowClick(gItems.get(which));
                    }
                })
                .show();

    }

    /////////////ClusterController.OnClusterItemClickListener implementation//////////////
    @Override
    public boolean onClusterItemClick(GItem gItem) {
        selectedMarker = gItem;
        fabDirections.show();
        return false;
    }

    //////////ClusterController.OnClusterItemInfoWindowClickListener implementation////////
    @Override
    public void onClusterItemInfoWindowClick(GItem gItem) {
        if(gItem != null) {
            presenter.cacheItem(gItem.getItem());
        }
    }
}