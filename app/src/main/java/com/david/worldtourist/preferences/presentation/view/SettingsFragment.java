package com.david.worldtourist.preferences.presentation.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.preferences.di.component.DaggerPreferenceComponent;
import com.david.worldtourist.preferences.di.component.PreferenceComponent;
import com.david.worldtourist.preferences.presentation.boundary.SettingsPresenter;
import com.david.worldtourist.preferences.presentation.boundary.SettingsView;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.preferences.presentation.component.SeekBarPreference;

import java.util.List;
import java.util.Set;


public class SettingsFragment extends PreferenceFragment implements SettingsView,
        Preference.OnPreferenceChangeListener{

    private SettingsPresenter<SettingsView> presenter;

    private MultiSelectListPreference itemTypes;
    private SeekBarPreference siteDistanceBar;
    private SeekBarPreference eventDistanceBar;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceComponent component = DaggerPreferenceComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .build();

        presenter = component.getSettingsPresenter();
        presenter.setView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View view = super.onCreateView(inflater, container, saveInstanceState);

        view.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));

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
        super.onStart();
        presenter.onStart();
    }


    @Override
    public void initializeViewComponents() {
        ((HomeActivity) getActivity()).setActionBarTitle(getString(R.string.menu_preferences));

        itemTypes = (MultiSelectListPreference) findPreference(getString(R.string.prf_item_type_multiselect));
        siteDistanceBar = (SeekBarPreference) findPreference(getString(R.string.prf_livetour_seekbar));
        eventDistanceBar = (SeekBarPreference) findPreference(getString(R.string.prf_event_seekbar));

    }

    @Override
    public void initializeViewListeners() {

        itemTypes.setOnPreferenceChangeListener(this);
        siteDistanceBar.setOnPreferenceChangeListener(this);
        eventDistanceBar.setOnPreferenceChangeListener(this);
    }

    @Override
    public void showSiteDistance(double distance) {
        siteDistanceBar.setCurrentValue(distance);
    }

    @Override
    public void showEventDistance(double distance) {
        eventDistanceBar.setCurrentValue(distance);
    }

    @Override
    public void showWarmingMessage() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.prf_warming)
                .setMessage(R.string.prf_no_type_selected_warming)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void showItemsTypeSummary(List<String> selectedItems) {
        if(selectedItems.isEmpty()) {
            itemTypes.setSummary(R.string.prf_no_type_selected);

        } else {
            StringBuilder summary = new StringBuilder();
            final int LAST_TYPE = selectedItems.size() - 1;
            final String COMMA = ", ";
            final String DOT = ".";

            for (int type = 0; type < selectedItems.size(); type++) {
                summary.append(getString(ItemType.valueOf(selectedItems.get(type)).getName()));
                summary = type < LAST_TYPE ? summary.append(COMMA) : summary.append(DOT);
            }
            itemTypes.setSummary(getString(R.string.prf_site_type_new_summary_start)
                    + " " + summary);
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();

        if(key.equals(getString(R.string.prf_livetour_seekbar))) {
            if(newValue instanceof Double) {
                presenter.saveDistance((double) newValue, ItemCategory.SITE);
            }

        } else if (key.equals(getString(R.string.prf_event_seekbar))) {
            if(newValue instanceof Double) {
                presenter.saveDistance((double) newValue, ItemCategory.EVENT);
            }

        } else if (key.equals(getString(R.string.prf_item_type_multiselect))) {
            if (newValue instanceof Set<?>) {
                presenter.saveTypes((Set<String>) newValue);
            }
        }

        return true;
    }
}
