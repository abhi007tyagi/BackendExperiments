package com.tyagiabhinav.backendexperiments.DB;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class DBContract {

    public static final String CONTENT_AUTHORITY = "com.tyagiabhinav.backendexperiments";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVITATION = "invite";
    public static final String PATH_USER = "user";

    public static final class InviteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INVITATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVITATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVITATION;

        public static final String TABLE_NAME = "invitation";

        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";
        public static final String COL_TYPE = "type";
        public static final String COL_MESSAGE = "message";
        public static final String COL_TIME = "time";
        public static final String COL_DATE = "date";
        public static final String COL_WEBSITE = "website";
        public static final String COL_VENUE_NAME = "v_name";
        public static final String COL_VENUE_EMAIL = "v_email";
        public static final String COL_VENUE_CONTACT = "v_contact";
        public static final String COL_VENUE_ADD1 = "v_add1";
        public static final String COL_VENUE_ADD2 = "v_add2";
        public static final String COL_VENUE_CITY = "v_city";
        public static final String COL_VENUE_STATE = "v_state";
        public static final String COL_VENUE_COUNTRY = "v_country";
        public static final String COL_VENUE_ZIP = "v_zip";
        public static final String COL_VENUE_LATITUDE = "v_lat";
        public static final String COL_VENUE_LONGITUDE = "v_lon";
        public static final String COL_INVITEE = "invitee";


        public static Uri buildInviteUri() {
            return CONTENT_URI;
        }

        public static Uri buildInvitationDataUri(String inviteId) {
            return CONTENT_URI.buildUpon().appendPath(inviteId).build();
        }

        public static String getInviteIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COL_USER_EMAIL = "email";
        public static final String COL_USER_NAME = "name";
        public static final String COL_USER_CONTACT = "contact";
        public static final String COL_USER_ADD1 = "add1";
        public static final String COL_USER_ADD2 = "add2";
        public static final String COL_USER_CITY = "city";
        public static final String COL_USER_STATE = "state";
        public static final String COL_USER_COUNTRY = "country";
        public static final String COL_USER_ZIP = "zip";
        public static final String COL_USER_TYPE = "type";

        public static final String USER_TYPE_SELF = "self";
        public static final String USER_TYPE_OTHER = "other";


        public static Uri buildUserUri() {
            return CONTENT_URI;
        }

        public static Uri buildUserDataUri(String userEmail) {
            return CONTENT_URI.buildUpon().appendPath(userEmail).build();
        }

        public static String getUserIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
