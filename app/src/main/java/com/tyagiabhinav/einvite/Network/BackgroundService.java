package com.tyagiabhinav.einvite.Network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.tyagiabhinav.backend.backendService.BackendService;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Util;

import java.io.IOException;

/**
 * Created by abhinavtyagi on 13/03/16.
 */
public class BackgroundService extends IntentService {

    private static String LOG_TAG = BackgroundService.class.getSimpleName();

    public static final String RECEIVER = "receiver";
    public static final String ACTION = "action";
    public static final String INVITATION = "invite";
    public static final String INVITATION_ID = "inviteId";
    public static final String ERROR = "error";
    public static final int CREATE_INVITE = 106;
    public static final int CREATE_INVITE_ERR = 105;
    public static final int GET_INVITE = 108;
    public static final int GET_INVITE_ERR = 107;

    private static final String BASE_URL = "https://javatestbackend.appspot.com/_ah/api";

    private BackendService backendService = null;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BackgroundService() {
        super(BackgroundService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResponseReceiver receiver = ((Invite) getApplication()).getReceiver();//(ResponseReceiver)intent.getParcelableExtra(RECEIVER);
        int action = intent.getIntExtra(ACTION, 0);

        if (backendService == null) { // Only do this once
            BackendService.Builder builder = new BackendService.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(BASE_URL)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            backendService = builder.build();

            switch (action) {
                case CREATE_INVITE:
                    try {
                        Invitation invitation = ((Invite) getApplication()).getInvitation();//intent.getParcelableExtra(INVITATION);
                        String inviteId = backendService.registerInvitation(invitation).execute().getId();
                        Log.d(LOG_TAG, "Generated Invitation Id --> " + inviteId);
                        invitation.setId(inviteId);
//                    saveToDB(invitation, true);
                        Bundle bundle = new Bundle();
                        bundle.putString(INVITATION_ID, inviteId);
                        receiver.send(action, bundle);
                    } catch (IOException e) {
//                        e.printStackTrace();
                        Log.d(LOG_TAG, "Generated Invitation Id --> Error" + e.getMessage());
                        Bundle bundle = new Bundle();
                        if(e instanceof GoogleJsonResponseException) {
                            bundle.putString(ERROR, ((GoogleJsonResponseException) e).getDetails().getMessage());
                        } else{
                            bundle.putString(ERROR, getResources().getString(R.string.ServiceError));
                        }
                        receiver.send(CREATE_INVITE_ERR, bundle);
                    }
                    break;
                case GET_INVITE:
                    try {
                        String invitationId = intent.getStringExtra(INVITATION_ID);
                        if(!Util.isNull(invitationId)) {
                            Invitation invitation = backendService.getInvitation(invitationId).execute();
                            Log.d(LOG_TAG, "Fetched Invitation --> " + invitation.getTitle());
                            receiver.send(action, Bundle.EMPTY);
//                    saveToDB(invitation, false);
                            ((Invite) getApplication()).setInvitation(invitation);
                        } else{
                            Bundle bundle = new Bundle();
                            bundle.putString(ERROR,"ERROR: Empty Invitation ID!!");
                            receiver.send(GET_INVITE_ERR, bundle);
                        }
                    } catch (IOException e) {
//                        e.printStackTrace();
//                        JSONObject json = new JSONObject(e.getMessage())
                        Log.d(LOG_TAG, "Fetched Invitation --> Error" + ((GoogleJsonResponseException) e).getDetails().getMessage());
                        Bundle bundle = new Bundle();
                        if(e instanceof GoogleJsonResponseException) {
                            bundle.putString(ERROR, ((GoogleJsonResponseException) e).getDetails().getMessage());
                        } else{
                            bundle.putString(ERROR, getResources().getString(R.string.ServiceError));
                        }
                        receiver.send(GET_INVITE_ERR, bundle);
                    }
                    break;
            }

        }

    }

//    private void saveToDB(Invitation invitation, boolean isSelf){
//        Log.d(LOG_TAG, "Save to DB");
//        ContentValues inviteValues = new ContentValues();
//        inviteValues.put(InviteEntry.COL_ID, invitation.getId());
//        inviteValues.put(InviteEntry.COL_TITLE, invitation.getTitle());
//        inviteValues.put(InviteEntry.COL_TYPE, invitation.getType());
//        inviteValues.put(InviteEntry.COL_MESSAGE, invitation.getMessage());
//        inviteValues.put(InviteEntry.COL_TIME, invitation.getTime());
//        inviteValues.put(InviteEntry.COL_DATE, invitation.getDate());
//        inviteValues.put(InviteEntry.COL_WEBSITE, invitation.getWebsite());
//        inviteValues.put(InviteEntry.COL_VENUE_NAME, invitation.getVenueName());
//        inviteValues.put(InviteEntry.COL_VENUE_EMAIL, invitation.getVenueEmail());
//        inviteValues.put(InviteEntry.COL_VENUE_CONTACT, invitation.getVenueContact());
//        inviteValues.put(InviteEntry.COL_VENUE_ADD1, invitation.getVenueAdd1());
//        inviteValues.put(InviteEntry.COL_VENUE_ADD2, invitation.getVenueAdd2());
//        inviteValues.put(InviteEntry.COL_VENUE_CITY, invitation.getVenueCity());
//        inviteValues.put(InviteEntry.COL_VENUE_STATE, invitation.getVenueState());
//        inviteValues.put(InviteEntry.COL_VENUE_COUNTRY, invitation.getVenueCountry());
//        inviteValues.put(InviteEntry.COL_VENUE_ZIP, invitation.getVenueZip());
//        inviteValues.put(InviteEntry.COL_VENUE_LATITUDE, invitation.getLatitude());
//        inviteValues.put(InviteEntry.COL_VENUE_LONGITUDE, invitation.getLongitude());
//
//        ContentValues userValues = new ContentValues();
//        userValues.put(UserEntry.COL_USER_EMAIL, invitation.getVenueEmail());
//        userValues.put(UserEntry.COL_USER_NAME, invitation.getVenueName());
//        userValues.put(UserEntry.COL_USER_CONTACT, invitation.getVenueContact());
//        userValues.put(UserEntry.COL_USER_ADD1, invitation.getVenueAdd1());
//        userValues.put(UserEntry.COL_USER_ADD2, invitation.getVenueAdd2());
//        userValues.put(UserEntry.COL_USER_CITY, invitation.getVenueCity());
//        userValues.put(UserEntry.COL_USER_STATE, invitation.getVenueState());
//        userValues.put(UserEntry.COL_USER_COUNTRY, invitation.getVenueCountry());
//        userValues.put(UserEntry.COL_USER_ZIP, invitation.getVenueZip());
//        if(isSelf){
//            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_SELF);
//        }else{
//            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_OTHER);
//        }
//
//
//        //save to db
//        getApplication().getContentResolver().insert(InviteEntry.CONTENT_URI, inviteValues);
//        getApplication().getContentResolver().insert(UserEntry.CONTENT_URI, userValues);
//    }


}
