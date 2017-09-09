package com.david.worldtourist.items.presentation.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.di.components.DaggerItemsComponent;
import com.david.worldtourist.items.di.components.ItemsComponent;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;
import com.david.worldtourist.items.presentation.boundary.ItemsView;
import com.david.worldtourist.items.presentation.presenter.ItemsPresenterImp;
import com.david.worldtourist.items.presentation.adapter.ItemsAdapter;
import com.david.worldtourist.items.presentation.component.click.ItemClick;
import com.david.worldtourist.items.presentation.component.search.TextSearch;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.sensors.device.services.SensorListener;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsFragment extends PermissionFragment implements ItemsView, ItemClick {

    private View view;

    private ItemsPresenter<ItemsView> presenter;
    private ItemsAdapter itemsAdapter;
    private LinearLayoutManager layoutManager;

    private Menu menu;

    @BindView(R.id.recycler_view) RecyclerView itemsRecycler;
    @BindView(R.id.no_items) TextView noItemsMessage;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    public static ItemsFragment newInstance(ItemCategory itemCategory) {
        final ItemsFragment itemsFragment = new ItemsFragment();
        final Bundle arguments = new Bundle();
        arguments.putSerializable(Constants.ITEM_CATEGORY_KEY, itemCategory);
        itemsFragment.setArguments(arguments);
        return itemsFragment;
    }

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        view = inflater.inflate(R.layout.fragment_items, container, false);

        ItemsComponent component = DaggerItemsComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getItemsPresenter();

        presenter.setView(this);
        presenter.cacheItemCategory(loadItemCategory());
        presenter.onCreate();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceSate) {
        super.onActivityCreated(saveInstanceSate);

        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);

        ((HomeActivity) getActivity()).setActionBarTitle(
                getString(presenter.getItemName(ItemNameFilter.PLURAL)));
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.connectSensor(SensorListener.LOCATION_SENSOR, (SensorListener) presenter);
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setupLayoutManager(newConfig.orientation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.connectSensor(SensorListener.LOCATION_SENSOR, (SensorListener) presenter);
            }
        } else if (requestCode == Constants.RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.startItemsSearch(getString(R.string.tts_start_search));
            }
        }
    }

    private ItemCategory loadItemCategory(){
        if(getArguments() != null){
            return (ItemCategory) getArguments().getSerializable(Constants.ITEM_CATEGORY_KEY);
        }

        return ItemCategory.EVENT;
    }

    ////////////////////////////Menu options//////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        setupSearchMenu(menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void setupSearchMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        final MenuItem searchVoiceItem = menu.findItem(R.id.menu_voice_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        TextSearch textSearch = new TextSearch();
        textSearch.addObserver((ItemsPresenterImp) presenter);
        searchView.setOnQueryTextListener(textSearch);

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        searchVoiceItem.setVisible(true);
                        presenter.restoreItems();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        searchVoiceItem.setVisible(false);
                        ((HomeActivity)getActivity()).setActionBarTitle(
                                getString(presenter.getItemName(ItemNameFilter.PLURAL)));
                        presenter.stopItemsSearch();
                        presenter.restoreItems();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                ((HomeActivity) getActivity()).setActionBarTitle("");
                break;
            case R.id.menu_voice_search:
                hideKeyboardIfOpened();
                presenter.restoreItems();
                presenter.startItemsSearch(getString(R.string.tts_start_search));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboardIfOpened() {
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if(getActivity().getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    ////////////////////////////BaseView implementation///////////////////////////
    @Override
    public void initializeViewComponents() {
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        itemsAdapter = new ItemsAdapter(getActivity());
        itemsAdapter.setClickAction(this);
        setupLayoutManager(getResources().getConfiguration().orientation);
        itemsRecycler.setLayoutManager(layoutManager);
        itemsRecycler.setAdapter(itemsAdapter);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight));

    }

    private void setupLayoutManager(int configuration) {
        final String XLARGE_PORTRAIT = "xlarge_portrait";

        View view = getActivity().findViewById(R.id.drawer_layout);

        if(view.getTag().equals(XLARGE_PORTRAIT)) {
            layoutManager = new LinearLayoutManager(getActivity());

        } else {
            if (configuration == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new LinearLayoutManager(getActivity());

            } else if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(getActivity(), 2);
            }
        }
        itemsRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public void initializeViewListeners() {
        itemsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int LAST_ITEM_POSITION = itemsAdapter.getItemCount() -1;

                if(newState == RecyclerView.SCROLL_STATE_SETTLING ||
                        newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                    if(layoutManager.findLastVisibleItemPosition() == LAST_ITEM_POSITION) {
                        progressBar.setVisibility(View.VISIBLE);
                        presenter.loadItems(LoadingState.UPDATE);
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                presenter.loadItems(LoadingState.RELOAD);
            }
        });
    }

    /////////////////////////FragmentView implementation//////////////////////////
    @Override
    public void showLoadingBar() {
        if(!refreshLayout.isRefreshing() && !progressBar.isShown()) {
            super.showLoadingBar();
        }
    }

    @Override
    public void hideLoadingBar() {
        if(refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);

        } else if (progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);

        } else {
            super.hideLoadingBar();
        }
    }

    //////////////////////////ItemsView implementation////////////////////////////
    @Override
    public void showItems(List<Item> items, GeoCoordinate userPosition) {
        itemsAdapter.replaceData(items, userPosition);
    }

    @Override
    public void showInfoMessage(int textReference, int iconReference) {
        noItemsMessage.setText(getString(textReference));
        Drawable iconMessage = ContextCompat.getDrawable(getActivity(), iconReference);
        noItemsMessage.setCompoundDrawablesWithIntrinsicBounds(null, iconMessage, null, null);
        noItemsMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInfoMessage() {
        noItemsMessage.setVisibility(View.GONE);
    }

    @Override
    public void enableMic(boolean enabled) {
        menu.getItem(1).setEnabled(enabled);
    }

    //////////////////////////ItemClick implementation/////////////////////////////
    @Override
    public void onItemClick(Item item) {
        presenter.cacheItem(item);
        ((HomeActivity) getActivity()).openFragment(FragmentType.ITEM_DETAIL, null);
    }
}