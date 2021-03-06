package com.tyagiabhinav.einvite.UI;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract;
import com.tyagiabhinav.einvite.DB.DBContract.PlaceEntry;
import com.tyagiabhinav.einvite.DB.DBContract.UserEntry;
import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.Network.BackgroundService;
import com.tyagiabhinav.einvite.Network.ResponseReceiver;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.PrefHelper;
import com.tyagiabhinav.einvite.Util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateVenueFragment extends Fragment implements Validator.ValidationListener, LoaderManager.LoaderCallbacks<Cursor>, ResponseReceiver.Receiver {
    private static final String LOG_TAG = CreateVenueFragment.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 7;

    private static final int CURSOR_LOADER = 205;
    private static final int CURSOR_SPINNER_LOADER = 207;
    private static final int PLACE_PICKER_REQUEST = 106;
    private static final int REGISTRATION_REQUEST = 108;

//    private static final String V_NAME = "venue";
//    private static final String V_PHONE = "phone";
//    private static final String V_WEB = "website";
//    private static final String V_ADD = "add";
//    private static final String V_LAT = "lat";
//    private static final String V_LON = "lon";
//    private static final String V_PLACE_ID = "placeId";

    private View rootView;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;

    @Bind(R.id.savedPalces)
    Spinner savedPlaces;

    @NotEmpty
    @Bind(R.id.venueName)
    EditText name;

    @Bind(R.id.venuePhone)
    EditText phone;
    //    @Url
    @Bind(R.id.website)
    EditText website;

    @NotEmpty
    @Bind(R.id.address)
    EditText address;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private String latitude, longitude, placeId;
    private Invitation invite;
    private Validator validator;
    private Loader<Cursor> loader;
    private SimpleCursorAdapter spinAdapter;

    private boolean isItemSelected = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.create_venue_fragment, container, false);
        //reset back button flag
        ((Invite) getActivity().getApplication()).setCreateVenueBackPressed(false);

        ButterKnife.bind(this, rootView);
        validator = new Validator(this);
        validator.setValidationListener(this);

        //populate if not empty/null
        invite = ((Invite) getActivity().getApplication()).getInvitation();
        if (!Util.isNull(invite.getVenueName())) {
            name.setText(invite.getVenueName());
        }
        if (!Util.isNull(invite.getVenueContact())) {
            phone.setText(invite.getVenueContact());
        }
        if (!Util.isNull(invite.getWebsite())) {
            website.setText(invite.getWebsite());
        }
        if (!Util.isNull(invite.getVenueAddress())) {
            address.setText(invite.getVenueAddress());
        }
        if (!Util.isNull(invite.getLatitude())) {
            latitude = invite.getLatitude();
        }
        if (!Util.isNull(invite.getLongitude())) {
            longitude = invite.getLongitude();
        }
        if (!Util.isNull(invite.getPlaceID())) {
            placeId = invite.getPlaceID();
        }

        spinAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item, null,
                new String[]{PlaceEntry.COL_PLACE_NAME},
                new int[] {android.R.id.text1}, 0);

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        savedPlaces.setAdapter(spinAdapter);

//        savedPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Log.d(LOG_TAG, "selected position --> " + position);
//                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                if (cursor != null) {
//                    name.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_NAME)));
//                    phone.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_CONTACT)));
//                    website.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_WEBSITE)));
//                    address.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ADDRESS)));
//                    placeId = cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ID));
//                    latitude = String.valueOf(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LATITUDE)));
//                    longitude = String.valueOf(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LONGITUDE)));
//                }
//            }
//        });


        getLoaderManager().initLoader(CURSOR_SPINNER_LOADER, null, CreateVenueFragment.this);

//        if (toolbar != null) {
//            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        }

