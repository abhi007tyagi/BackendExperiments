package com.tyagiabhinav.einvite.DB;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "invite.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold invitation details.
        final String SQL_CREATE_INVITATION_TABLE = "CREATE TABLE " + DBContract.InviteEntry.TABLE_NAME + " (" +
                DBContract.InviteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.InviteEntry.COL_ID + " TEXT UNIQUE NOT NULL," +
                DBContract.InviteEntry.COL_TITLE + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_TYPE + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_IMG + " BLOB, " +
                DBContract.InviteEntry.COL_MESSAGE + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_TIME + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_DATE + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_WEBSITE + " TEXT, " +
                DBContract.InviteEntry.COL_VENUE_NAME + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_VENUE_CONTACT + " TEXT, " +
                DBContract.InviteEntry.COL_VENUE_ADDRESS + " TEXT NOT NULL, " +
                DBContract.InviteEntry.COL_PLACE_ID + " TEXT, " +
                DBContract.InviteEntry.COL_VENUE_LATITUDE + " TEXT, " +
                DBContract.InviteEntry.COL_VENUE_LONGITUDE + " TEXT, " +
                DBContract.InviteEntry.COL_INVITEE + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                DBContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.UserEntry.COL_USER_NAME + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                DBContract.UserEntry.COL_USER_CONTACT + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_COUNTRY + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_STATE + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_ADD1 + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_ADD2 + " TEXT, " +
                DBContract.UserEntry.COL_USER_CITY + " TEXT NOT NULL, " +
                DBContract.UserEntry.COL_USER_ZIP + " TEXT, " +
                DBContract.UserEntry.COL_USER_TYPE + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_PLACE_TABLE = "CREATE TABLE " + DBContract.PlaceEntry.TABLE_NAME + " (" +
                DBContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.PlaceEntry.COL_PLACE_ID + " TEXT UNIQUE NOT NULL, " +
                DBContract.PlaceEntry.COL_PLACE_NAME + " TEXT, " +
                DBContract.PlaceEntry.COL_PLACE_CONTACT + " TEXT, " +
                DBContract.PlaceEntry.COL_PLACE_WEBSITE + " TEXT, " +
                DBContract.PlaceEntry.COL_PLACE_ADDRESS + " TEXT, " +
                DBContract.PlaceEntry.COL_PLACE_LATITUDE + " TEXT NOT NULL, " +
                DBContract.PlaceEntry.COL_PLACE_LONGITUDE + " TEXT NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_INVITATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PLACE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.InviteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.PlaceEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}