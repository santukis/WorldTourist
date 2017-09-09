package com.david.worldtourist.items.presentation.component.search;

import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class TextSearch extends Observable implements SearchView.OnQueryTextListener {

    ////////////SearchView.OnQueryTextListener implementation//////////////
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String currentText) {
        List<String> results = new ArrayList<>();
        results.add(currentText);
        setChanged();
        notifyObservers(results);
        return true;
    }
}
