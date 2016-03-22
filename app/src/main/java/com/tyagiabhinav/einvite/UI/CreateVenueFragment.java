package com.tyagiabhinav.einvite.UI;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Url;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract;
import com.tyagiabhinav.einvite.DB.DBContract.UserEntry;
import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.Network.BackgroundService;
import com.tyagiabhinav.einvite.Network.ResponseReceiver;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateVenueFragment extends Fragment implements Validator.ValidationListener, LoaderManager.LoaderCallbacks<Cursor>, ResponseReceiver.Receiver {
    private static final String LOG_TAG = CreateVenueFragment.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 7;

    private View rootView;
    @NotEmpty
    @Bind(R.id.venueName)
    EditText name;
//    @NotEmpty
//    @Bind(R.id.venueCountry)
//    EditText country;
//    @NotEmpty
//    @Bind(R.id.venueState)
//    EditText state;
//    @NotEmpty
//    @Bind(R.id.venueAdd1)
//    EditText street1;
//    @Bind(R.id.venueAdd2)
//    EditText street2;
//    @NotEmpty
//    @Bind(R.id.venueCity)
//    EditText city;
//    @Bind(R.id.venueZip)
//    EditText zip;
    @NotEmpty
    @Bind(R.id.venuePhone)
    EditText phone;
    @Url
    @Bind(R.id.website)
    EditText website;
    @NotEmpty
    @Bind(R.id.address)
    EditText address;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    String latitude, longitude, placeId;
    Invitation invite;
    private Validator validator;
    private static final int CURSOR_LOADER = 205;
    private int PLACE_PICKER_REQUEST = 106;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.create_venue_fragment, container, false);
        ButterKnife.bind(this, rootView);
        validator = new Validator(this);
        validator.setValidationListener(this);

//        name.addTextChangedListener(textWatcher);
//        country.addTextChangedListener(textWatcher);
//        state.addTextChangedListener(textWatcher);
//        street1.addTextChangedListener(textWatcher);
//        street2.addTextChangedListener(textWatcher);
//        city.addTextChangedListener(textWatcher);
//        zip.addTextChangedListener(textWatcher);
//        email.addTextChangedListener(textWatcher);
//        phone.addTextChangedListener(textWatcher);
        return rootView;
    }

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

    @OnClick(R.id.createInvitation)
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
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                if(place.getName()!=null) {
                    name.setText(place.getName());
                }
                if(place.getPhoneNumber()!=null) {
                    phone.setText(place.getPhoneNumber());
                }
                if(place.getWebsiteUri()!=null) {
                    website.setText(place.getWebsiteUri().toString());
                }
                if(place.getAddress()!=null){
                    address.setText(place.getAddress());
                }
                if(place.getId()!=null){
                    placeId = place.getId();
                }
                if(place.getLatLng()!=null){
                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                }
            } else {
                Log.d(LOG_TAG, "Result Code -->" + resultCode);
            }
        }
    }

    @Override
    public void onValidationSucceeded() {

        progressBar.setVisibility(View.VISIBLE);

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

        getLoaderManager().initLoader(CURSOR_LOADER, null, this);

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
        Uri registeredUser = DBContract.UserEntry.buildUserDataUri(UserEntry.USER_TYPE_SELF);
        return new CursorLoader(getContext(),
                registeredUser,
                null,
                UserEntry.COL_USER_TYPE + " = ?",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished -->" + cursor.getCount());
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
                    if(uri.equals(DBContract.InviteEntry.buildInviteUri())){
                        // success
                        Bundle bundle = new Bundle();
                        bundle.putString(InvitationFragment.INVITATION_ID, id);

                        Intent intent = new Intent(getActivity(),InvitationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        // failed to save data
                        Toast.makeText(getActivity(),"Error while saving invitation",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(),resultData.getString(BackgroundService.ERROR),Toast.LENGTH_LONG).show();
                break;
        }
        progressBar.setVisibility(View.GONE);
    }
}
