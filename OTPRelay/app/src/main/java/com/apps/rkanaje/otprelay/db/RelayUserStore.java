package com.apps.rkanaje.otprelay.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import static com.apps.rkanaje.otprelay.db.RelayUserTableEntities.*;

public class RelayUserStore extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                    "%s TEXT, " +
                    "%s TEXT)", TABLE_NAME, _ID, COLUMN_PHONE, COLUMN_EMAIL);

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "OTPRelayUser.db";

    public RelayUserStore(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO
    }


}
