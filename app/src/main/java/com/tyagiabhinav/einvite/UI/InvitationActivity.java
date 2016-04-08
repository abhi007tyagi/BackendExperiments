package com.tyagiabhinav.einvite.UI;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tyagiabhinav.einvite.R;

/**
 * Created by abhinavtyagi on 20/03/16.
 */
public class InvitationActivity extends AppCompatActivity {
    private static final String LOG_TAG = InvitationActivity.class.getSimpleName();
    private static final String FRAG_TAG = "InvitationFragment";

    private InvitationFragment invitationFragment;

//    @Bind(R.id.toolbar)
//    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.colorPrimaryDarkAlpha));//android.R.color.transparent));
        }
        setContentView(R.layout.activity_invitation);


//        ButterKnife.bind(this);

//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        if(savedInstanceState == null) {
            invitationFragment= new InvitationFragment();//set tag for fragment
            invitationFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.invitation_container, invitationFragment,FRAG_TAG).commit(); // Use tags, it's simpler to deal with

        } else {
            invitationFragment= (InvitationFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
//            invitationFragment.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction().replace(R.id.invitation_container, invitationFragment,FRAG_TAG).commit(); // Use tags, it's simpler to deal with

        }
//        InvitationFragment invitationFragment = new InvitationFragment();
//
//        // In case this activity was started with special instructions from an
//        // Intent, pass the Intent's extras to the fragment as arguments
//        invitationFragment.setArguments(getIntent().getExtras());
//
//        // Add the fragment to the 'invitation_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.invitation_container, invitationFragment).commit();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        recreate();
//    }
}
