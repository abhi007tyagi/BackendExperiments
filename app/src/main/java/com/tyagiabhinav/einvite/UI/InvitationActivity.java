package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tyagiabhinav.einvite.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by abhinavtyagi on 20/03/16.
 */
public class InvitationActivity extends AppCompatActivity {
    private static final String LOG_TAG = InvitationActivity.class.getSimpleName();
    private static final String FRAG_TAG = "InvitationFragment";

    private InvitationFragment invitationFragment;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
