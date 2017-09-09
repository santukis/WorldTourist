package com.david.worldtourist.message.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.david.worldtourist.useritems.data.local.DataBaseHelper;
import com.david.worldtourist.useritems.data.local.DataBasePersistence;
import com.david.worldtourist.message.data.boundary.MessageDataSource;

public class MessagesSQLiteDataSource implements MessageDataSource.Local {

    private DataBaseHelper dbHelper;

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String EQUALS = " = ";
    private static final String ALL_COLUMNS = "*";
    private static final String QUOTE = "'";

    public MessagesSQLiteDataSource(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    @Override
    public void saveMessageId(@NonNull String messageId) {

        if (searchId(messageId)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DataBasePersistence.UserMessages.COLUMN_NAME_MESSAGES_IDS, QUOTE + messageId + QUOTE);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.insert(DataBasePersistence.UserMessages.MESSAGES_TABLE_NAME, null, values);
        database.close();
    }

    @Override
    public void deleteMessageId(@NonNull String messageId) {

        if (searchId(messageId)) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            database.delete(DataBasePersistence.UserMessages.MESSAGES_TABLE_NAME,
                    DataBasePersistence.UserMessages.COLUMN_NAME_MESSAGES_IDS +  EQUALS +
                    QUOTE + messageId + QUOTE, null);

            database.close();
        }
    }

    private boolean searchId(@NonNull String messageId) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String QUERY = SELECT + ALL_COLUMNS + FROM +
                DataBasePersistence.UserMessages.MESSAGES_TABLE_NAME + WHERE +
                DataBasePersistence.UserMessages.COLUMN_NAME_MESSAGES_IDS +
                EQUALS + QUOTE + messageId + QUOTE;

        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }
}
