package com.tyagiabhinav.einvite.DB;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class DBProvider extends ContentProvider {
    private static final String LOG_TAG = DBProvider.class.getSimpleName();

    private DBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int INVITE = 100;
    static final int INVITE_COLUMN = 101;
    static final int USER = 300;
    static final int USER_COLUMN = 301;

    private static final SQLiteQueryBuilder sUserQueryBuilder, sInvitationQueryBuilder;

    static {
        sUserQueryBuilder = new SQLiteQueryBuilder();

        /// JOIN query for favorite movies...
        // SELECT * FROM user INNER JOIN invite ON invite.invitee = user.email;
        sUserQueryBuilder.setTables(
                DBContract.UserEntry.TABLE_NAME + " INNER JOIN " +
                        DBContract.InviteEntry.TABLE_NAME +
                        " ON " + DBContract.UserEntry.TABLE_NAME +
                        "." + DBContract.UserEntry.COL_USER_EMAIL +
                        " = " + DBContract.InviteEntry.TABLE_NAME +
                        "." + DBContract.InviteEntry.COL_INVITEE);
    }

    static {
        sInvitationQueryBuilder = new SQLiteQueryBuilder();

        /// JOIN query for favorite movies...
        // SELECT * FROM invite, user WHERE invite.invitee = user.email;
        sUserQueryBuilder.setTables(
                DBContract.UserEntry.TABLE_NAME + " INNER JOIN " +
                        DBContract.InviteEntry.TABLE_NAME +
                        " ON " + DBContract.UserEntry.TABLE_NAME +
                        "." + DBContract.UserEntry.COL_USER_EMAIL +
                        " = " + DBContract.InviteEntry.TABLE_NAME +
                        "." + DBContract.InviteEntry.COL_INVITEE);
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DBContract.PATH_INVITATION, INVITE);
        matcher.addURI(authority, DBContract.PATH_INVITATION + "/*", INVITE_COLUMN);
        matcher.addURI(authority, DBContract.PATH_USER, USER);
        matcher.addURI(authority, DBContract.PATH_USER + "/*", USER_COLUMN);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query -->" + uri);
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "invite/*"
            case INVITE_COLUMN: {
                Log.d(LOG_TAG, "INVITATION");
                String inviteId = DBContract.InviteEntry.getInviteIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.InviteEntry.TABLE_NAME + ", " + DBContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        new String[]{inviteId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "user/*"
            case USER_COLUMN: {
                Log.d(LOG_TAG, "USER");
                String id = DBContract.UserEntry.getUserIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "user"
            case USER: {
                Log.d(LOG_TAG, "USER");
                retCursor = sUserQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            // "invite"
            case INVITE: {
                Log.d(LOG_TAG, "INVITE");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.InviteEntry.TABLE_NAME + ", " + DBContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType -->" + uri);
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case INVITE:
                return DBContract.InviteEntry.CONTENT_TYPE;
            case INVITE_COLUMN:
                return DBContract.InviteEntry.CONTENT_ITEM_TYPE;
            case USER:
                return DBContract.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case INVITE: {
                try {
                    long _id = db.insertWithOnConflict(DBContract.InviteEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                    if (_id > 0) {
                        Log.d(LOG_TAG, _id+" Invite rows inserted");
                        returnUri = DBContract.InviteEntry.buildInviteUri();
                    }
                    else {
                        Log.d(LOG_TAG, "no Invite row inserted");
                        returnUri = null;
//                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception-->"+e.getMessage());
                }
                break;
            }
            case USER: {
                try {
                    long _id = db.insertWithOnConflict(DBContract.UserEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                    if (_id > 0) {
                        Log.d(LOG_TAG, _id+" User rows inserted");
                        returnUri = DBContract.UserEntry.buildUserUri();
                    }
                    else {
                        returnUri = null;
                        Log.d(LOG_TAG, "no User row inserted");
//                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception-->" + e.getMessage());
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case INVITE:
                rowsDeleted = db.delete(DBContract.InviteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(DBContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case INVITE:
                rowsUpdated = db.update(DBContract.InviteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(DBContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