//        ((CreateInviteActivity)getActivity()).setScreenTitle(getString(R.string.create_venue_title));
        getActivity().setTitle(getString(R.string.create_venue_title));

        return rootView;
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "onPause.. enter");
//        getActivity().getSupportFragmentManager().saveFragmentInstanceState(this);
        invite.setVenueName(name.getText().toString());
        invite.setVenueContact(phone.getText().toString());
        invite.setWebsite(website.getText().toString());
        invite.setVenueAddress(address.getText().toString());
        invite.setLatitude(latitude);
        invite.setLongitude(longitude);
        invite.setPlaceID(placeId);
        ((Invite) getActivity().getApplication()).setInvitation(invite);
        Log.d(LOG_TAG, "onPause.. exiting");
        super.onPause();
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String vName = name.getText().toString();
//        String vPhone = phone.getText().toString();
//        String vWeb = website.getText().toString();
//        String vAdd = address.getText().toString();
//        if(!Util.isNull(vName)) {
//            outState.putString(V_NAME, vName);
//        }
//        if(!Util.isNull(vPhone)) {
//            outState.putString(V_PHONE, vPhone);
//        }
//        if(!Util.isNull(vWeb)) {
//            outState.putString(V_WEB, vWeb);
//        }
//        if(!Util.isNull(vAdd)) {
//            outState.putString(V_ADD, vAdd);
//        }
//        if(!Util.isNull(latitude)) {
//            outState.putString(V_LAT, latitude);
//        }
//        if(!Util.isNull(longitude)) {
//            outState.putString(V_LON, longitude);
//        }
//        if(!Util.isNull(placeId)) {
//            outState.putString(V_PLACE_ID, placeId);
//        }
//    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        Log.d(LOG_TAG,"onActivityCreated");
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            Log.d(LOG_TAG,"onActivityCreated... Not NULL");
//            // Restore last state for checked position.
//            name.setText(savedInstanceState.getString(V_NAME,""));
//            phone.setText(savedInstanceState.getString(V_PHONE, ""));
//            website.setText(savedInstanceState.getString(V_WEB, ""));
//            address.setText(savedInstanceState.getString(V_ADD, ""));
//            latitude = savedInstanceState.getString(V_LAT,"");
//            longitude = savedInstanceState.getString(V_LAT,"");
//            placeId = savedInstanceState.getString(V_PLACE_ID,"");
//        } else{
//            Log.d(LOG_TAG,"onActivityCreated... NULL");
//        }
//    }

    private void placePicker() {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

//    @OnItemSelected(value = R.id.savedPalces, callback = NOTHING_SELECTED)
//    void onNothingSelected() {
//        Log.d(LOG_TAG, "nothing selected");
//    }
//
    @OnTouch(R.id.savedPalces)
    public boolean onTouch() {
        isItemSelected = true;
        return false;
    }
    @OnItemSelected(R.id.savedPalces)
    public void getSavedPlace(Spinner spinner, int position) {
        Log.d(LOG_TAG, "selected position --> " + position);
        if(isItemSelected) {
            Cursor cursor = (Cursor) spinner.getAdapter().getItem(position);// getItemAtPosition(position);
            if (cursor != null) {
                name.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_NAME)));
                phone.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_CONTACT)));
                website.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_WEBSITE)));
                address.setText(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ADDRESS)));
                placeId = cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ID));
                latitude = String.valueOf(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LATITUDE)));
                longitude = String.valueOf(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LONGITUDE)));
            }
            isItemSelected = false;
        }
    }

    @OnClick(R.id.mapBtn)
    public void getLocation() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Ask for Permission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//                placePicker();
            }
        } else {
            Log.d(LOG_TAG, "Permission Available");
            placePicker();
        }
    }

    @OnClick(R.id.createSharedFab)
    public void createInvitation() {
        Log.d(LOG_TAG, "create new invitation");

        validator.validate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(LOG_TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Permission Granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    placePicker();

                } else {
                    Log.d(LOG_TAG, "Permission Denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Location Permission is required for accessing Place Picker!", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "Request Code -->" + requestCode);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(LOG_TAG, "PLACE PICKER OK");
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    if (place.getName() != null) {
                        name.setText(place.getName());
                    }
                    if (place.getPhoneNumber() != null) {
                        phone.setText(place.getPhoneNumber());
                    }
                    if (place.getWebsiteUri() != null) {
                        website.setText(place.getWebsiteUri().toString());
                    }
                    if (place.getAddress() != null) {
                        address.setText(place.getAddress());
                    }
                    if (place.getId() != null) {
                        placeId = place.getId();
                    }
                    if (place.getLatLng() != null) {
                        latitude = String.valueOf(place.getLatLng().latitude);
                        longitude = String.valueOf(place.getLatLng().longitude);
                    }

                    Uri uri = getActivity().getApplication().getContentResolver().insert(DBContract.PlaceEntry.CONTENT_URI, Util.getPlaceValues(place));
                    // check for return uri and take action
                    if (uri != null && uri.equals(DBContract.PlaceEntry.buildPlaceUri())) {
                        Log.d(LOG_TAG, "Place inserted !!!");
                    } else {
                        Log.d(LOG_TAG, "Error inserting place !!!");
                    }
                } else {
                    Log.d(LOG_TAG, "Result Code -->" + resultCode);
                }
                break;
            case REGISTRATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(LOG_TAG, "REGISTRATION OK");

                } else {
                    Log.d(LOG_TAG, "Result Code -->" + resultCode);
                }
                break;
        }
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                Place place = PlacePicker.getPlace(getActivity(), data);
//                if(place.getName()!=null) {
//                    name.setText(place.getName());
//                }
//                if(place.getPhoneNumber()!=null) {
//                    phone.setText(place.getPhoneNumber());
//                }
//                if(place.getWebsiteUri()!=null) {
//                    website.setText(place.getWebsiteUri().toString());
//                }
//                if(place.getAddress()!=null){
//                    address.setText(place.getAddress());
//                }
//                if(place.getId()!=null){
//                    placeId = place.getId();
//                }
//                if(place.getLatLng()!=null){
//                    latitude = String.valueOf(place.getLatLng().latitude);
//                    longitude = String.valueOf(place.getLatLng().longitude);
//                }
//
//                Uri uri = getActivity().getApplication().getContentResolver().insert(DBContract.PlaceEntry.CONTENT_URI, Util.getPlaceValues(place));
//                // check for return uri and take action
//                if (uri != null && uri.equals(DBContract.PlaceEntry.buildPlaceUri())) {
//                    Log.d(LOG_TAG, "Place inserted !!!");
//                } else {
//                    Log.d(LOG_TAG, "Error inserting place !!!");
//                }
//            } else {
//                Log.d(LOG_TAG, "Result Code -->" + resultCode);
//            }
//        }
    }

    @Override
    public void onValidationSucceeded() {

        invite = ((Invite) getActivity().getApplication()).getInvitation();
        invite.setVenueName(name.getText().toString());
        invite.setVenueContact(phone.getText().toString());
        String webURL = website.getText().toString();
//        if (webURL != null && !webURL.trim().isEmpty()) {
        invite.setWebsite(webURL);
//        }
        invite.setVenueAddress(address.getText().toString());
//        invite.setVenueCountry(country.getText().toString());
//        invite.setVenueState(state.getText().toString());
//        invite.setVenueAdd1(street1.getText().toString());
//        String add2 = street2.getText().toString();
//        if (add2 != null && !add2.trim().isEmpty()) {
//            invite.setVenueAdd2(add2);
//        }
//        invite.setVenueCity(city.getText().toString());
//        String pin = zip.getText().toString();
//        if (pin != null && !pin.trim().isEmpty()) {
//            invite.setVenueZip(pin);
//        }
//        if (latitude != null && !latitude.trim().isEmpty()) {
        invite.setLatitude(latitude);
//        }
//        if (longitude != null && !longitude.trim().isEmpty()) {
        invite.setLongitude(longitude);
//        }
//        if(placeId != null && !placeId.trim().isEmpty()) {
        invite.setPlaceID(placeId);
//        }

        if (PrefHelper.isUserRegistered()) {
            // show alert
            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.create_invite_alert_msg))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with saving
                            progressBar.setVisibility(View.VISIBLE);
                            getLoaderManager().initLoader(CURSOR_LOADER, null, CreateVenueFragment.this);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            // open user registration screen
            Log.d(LOG_TAG, "Open Registration Screen");
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            startActivityForResult(intent, REGISTRATION_REQUEST);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case CURSOR_LOADER:
                Uri registeredUser = DBContract.UserEntry.buildUserDataUri(UserEntry.USER_TYPE_SELF);
                loader = new CursorLoader(getContext(),
                        registeredUser,
                        null,
                        UserEntry.COL_USER_TYPE + " = ?",
                        null,
                        null);
                break;
            case CURSOR_SPINNER_LOADER:
                Uri savedPlaces = DBContract.PlaceEntry.buildPlaceUri();
                loader = new CursorLoader(getContext(),
                        savedPlaces,
                        null,
                        null,
                        null,
                        null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished -->" + cursor.getCount());
        switch (loader.getId()) {
            case CURSOR_LOADER:
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    User user = new User();
                    user.setName(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_EMAIL)));
                    user.setContact(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_CONTACT)));
                    user.setCountry(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_COUNTRY)));
                    user.setState(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_STATE)));
                    user.setAdd1(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_ADD1)));
                    user.setAdd2(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_ADD2)));
                    user.setCity(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_CITY)));
                    user.setZip(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_ZIP)));
                    user.setCity(cursor.getString(cursor.getColumnIndex(UserEntry.COL_USER_CITY)));

                    Log.d(LOG_TAG, "Name-->" + user.getName());

