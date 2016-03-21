package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.einvite.R;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private static final String FRAG_TAG = "RegistrationFragment";
    private RegistrationFragment registrationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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
