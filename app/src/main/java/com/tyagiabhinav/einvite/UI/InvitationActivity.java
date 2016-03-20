package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.einvite.R;

/**
 * Created by abhinavtyagi on 20/03/16.
 */
public class InvitationActivity extends AppCompatActivity {
    private static final String LOG_TAG = InvitationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        InvitationFragment invitationFragment = new InvitationFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        invitationFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'invitation_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.invitation_container, invitationFragment).commit();


    }
}
