package com.apps.rkanaje.otprelay.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.apps.rkanaje.otprelay.db.RelayUserStore;

import java.util.ArrayList;
import java.util.List;

import static com.apps.rkanaje.otprelay.db.RelayUserTableEntities.*;

public class RelayUserService {

    private final SQLiteDatabase writer;
    private final SQLiteDatabase reader;

    public RelayUserService(Context context) {
        final SQLiteOpenHelper dbHelper = new RelayUserStore(context);
        writer = dbHelper.getWritableDatabase();
        reader = dbHelper.getReadableDatabase();
    }

    public long create(RelayUser relayUser) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, relayUser.getPhone());
        values.put(COLUMN_EMAIL, relayUser.getEmail());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = writer.insert(TABLE_NAME, null, values);
        return newRowId;
    }

    public List<RelayUser> getAllUsers() {
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        final String[] projection = {
                BaseColumns._ID,
                COLUMN_PHONE,
                COLUMN_EMAIL
        };


        final Cursor cursor = reader.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        final List<RelayUser> relayUsers = new ArrayList();
        while (cursor.moveToNext()) {
            final String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            final String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            relayUsers.add(new RelayUser(phone, email));
        }
        cursor.close();
        return relayUsers;
    }

    public RelayUser getByPhone(String phone) {
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_PHONE,
                COLUMN_EMAIL
        };

// Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};

// How you want the results sorted in the resulting Cursor

        Cursor cursor = reader.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        RelayUser relayUser = null;
        while (cursor.moveToNext()) {
            final String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            relayUser = new RelayUser(phone, email);
            break;
        }
        cursor.close();

        return relayUser;

    }


    public boolean contains(String phone) {
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_PHONE,
                COLUMN_EMAIL
        };

// Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};

// How you want the results sorted in the resulting Cursor

        Cursor cursor = reader.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        boolean b = cursor.moveToNext();
        cursor.close();
        return b;
    }

    public int deleteAll() {
        int deleted = writer.delete(TABLE_NAME, null, null);
        return deleted;
    }
}
