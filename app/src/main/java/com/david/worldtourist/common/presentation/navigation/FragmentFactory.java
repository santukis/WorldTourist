package com.david.worldtourist.common.presentation.navigation;


import android.app.Fragment;

import com.david.worldtourist.aritems.presentation.view.ArItemsFragment;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.presentation.view.ItemsFragment;
import com.david.worldtourist.itemsdetail.presentation.view.ItemDetailFragment;
import com.david.worldtourist.itemsdetail.presentation.view.StreetViewFragment;
import com.david.worldtourist.itemsdetail.presentation.view.WebViewFragment;
import com.david.worldtourist.message.presentation.view.MessageFragment;
import com.david.worldtourist.preferences.presentation.view.SettingsFragment;
import com.david.worldtourist.itemsmap.presentation.view.ItemsMapFragment;
import com.david.worldtourist.report.presentation.view.ReportFragment;
import com.david.worldtourist.useritems.presentation.view.UserItemsFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    private Map<FragmentType, Fragment> filter = new HashMap<>();

    public FragmentFactory(){
        filter.put(FragmentType.EVENTS, ItemsFragment.newInstance(ItemCategory.EVENT));
        filter.put(FragmentType.SITES, ItemsFragment.newInstance(ItemCategory.SITE));
        filter.put(FragmentType.USER_EVENTS, UserItemsFragment.newInstance(ItemCategory.EVENT));
        filter.put(FragmentType.USER_SITES, UserItemsFragment.newInstance(ItemCategory.SITE));
        filter.put(FragmentType.ITEM_DETAIL, new ItemDetailFragment());
        filter.put(FragmentType.ITEMS_MAP, new ItemsMapFragment());
        filter.put(FragmentType.ARITEMS, new ArItemsFragment());
        filter.put(FragmentType.PREFERENCES, new SettingsFragment());
        filter.put(FragmentType.WRITE_MESSAGE, new MessageFragment());
        filter.put(FragmentType.STREET_VIEW, new StreetViewFragment());
        filter.put(FragmentType.WEB, new WebViewFragment());
        filter.put(FragmentType.REPORT, new ReportFragment());
    }

    public Fragment create(FragmentType type){
        return this.filter.get(type);
    }
}