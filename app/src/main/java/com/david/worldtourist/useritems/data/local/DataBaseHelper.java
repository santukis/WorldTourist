package com.david.worldtourist.useritems.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Items.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String CREATE_ITEMS_TABLE =
            "CREATE TABLE " + DataBasePersistence.UserItems.ITEMS_TABLE_NAME + " (" +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_ID + TEXT_TYPE + " PRIMARY KEY," +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_NAME + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_IMAGE + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_SUMMARY + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_TYPE + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LATITUDE + REAL_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_FILTER + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_INFO + TEXT_TYPE + COMMA_SEP +
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_DATE + REAL_TYPE + COMMA_SEP +    //Check the type " TIMESTAMP DEFAULT CURRENT TIME_STAMP"
                    DataBasePersistence.UserItems.COLUMN_NAME_ITEM_CLASS + TEXT_TYPE + " )";

    private static final String CREATE_MESSAGES_TABLE =
            "CREATE TABLE " + DataBasePersistence.UserMessages.MESSAGES_TABLE_NAME + " (" +
                    DataBasePersistence.UserMessages.COLUMN_NAME_MESSAGES_IDS + TEXT_TYPE + " )";


    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ITEMS_TABLE);
        database.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBasePersistence.UserMessages.MESSAGES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DataBasePersistence.UserItems.ITEMS_TABLE_NAME);
            onCreate(db);
        }
    }
}
