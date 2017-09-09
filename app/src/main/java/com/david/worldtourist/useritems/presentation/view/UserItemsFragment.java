package com.david.worldtourist.useritems.presentation.view;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.items.presentation.component.click.ItemClick;
import com.david.worldtourist.items.presentation.component.click.ItemLongClick;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.useritems.di.components.DaggerUserItemsComponent;
import com.david.worldtourist.useritems.di.components.UserItemsComponent;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsPresenter;
import com.david.worldtourist.useritems.presentation.adapter.UserItemsAdapter;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsView;
import com.david.worldtourist.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserItemsFragment extends PermissionFragment implements UserItemsView, ItemClick, ItemLongClick {

    private View view;
    private UserItemsPresenter<UserItemsView> presenter;
    private UserItemsAdapter itemsAdapter;

    private Menu menu;

    @BindView(R.id.recycler_view) RecyclerView itemsRecycler;
    @BindView(R.id.no_items) TextView noItemsMessage;

    public static UserItemsFragment newInstance(ItemCategory itemCategory) {
        final UserItemsFragment itemsFragment = new UserItemsFragment();
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

        view = inflater.inflate(R.layout.fragment_user_items, container, false);

        UserItemsComponent component = DaggerUserItemsComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .build();

        presenter = component.getUserItemsPresenter();
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
    }

    @Override
    public void onStart() {
        presenter.loadUserItems();
        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        itemsAdapter.clearMemory();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            itemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            itemsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }

    private ItemCategory loadItemCategory(){
        if(getArguments() != null){
            return (ItemCategory) getArguments().getSerializable(Constants.ITEM_CATEGORY_KEY);
        }

        return ItemCategory.EVENT;
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        this.menu = menu;
        inflater.inflate(R.menu.menu_user_items, menu);
        inflater.inflate(R.menu.menu_delete, menu);

        menu.getItem(0).getIcon().setColorFilter(ContextCompat.getColor(getActivity(),
                android.R.color.white), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_delete:
                presenter.deleteItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFilteringPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.menu_items_filter, popupMenu.getMenu());


        int[] itemRes = new int[]{R.id.favourite, R.id.to_visit, R.id.all};
        String itemReference = getString(presenter.getItemName(ItemNameFilter.PLURAL));
        String[] stringValues = new String[]{
                String.format(getString(R.string.menu_favourite_items), itemReference),
                String.format(getString(R.string.menu_items_to_visit), itemReference),
                getString(R.string.menu_all)};

        for (int i = 0; i < itemRes.length; i++) {
            popupMenu.getMenu().findItem(itemRes[i]).setTitle(stringValues[i]);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourite:
                        presenter.cacheUserFilter(ItemUserFilter.FAVOURITE);
                        break;
                    case R.id.to_visit:
                        presenter.cacheUserFilter(ItemUserFilter.TO_VISIT);
                        break;
                    case R.id.all:
                        presenter.cacheUserFilter(ItemUserFilter.ALL);
                }
                presenter.loadUserItems();
                return true;
            }
        });
        popupMenu.show();
    }

    ////////////////////////////BaseView implementation////////////////////////////
    @Override
    public void initializeViewComponents() {
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        itemsAdapter = new UserItemsAdapter(getActivity());
        itemsAdapter.setClickAction(this);
        itemsAdapter.setLongClickAction(this);
        itemsRecycler.setAdapter(itemsAdapter);
        itemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((HomeActivity) getActivity())
                .setActionBarTitle(getString(R.string.my).concat(" ").concat(getString(
                        presenter.getItemName(ItemNameFilter.PLURAL))));
    }

    /////////////////////////ItemsView implementation////////////////////////////
    @Override
    public void showItems(List<Item> events, GeoCoordinate userPosition) {
        itemsAdapter.replaceData(events);
    }

    @Override
    public void showInfoMessage(int textReference, int iconReference) {
        noItemsMessage.setText(String.format(getString(R.string.no_item_in_my_items),
                getString(textReference)));
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

    }

    ////////////////////////UserItemsView implementation//////////////////////////
    @Override
    public void showDeleteMenu() {
        menu.getItem(0).setVisible(true);
    }

    @Override
    public void hideDeleteMenu() {
        menu.getItem(0).setVisible(false);
    }

    //////////////////////////ItemClick implementation/////////////////////////////
    @Override
    public void onItemClick(Item item) {
        presenter.cacheItem(item);

        ((HomeActivity) getActivity()).openFragment(FragmentType.ITEM_DETAIL, null);
    }

    ////////////////////////ItemLongClick implementation///////////////////////////
    @Override
    public void onItemLongClick(final String itemId) {
        presenter.updateSelectedItems(itemId);
    }
}