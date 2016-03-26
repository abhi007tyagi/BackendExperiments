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
import android.util.Log;

import com.tyagiabhinav.einvite.R;

public class MainActivity extends AppCompatActivity {//} implements ResponseReceiver.Receiver{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String FRAG_TAG = "HomeFragment";
    private HomeFragment homeFragment;
    private String inviteID = "";
//    private int CURSOR_LOADER = 1006;

//    TextView dataView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG,"Screen-->"+getString(R.string.device_screen));

        Bundle bundleWidget = getIntent().getExtras();
        if(bundleWidget != null) {
            inviteID = bundleWidget.getString(InvitationFragment.INVITATION_ID);
        }

        String einviteDeepLink = getIntent().getDataString();
        if(einviteDeepLink != null){
            Log.d(LOG_TAG, "Deeplink Uri -->"+einviteDeepLink);
            inviteID = einviteDeepLink.split("://")[1];

        }else{
            Log.d(LOG_TAG, "No Deeplink Uri");
        }

        if(savedInstanceState == null) {
            homeFragment= new HomeFragment();//set tag for fragment
            Bundle bundle = new Bundle();
            bundle.putString(InvitationFragment.INVITATION_ID, inviteID);
            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.home_container, homeFragment,FRAG_TAG).commit(); // Use tags, it's simpler to deal with

        } else {
            homeFragment= (HomeFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }

//        HomeFragment homeFragment = new HomeFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
//        posterFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'home_container' FrameLayout
//        getSupportFragmentManager().beginTransaction().add(R.id.home_container, homeFragment).commit();

        /*User user = new User();
        user.setName("YES YES");
        user.setEmail("yes@yes.com");
        user.setContact("9876543210");
        user.setCountry("India");
        user.setState("Delhi");
        user.setCity("New Delhi");
        user.setAdd1("Faridabad");
        user.setAdd2("Near Oxford School");
        user.setZip("110018");

        Invitation invite = new Invitation();
        invite.setInvitee(user);
        invite.setTitle("No Day");
        invite.setType("B'Day");
        invite.setTime("7:00pm");
        invite.setDate("2016/09/27");
        invite.setMessage("Aaa jao and attend my b'day... :)");
        invite.setVenueName("Taj Palace Hotel");
        invite.setVenueEmail("taj@taj.com");
        invite.setVenueContact("9876543210");
        invite.setVenueCountry("India");
        invite.setVenueState("Delhi");
        invite.setVenueCity("New Delhi");
        invite.setVenueAdd1("Manshingh Road");
        invite.setVenueAdd2("Near India Gate");
        invite.setVenueZip("110001");
        invite.setWebsite("tyagiabhinav.com");

        ((Invite) getApplication()).setInvitation(invite);

        ResponseReceiver receiver = new ResponseReceiver(new Handler());
        receiver.setReceiver(this);

        ((Invite) getApplication()).setReceiver(receiver);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
        intent.putExtra(BackgroundService.ACTION, BackgroundService.CREATE_INVITE);
        startService(intent);
*/
//        dataView = (TextView)findViewById(R.id.data);
//        new EndpointsAsyncTask(this).execute();
    }

