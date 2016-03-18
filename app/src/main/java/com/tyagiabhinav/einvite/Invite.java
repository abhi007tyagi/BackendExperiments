package com.tyagiabhinav.einvite;

import android.app.Application;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.einvite.Network.ResponseReceiver;
import com.tyagiabhinav.einvite.Util.PrefHelper;

/**
 * Created by abhinavtyagi on 13/03/16.
 */
public class Invite extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefHelper.init(getApplicationContext());
    }

    ResponseReceiver receiver;
    Invitation invitation;


    public ResponseReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(ResponseReceiver receiver) {
        this.receiver = receiver;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }
}