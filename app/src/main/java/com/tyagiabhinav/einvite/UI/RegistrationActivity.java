package com.tyagiabhinav.einvite.UI;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tyagiabhinav.einvite.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private static final String FRAG_TAG = "RegistrationFragment";
    private RegistrationFragment registrationFragment;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null) {
            registrationFragment= new RegistrationFragment();//set tag for fragment
            getSupportFragmentManager().beginTransaction().add(R.id.registration_container, registrationFragment,FRAG_TAG).commit(); // Use tags, it's simpler to deal with

        } else {
            registrationFragment= (RegistrationFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }
//        RegistrationFragment registrationFragment = new RegistrationFragment();
//
//        // In case this activity was started with special instructions from an
//        // Intent, pass the Intent's extras to the fragment as arguments
//
//        // Add the fragment to the 'registration_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.registration_container, registrationFragment).commit();
    }
}
