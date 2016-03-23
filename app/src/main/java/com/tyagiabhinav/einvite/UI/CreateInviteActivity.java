package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.einvite.R;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateInviteActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private static final String FRAG_TAG = "CreateInviteFragment";
    private CreateInviteFragment createInviteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invite);

        if(savedInstanceState == null) {
            createInviteFragment= new CreateInviteFragment();//set tag for fragment
            getSupportFragmentManager().beginTransaction().add(R.id.create_invite_container, createInviteFragment,FRAG_TAG).commit(); // Use tags, it's simpler to deal with

        } else {
            createInviteFragment= (CreateInviteFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }

//        CreateInviteFragment createInviteFragment = new CreateInviteFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        // Add the fragment to the 'create_invite_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.create_invite_container, createInviteFragment).commit();
    }

    public void showNextScreen() {


//        CreateVenueFragment createVenueFragment = new CreateVenueFragment();
//
//        // In case this activity was started with special instructions from an
//        // Intent, pass the Intent's extras to the fragment as arguments
//
//        // Add the fragment to the 'create_invite_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.create_invite_container, createVenueFragment).commit();
            CreateVenueFragment createVenueFragment = new CreateVenueFragment();
//        Bundle args = new Bundle();
//        args.putInt(ArticleFragment.ARG_POSITION, position);
//        newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.create_invite_container, createVenueFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
    }

}
