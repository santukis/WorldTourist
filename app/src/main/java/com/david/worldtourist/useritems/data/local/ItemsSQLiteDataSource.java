package com.david.worldtourist.useritems.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsDataSource;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;
import com.david.worldtourist.useritems.domain.usecases.filter.ItemFilter;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ItemsSQLiteDataSource implements ItemsDataSource.Local {

    private DataBaseHelper dbHelper;

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String EQUALS = " = ";
    private static final String ALL_COLUMNS = "*";
    private static final String QUOTE = "'";

    public ItemsSQLiteDataSource(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    @Override
    public void getUserItems(@NonNull GetUserItems.RequestValues requestValues,
                             @NonNull UseCase.Callback<GetUserItems.ResponseValues> callback) {

        List<Item> userItems = new ArrayList<>();
        String itemClass = requestValues.getItemClass().toString();
        ItemFilter itemFilter = requestValues.getItemFilter();

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String QUERY = SELECT + ALL_COLUMNS + FROM +
                DataBasePersistence.UserItems.ITEMS_TABLE_NAME + WHERE +
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_CLASS + EQUALS +
                QUOTE + itemClass + QUOTE;

        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = buildItem(cursor);
                userItems.add(item);
            }
            cursor.close();
        }
        database.close();

        List<Item> filteredItems = itemFilter.filter(userItems);

        if (!filteredItems.isEmpty()) {
            callback.onSuccess(new GetUserItems.ResponseValues(filteredItems));
        } else {
            callback.onError(Constants.EMPTY_LIST_ERROR);
        }
    }

    @Override
    public void getItem(@NonNull GetItem.RequestValues requestValues,
                        @NonNull UseCase.Callback<GetItem.ResponseValues> callback) {

        Item item = Item.EMPTY_ITEM;
        String itemId = requestValues.getItemId();

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String QUERY = SELECT + ALL_COLUMNS + FROM +
                DataBasePersistence.UserItems.ITEMS_TABLE_NAME + WHERE +
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID + EQUALS +
                QUOTE + itemId + QUOTE;

        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                item = buildItem(cursor);
            }
            cursor.close();
        }

        database.close();

        callback.onSuccess(new GetItem.ResponseValues(item));
    }

    @Override
    public void saveItem(@NonNull SaveItem.RequestValues requestValues,
                         @NonNull UseCase.Callback<SaveItem.ResponseValues> callback) {
        Item item = requestValues.getItem();

        String itemCategory = requestValues.getItemCategory().toString();
        String photo = checkPhotoSize(item);

        if (searchItem(item.getId())) {
            updateItem(item);
            return;
        }

        ContentValues values = new ContentValues();

        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID, item.getId());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_NAME, item.getName());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_IMAGE, photo);
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_SUMMARY, item.getSummary());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LATITUDE, item.getCoordinate().getLatitude());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LONGITUDE, item.getCoordinate().getLongitude());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_FILTER, item.getFilter().toString());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_INFO, item.getInfo());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_DATE, item.getStartDate().getDate());
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_CLASS, itemCategory);


        SQLiteDatabase database = dbHelper.getWritableDatabase();

            if(database.insert(
                    DataBasePersistence.UserItems.ITEMS_TABLE_NAME,
                    null,
                    values) == -1) {
                callback.onError(Constants.SQL_INSERT_ERROR);

            } else {
                callback.onSuccess(new SaveItem.ResponseValues());
            }

            database.close();

    }

    @Override
    public void deleteItems(@NonNull DeleteItems.RequestValues requestValues,
                            @NonNull UseCase.Callback<DeleteItems.ResponseValues> callback) {

        for(String itemId : requestValues.getItemIds()) {

            if(!searchItem(itemId)) {
                continue;
            }

            SQLiteDatabase database = dbHelper.getWritableDatabase();

            if(database.delete(
                    DataBasePersistence.UserItems.ITEMS_TABLE_NAME,
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID + EQUALS + QUOTE + itemId + QUOTE,
                    null) == 0) {
                callback.onError(Constants.SQL_DELETE_ERROR);

            } else {
                callback.onSuccess(new DeleteItems.ResponseValues());
            }

            database.close();
       }
    }

    private String checkPhotoSize(Item item) {
        final int NO_PHOTOS = 0;

        return item.getPhotos().size() > NO_PHOTOS ? item.getPhotos().get(0).getPhoto() : "";
    }

    private void updateItem(@NonNull Item item) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBasePersistence.UserItems.COLUMN_NAME_ITEM_FILTER, item.getFilter().toString());

        database.update(
                DataBasePersistence.UserItems.ITEMS_TABLE_NAME,
                values,
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID + EQUALS + QUOTE + item.getId() + QUOTE,
                null);

        database.close();
    }

    private boolean searchItem(@NonNull String itemId) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String QUERY = SELECT + ALL_COLUMNS + FROM +
                DataBasePersistence.UserItems.ITEMS_TABLE_NAME + WHERE +
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID + EQUALS +
                QUOTE + itemId + QUOTE;

        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    private Item buildItem(Cursor cursor) {

        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(cursor.getString(cursor.getColumnIndex(
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_IMAGE)), ""));

        return new Item.Builder(cursor.getString(cursor.getColumnIndexOrThrow(
                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID)))
                .withName(cursor.getString(cursor.getColumnIndexOrThrow(
                        DataBasePersistence.UserItems.COLUMN_NAME_ITEM_NAME)))
                .withPhotos(photoList)
                .withDescription(cursor.getString(cursor.getColumnIndexOrThrow(
                        DataBasePersistence.UserItems.COLUMN_NAME_ITEM_SUMMARY)))
                .withType(ItemType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
                        DataBasePersistence.UserItems.COLUMN_NAME_ITEM_TYPE))))
                .withCoordinate(new GeoCoordinate(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(
                                DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LONGITUDE))))
                .withFilter(ItemUserFilter.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
                        DataBasePersistence.UserItems.COLUMN_NAME_ITEM_FILTER))))
                .build();
    }
}