//            invite = ((Invite)getActivity().getApplication()).getInvitation();
                    invite.setInvitee(user);

                    ((Invite) getActivity().getApplication()).setInvitation(invite);

                    ResponseReceiver receiver = new ResponseReceiver(new Handler());
                    receiver.setReceiver(this);

                    ((Invite) getActivity().getApplication()).setReceiver(receiver);

                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), BackgroundService.class);
                    intent.putExtra(BackgroundService.ACTION, BackgroundService.CREATE_INVITE);
                    getActivity().startService(intent);
                }
                Log.d(LOG_TAG, "Destroying Loader");
                getLoaderManager().destroyLoader(CURSOR_LOADER);
                break;
            case CURSOR_SPINNER_LOADER:
                if (cursor.getCount() > 0) {
                    savedPlaces.setVisibility(View.VISIBLE);
                    spinAdapter.swapCursor(cursor);
//                    cursor.moveToFirst();
//                    while(cursor.getCount() > 0) {
//                        Places place = new Places();
//                        place.setPlaceId(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ID)));
//                        place.setPlaceName(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_NAME)));
//                        place.setPlaceAddress(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_ADDRESS)));
//                        place.setPlaceContact(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_CONTACT)));
//                        place.setPlaceWebsite(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_WEBSITE)));
//                        place.setPlaceLatitude(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LATITUDE)));
//                        place.setPlaceLongitude(cursor.getString(cursor.getColumnIndex(PlaceEntry.COL_PLACE_LONGITUDE)));
//                        Log.d(LOG_TAG,"Saved Place --> "+place.getPlaceName());
//                        cursor.moveToNext();
//                    }
                }
                else{
                    savedPlaces.setVisibility(View.GONE);
                }
