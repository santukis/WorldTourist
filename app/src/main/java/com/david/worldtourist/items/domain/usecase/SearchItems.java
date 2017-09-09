package com.david.worldtourist.items.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class SearchItems extends UseCase<SearchItems.RequestValues, SearchItems.ResponseValues> {

    @Inject
    public SearchItems() {}

    @Override
    public void execute(RequestValues requestValues) {
    }

    @Override
    public void execute(SearchItems.RequestValues requestValues,
                        Callback<SearchItems.ResponseValues> callback) {
        Set<Item> itemsFound = new HashSet<>();

        for (String word : requestValues.getWords()) {
            word = word.toLowerCase();
            for (Item item : requestValues.getItems()) {
                if (item.getName().toLowerCase().contains(word) ||
                        word.contains(item.getName().toLowerCase())) {
                    itemsFound.add(item);
                }
            }
        }

        if (!itemsFound.isEmpty()) {
            callback.onSuccess(new ResponseValues(new ArrayList<>(itemsFound)));

        } else {
            callback.onError(Constants.EMPTY_LIST_ERROR);
        }
    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<Item> items;
        private List<String> words;

        public RequestValues(List<Item> items, List<String> words) {
            this.items = items;
            this.words = words;
        }

        public List<Item> getItems() {
            return items;
        }

        public List<String> getWords() {
            return words;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private List<Item> itemsFound;

        public ResponseValues(List<Item> items) {
            itemsFound = items;
        }

        public List<Item> getItemsFound() {
            return itemsFound;
        }
    }
}