//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        Invitation invite = ((Invite) getApplication()).getInvitation();
//        switch (resultCode) {
//            case BackgroundService.GET_INVITE:
//                Toast.makeText(getApplicationContext(), "Invitation: " + invite.getTitle() + " received !!", Toast.LENGTH_LONG).show();
////                saveToDB(invite, false);
//                break;
//            case BackgroundService.CREATE_INVITE:
//                String id = resultData.getString(BackgroundService.INVITATION_ID);
//                Toast.makeText(getApplicationContext(), "ID: " + id, Toast.LENGTH_LONG).show();
//                invite.setId(id);
//                saveToDB(invite, true);
//                Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
//                intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
//                intent.putExtra(BackgroundService.INVITATION_ID, id);
//                startService(intent);
//                break;
//        }
//    }
//
//    private void saveToDB(Invitation invitation, boolean isSelf) {
//        ContentValues inviteValues = new ContentValues();
//        inviteValues.put(DBContract.InviteEntry.COL_ID, invitation.getId());
//        inviteValues.put(DBContract.InviteEntry.COL_TITLE, invitation.getTitle());
//        inviteValues.put(DBContract.InviteEntry.COL_TYPE, invitation.getType());
//        inviteValues.put(DBContract.InviteEntry.COL_MESSAGE, invitation.getMessage());
//        inviteValues.put(DBContract.InviteEntry.COL_TIME, invitation.getTime());
//        inviteValues.put(DBContract.InviteEntry.COL_DATE, invitation.getDate());
//        inviteValues.put(DBContract.InviteEntry.COL_WEBSITE, invitation.getWebsite());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_NAME, invitation.getVenueName());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_EMAIL, invitation.getVenueEmail());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_CONTACT, invitation.getVenueContact());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_ADD1, invitation.getVenueAdd1());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_ADD2, invitation.getVenueAdd2());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_CITY, invitation.getVenueCity());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_STATE, invitation.getVenueState());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_COUNTRY, invitation.getVenueCountry());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_ZIP, invitation.getVenueZip());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_LATITUDE, invitation.getLatitude());
//        inviteValues.put(DBContract.InviteEntry.COL_VENUE_LONGITUDE, invitation.getLongitude());
//
//        User user = invitation.getInvitee();
//        inviteValues.put(DBContract.InviteEntry.COL_INVITEE, user.getEmail());
//
//        ContentValues userValues = new ContentValues();
//        userValues.put(DBContract.UserEntry.COL_USER_EMAIL, user.getEmail());
//        userValues.put(DBContract.UserEntry.COL_USER_NAME, user.getName());
//        userValues.put(DBContract.UserEntry.COL_USER_CONTACT, user.getContact());
//        userValues.put(DBContract.UserEntry.COL_USER_ADD1, user.getAdd1());
//        userValues.put(DBContract.UserEntry.COL_USER_ADD2, user.getAdd2());
//        userValues.put(DBContract.UserEntry.COL_USER_CITY, user.getCity());
//        userValues.put(DBContract.UserEntry.COL_USER_STATE, user.getState());
//        userValues.put(DBContract.UserEntry.COL_USER_COUNTRY, user.getCountry());
//        userValues.put(DBContract.UserEntry.COL_USER_ZIP, user.getZip());
//        if (isSelf) {
//            userValues.put(DBContract.UserEntry.COL_USER_TYPE, DBContract.UserEntry.USER_TYPE_SELF);
//        } else {
//            userValues.put(DBContract.UserEntry.COL_USER_TYPE, DBContract.UserEntry.USER_TYPE_OTHER);
//        }
//
//
//        //save to db
//        getApplication().getContentResolver().insert(DBContract.InviteEntry.CONTENT_URI, inviteValues);
//        getApplication().getContentResolver().insert(DBContract.UserEntry.CONTENT_URI, userValues);
//    }


//    class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Invitation>> {
//        private BackendService backendService = null;
//        private Context context;
//
//        EndpointsAsyncTask(Context context) {
//            this.context = context;
//            dataView.setText("Fetching data...");
//        }
//
//        @Override
//        protected List<Invitation> doInBackground(Void... params) {
//            if(backendService == null) { // Only do this once
//                BackendService.Builder builder = new BackendService.Builder(AndroidHttp.newCompatibleTransport(),
//                        new AndroidJsonFactory(), null)
//                        .setRootUrl("https://javatestbackend.appspot.com/_ah/api")
//                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                            @Override
//                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                abstractGoogleClientRequest.setDisableGZipContent(true);
//                            }
//                        });
//                backendService = builder.build();
//            }
//
//            try {
//                return backendService.listInvitations().execute().getItems();
//            } catch (IOException e) {
//                return Collections.EMPTY_LIST;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<Invitation> result) {
//            StringBuilder data = new StringBuilder();
//            for (Invitation user : result) {
////                data.append("Name: "+user.getName()+"\n");
////                data.append("Address: "+user.getAddress()+"\n");
////                data.append("Contact1: "+user.getContact1()+"\n");
////                data.append("Contact2: "+user.getContact2()+"\n");
//                data.append("\n\n");
//            }
//            dataView.setText(data.toString());
//        }
//    }
}
