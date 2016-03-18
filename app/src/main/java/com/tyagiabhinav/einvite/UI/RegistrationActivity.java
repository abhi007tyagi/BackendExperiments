package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.einvite.R;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RegistrationFragment registrationFragment = new RegistrationFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        // Add the fragment to the 'registration_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.registration_container, registrationFragment).commit();
    }
}
