package com.tyagiabhinav.einvite.Util;

import android.content.ContentValues;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract.InviteEntry;
import com.tyagiabhinav.einvite.DB.DBContract.UserEntry;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class Util {

    public static ContentValues getUserValues(User user, boolean isSelf) {
        ContentValues userValues = new ContentValues();
        userValues.put(UserEntry.COL_USER_EMAIL, user.getEmail());
        userValues.put(UserEntry.COL_USER_NAME, user.getName());
        userValues.put(UserEntry.COL_USER_CONTACT, user.getContact());
        userValues.put(UserEntry.COL_USER_ADD1, user.getAdd1());
        userValues.put(UserEntry.COL_USER_ADD2, user.getAdd2());
        userValues.put(UserEntry.COL_USER_CITY, user.getCity());
        userValues.put(UserEntry.COL_USER_STATE, user.getState());
        userValues.put(UserEntry.COL_USER_COUNTRY, user.getCountry());
        userValues.put(UserEntry.COL_USER_ZIP, user.getZip());
        if (isSelf) {
            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_SELF);
        } else {
            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_OTHER);
        }

        return userValues;
    }

    public static ContentValues getInvitationValues(Invitation invitation) {
        ContentValues inviteValues = new ContentValues();
        inviteValues.put(InviteEntry.COL_ID, invitation.getId());
        inviteValues.put(InviteEntry.COL_TITLE, invitation.getTitle());
        inviteValues.put(InviteEntry.COL_TYPE, invitation.getType());
        inviteValues.put(InviteEntry.COL_MESSAGE, invitation.getMessage());
        inviteValues.put(InviteEntry.COL_TIME, invitation.getTime());
        inviteValues.put(InviteEntry.COL_DATE, invitation.getDate());
        inviteValues.put(InviteEntry.COL_WEBSITE, invitation.getWebsite());
        inviteValues.put(InviteEntry.COL_VENUE_NAME, invitation.getVenueName());
        inviteValues.put(InviteEntry.COL_VENUE_EMAIL, invitation.getVenueEmail());
        inviteValues.put(InviteEntry.COL_VENUE_CONTACT, invitation.getVenueContact());
        inviteValues.put(InviteEntry.COL_VENUE_ADD1, invitation.getVenueAdd1());
        inviteValues.put(InviteEntry.COL_VENUE_ADD2, invitation.getVenueAdd2());
        inviteValues.put(InviteEntry.COL_VENUE_CITY, invitation.getVenueCity());
        inviteValues.put(InviteEntry.COL_VENUE_STATE, invitation.getVenueState());
        inviteValues.put(InviteEntry.COL_VENUE_COUNTRY, invitation.getVenueCountry());
        inviteValues.put(InviteEntry.COL_VENUE_ZIP, invitation.getVenueZip());
        inviteValues.put(InviteEntry.COL_VENUE_LATITUDE, invitation.getLatitude());
        inviteValues.put(InviteEntry.COL_VENUE_LONGITUDE, invitation.getLongitude());
        inviteValues.put(InviteEntry.COL_INVITEE, invitation.getInvitee().getEmail());

        return inviteValues;
    }
}