//                Log.d(LOG_TAG, "Destroying Loader");
//                getLoaderManager().destroyLoader(CURSOR_SPINNER_LOADER);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        spinAdapter.swapCursor(null);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Invitation invite = ((Invite) getActivity().getApplication()).getInvitation();

        switch (resultCode) {
            case BackgroundService.CREATE_INVITE:
                String id = resultData.getString(BackgroundService.INVITATION_ID);
                if (id != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "ID: " + id, Toast.LENGTH_LONG).show();
                    invite.setId(id);
                    //save to db
                    Uri uri = getActivity().getApplication().getContentResolver().insert(DBContract.InviteEntry.CONTENT_URI, Util.getInvitationValues(invite));
                    if (uri != null && uri.equals(DBContract.InviteEntry.buildInviteUri())) {
                        // success
                        Bundle bundle = new Bundle();
                        bundle.putString(InvitationFragment.INVITATION_ID, id);

                        Intent intent = new Intent(getActivity(), InvitationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        // failed to save data
                        Toast.makeText(getActivity(), "Error while saving invitation", Toast.LENGTH_LONG).show();
                    }
                    // self user already registered
//                    getActivity().getApplication().getContentResolver().insert(DBContract.UserEntry.CONTENT_URI, Util.getUserValues(invite.getInvitee(), true));
//                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), BackgroundService.class);
//                    intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
//                    intent.putExtra(BackgroundService.INVITATION_ID, id);
//                    getActivity().startService(intent);
                }
                break;
            case BackgroundService.CREATE_INVITE_ERR:
                Toast.makeText(getActivity(), resultData.getString(BackgroundService.ERROR), Toast.LENGTH_LONG).show();
                break;
        }
        progressBar.setVisibility(View.GONE);
    }
}
