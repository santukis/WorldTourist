package com.david.worldtourist.useritems.data.local;

import android.provider.BaseColumns;


public final class DataBasePersistence {

    private DataBasePersistence(){}

    static abstract class UserItems implements BaseColumns{
        static final String ITEMS_TABLE_NAME = "userItems";
        static final String COLUMN_NAME_ITEM_ID = "id";
        static final String COLUMN_NAME_ITEM_NAME = "name";
        static final String COLUMN_NAME_ITEM_IMAGE = "image";
        static final String COLUMN_NAME_ITEM_SUMMARY = "summary";
        static final String COLUMN_NAME_ITEM_TYPE = "type";
        static final String COLUMN_NAME_ITEM_LATITUDE = "latitude";
        static final String COLUMN_NAME_ITEM_LONGITUDE = "longitude";
        static final String COLUMN_NAME_ITEM_DATE = "date";
        static final String COLUMN_NAME_ITEM_INFO = "info";
        static final String COLUMN_NAME_ITEM_FILTER = "filter";
        static final String COLUMN_NAME_ITEM_CLASS = "class";
    }

    public static abstract class UserMessages implements BaseColumns {
        public static final String MESSAGES_TABLE_NAME = "userMessages";
        public static final String COLUMN_NAME_MESSAGES_IDS = "messageIds";
    }
}
