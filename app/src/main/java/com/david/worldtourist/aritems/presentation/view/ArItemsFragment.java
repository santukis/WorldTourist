package com.david.worldtourist.aritems.presentation.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.arlocation.aritems.boundary.ArManager;
import com.david.arlocation.aritems.manager.DefaultArManager;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.views.MarkersView;
import com.david.arlocation.view.views.RadarView;
import com.david.worldtourist.R;
import com.david.worldtourist.aritems.di.components.ArItemsComponent;
import com.david.worldtourist.aritems.di.components.DaggerArItemsComponent;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsPresenter;
import com.david.worldtourist.aritems.presentation.components.ArMarkerRenderer;
import com.david.worldtourist.aritems.presentation.components.AItem;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsView;
import com.david.worldtourist.aritems.presentation.adapter.ClusterItemAdapter;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArItemsFragment extends PermissionFragment implements ArItemsView,
        OnArItemClickListener<AItem>, OnClusterClickListener<AItem> {

    private View view;

    private ArItemsPresenter<ArItemsView> presenter;

    private ArManager<AItem> arManager;

    @BindView(R.id.camera_view) TextureView cameraPreview;
    @BindView(R.id.aritems_overlay) MarkersView<AItem> clusterView;
    @BindView(R.id.radar_view) RadarView<AItem> radarView;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        view = inflater.inflate(R.layout.fragment_ar_location, container, false);

        ArItemsComponent component = DaggerArItemsComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getArItemsPresenter();
        presenter.setView(this);
        presenter.onCreate();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceSate) {
        super.onActivityCreated(saveInstanceSate);
        hideStatusBar();
    }

    private void hideStatusBar() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseArItemsManager();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    /////////////////////////Permission Management////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeArItemsManager();
                presenter.loadArItems(LoadingState.FIRST_LOAD);

            } else {
                closeView();
            }
        }
    }

    /////////////////////////BaseView implementation////////////////////////
    @Override
    public void initializeViewComponents() {
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        arManager = new DefaultArManager<>(getActivity(), cameraPreview, clusterView, radarView);

        arManager.setMarkerRenderer(new ArMarkerRenderer(getActivity().getApplicationContext()));

    }

    @Override
    public void initializeViewListeners() {
        arManager.setOnArItemClickListener(this);
        arManager.setOnClusterClickListener(this);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                View sheetView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.element_bottom_sheet_menu, null);

                onEventsClick(bottomSheetDialog, sheetView);

                onSitesClick(bottomSheetDialog, sheetView);

                onDismissView(bottomSheetDialog);

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
                return true;
            }
        });
    }

    private void onEventsClick(final BottomSheetDialog dialog, View sheetView) {
        RelativeLayout events = (RelativeLayout) sheetView.findViewById(R.id.events_layout);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cacheItemCategory(ItemCategory.EVENT);
                presenter.loadArItems(LoadingState.FIRST_LOAD);
                dialog.dismiss();
            }
        });
    }

    private void onSitesClick(final BottomSheetDialog dialog, View sheetView) {
        RelativeLayout sites = (RelativeLayout) sheetView.findViewById(R.id.sites_layout);
        sites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cacheItemCategory(ItemCategory.SITE);
                presenter.loadArItems(LoadingState.FIRST_LOAD);
                dialog.dismiss();
            }
        });
    }

    private void onDismissView(BottomSheetDialog bottomSheetDialog) {
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideStatusBar();
            }
        });
    }

    //////////////////////ArItemsView implementation////////////////////////

    @Override
    public void initializeArItemsManager() {
        arManager.init();
    }

    @Override
    public void releaseArItemsManager() {
        arManager.release();
    }

    @Override
    public void showArItems(List<AItem> aItems) {
        arManager.addArItems(aItems);
    }

    @Override
    public void onClusterClick(final Cluster<AItem> cluster) {
        List<AItem> aItems = new ArrayList<>(cluster.getItems());

        ListAdapter adapter = new ClusterItemAdapter(getActivity().getApplicationContext(), aItems);

        new AlertDialog.Builder(getActivity())
                .setTitle(aItems.get(0).getItem().getAddress())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onClusterItemClick(new ArrayList<>(cluster.getItems()).get(which));
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        hideStatusBar();
                    }
                })
                .show();
    }

    @Override
    public void onClusterItemClick(AItem clusterItem) {

        Item item = clusterItem.getItem();

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        View itemView = getActivity().getLayoutInflater()
                .inflate(R.layout.element_bottom_item_options, null);

        ImageView itemIcon = (ImageView) itemView.findViewById(R.id.ar_item_icon);
        itemIcon.setBackgroundResource(item.getType().getIcon());

        TextView howToGet = (TextView) itemView.findViewById(R.id.ar_how_to);
        howToGet.setCompoundDrawables(setDrawableColor(howToGet.getCompoundDrawables()[0],
                R.color.colorPrimary), null, null, null);

        TextView itemName = (TextView) itemView.findViewById(R.id.ar_title);
        itemName.setText(item.getName());

        onHowToGetClick(item, dialog, howToGet);

        onDetailClick(item, dialog, itemView);

        onDismissView(dialog);

        dialog.setContentView(itemView);
        dialog.show();
    }

    private Drawable setDrawableColor(Drawable iconDrawable, int colorResource) {
        iconDrawable.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(),
                colorResource), PorterDuff.Mode.SRC_IN);
        return iconDrawable;
    }

    private void onHowToGetClick(final Item item, final BottomSheetDialog dialog, TextView howTo) {
        howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.cacheMapCoordinates(item.getCoordinate());

                ((HomeActivity) getActivity()).openFragment(FragmentType.ITEMS_MAP, null);
            }
        });
    }

    private void onDetailClick(final Item item, final BottomSheetDialog dialog, View itemView) {
        itemView.findViewById(R.id.ar_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.cacheItem(item);

                ((HomeActivity)getActivity()).openFragment(FragmentType.ITEM_DETAIL, null);
            }
        });
    }
}