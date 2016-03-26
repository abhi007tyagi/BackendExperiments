package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;

import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateInviteActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private static final String FRAG_TAG_1 = "CreateInviteFragment";
    private static final String FRAG_TAG_2 = "CreateVenueFragment";
    private CreateInviteFragment createInviteFragment;
    private CreateVenueFragment createVenueFragment;
    Bundle savedInstanceState;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invite);

        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.savedInstanceState = savedInstanceState;
        if (this.savedInstanceState == null) {
            Log.d(LOG_TAG, "onCreate... saved-> null");
            createInviteFragment = new CreateInviteFragment();//set tag for fragment
            getSupportFragmentManager().beginTransaction().add(R.id.create_invite_container, createInviteFragment, FRAG_TAG_1).commit(); // Use tags, it's simpler to deal with

        } else {
            Log.d(LOG_TAG, "onCreate... saved-> not null");
            createInviteFragment = (CreateInviteFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_1);
        }

//        CreateInviteFragment createInviteFragment = new CreateInviteFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        // Add the fragment to the 'create_invite_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.create_invite_container, createInviteFragment).commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed()");
        if(toolbar != null){
            String title = toolbar.getTitle().toString();
            if(title.equalsIgnoreCase(getString(R.string.create_venue_title))){
                ((Invite)getApplication()).setCreateVenueBackPressed(true);
            }else if(title.equalsIgnoreCase(getString(R.string.create_invite_title))){
                ((Invite)getApplication()).setCreateVenueBackPressed(false);
            }
        }
        super.onBackPressed();
    }

    public void setScreenTitle(String title){
        Log.d(LOG_TAG,"set title");
        if(toolbar != null){
            toolbar.setTitle(title);
        }
    }

    public void showNextScreen() {

        if (this.savedInstanceState == null) {
            Log.d(LOG_TAG, "showNextScreen... saved-> null");
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
        } else {
            Log.d(LOG_TAG, "showNextScreen... saved-> not null");
            createVenueFragment = (CreateVenueFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_2);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.create_invite_container, createVenueFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    public void animateFab(Fragment createInvite, View sharedView) {

        createInvite.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_fab_transform));
        createInvite.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));

        if (this.savedInstanceState == null) {
            Log.d(LOG_TAG, "animateFab... saved-> null");
            createVenueFragment = new CreateVenueFragment();
        }else{
            Log.d(LOG_TAG, "animateFab... saved-> not null");
            createVenueFragment = (CreateVenueFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_2);
        }
        createVenueFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_fab_transform));
        createVenueFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.create_invite_container, createVenueFragment)
                .addToBackStack(null)
                .addSharedElement(sharedView, getString(R.string.shared_string_name) )
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        createVenueFragment = (CreateVenueFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_2);
//        getSupportFragmentManager().saveFragmentInstanceState(createVenueFragment);
//        super.onBackPressed();
//    }
}
