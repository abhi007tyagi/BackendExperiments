package com.tyagiabhinav.backendexperiments;

import android.app.Application;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backendexperiments.Network.ResponseReceiver;

/**
 * Created by abhinavtyagi on 13/03/16.
 */
public class Invite extends Application {

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
