package com.tyagiabhinav.backendexperiments.Network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.tyagiabhinav.backend.backendService.BackendService;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backendexperiments.Invite;

import java.io.IOException;

/**
 * Created by abhinavtyagi on 13/03/16.
 */
public class BackgroundService  extends IntentService{

    private static String LOG_TAG = BackgroundService.class.getSimpleName();

    public static final String RECEIVER = "receiver";
    public static final String ACTION = "action";
    public static final String INVITATION = "invite";
    public static final String INVITATION_ID = "inviteId";
    public static final int CREATE_INVITE = 106;
    public static final int GET_INVITE = 108;

    private static final String BASE_URL = "https://javatestbackend.appspot.com/_ah/api";

    private BackendService backendService = null;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public BackgroundService() {
        super(BackgroundService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResponseReceiver receiver = ((Invite)getApplication()).getReceiver();//(ResponseReceiver)intent.getParcelableExtra(RECEIVER);
        int action = intent.getIntExtra(ACTION, 0);

        if(backendService == null) { // Only do this once
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

        switch (action){
            case CREATE_INVITE:
                try {
                    Invitation invitation = ((Invite)getApplication()).getInvitation();//intent.getParcelableExtra(INVITATION);
                    String inviteId = backendService.registerInvitation(invitation).execute().getId();
                    Log.d(LOG_TAG, "Generated Invitation Id --> " + inviteId);
                    Bundle bundle = new Bundle();
                    bundle.putString(INVITATION_ID,inviteId);
                    receiver.send(action,bundle);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case GET_INVITE:
                try {
                    String invitationId = intent.getStringExtra(INVITATION_ID);
                    Invitation invitation = backendService.getInvitation(invitationId).execute();
                    Log.d(LOG_TAG, "Fetched Invitation --> " + invitation.getTitle());
                    receiver.send(action, Bundle.EMPTY);
                    ((Invite)getApplication()).setInvitation(invitation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        }

    }


}
