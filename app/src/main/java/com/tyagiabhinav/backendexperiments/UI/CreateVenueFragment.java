package com.tyagiabhinav.backendexperiments.UI;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Url;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.backendexperiments.DB.DBContract;
import com.tyagiabhinav.backendexperiments.DB.DBContract.UserEntry;
import com.tyagiabhinav.backendexperiments.Invite;
import com.tyagiabhinav.backendexperiments.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateVenueFragment extends Fragment implements Validator.ValidationListener, LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String LOG_TAG = CreateVenueFragment.class.getSimpleName();

    private View rootView;
    @NotEmpty @Bind(R.id.venueName) EditText name;
    @NotEmpty @Bind(R.id.venueCountry) EditText country;
    @NotEmpty @Bind(R.id.venueState) EditText state;
    @NotEmpty @Bind(R.id.venueAdd1)EditText street1;
    @Bind(R.id.venueAdd2) EditText street2;
    @NotEmpty @Bind(R.id.venueCity) EditText city;
    @Bind(R.id.venueZip)EditText zip;
    @NotEmpty @Bind(R.id.venuePhone) EditText phone;
    @Url @Bind(R.id.website) EditText website;
    String latitude, longitude;
    Invitation invite;
    private Validator validator;
    private int CURSOR_LOADER = 205;

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

    @OnClick(R.id.createInvitation)
    public void createInvitation(){
        Log.d(LOG_TAG, "create new invitation");

        validator.validate();
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onValidationSucceeded() {
        invite = ((Invite)getActivity().getApplication()).getInvitation();
        invite.setVenueName(name.getText().toString());
        invite.setVenueContact(phone.getText().toString());
        String webURL = website.getText().toString();
        if(webURL != null && !webURL.trim().isEmpty()) {
            invite.setWebsite(webURL);
        }
        invite.setVenueCountry(country.getText().toString());
        invite.setVenueState(state.getText().toString());
        invite.setVenueAdd1(street1.getText().toString());
        String add2 = street2.getText().toString();
        if(add2 != null && !add2.trim().isEmpty()) {
            invite.setVenueAdd2(add2);
        }
        invite.setVenueCity(city.getText().toString());
        String pin = zip.getText().toString();
        if(pin != null && !pin.trim().isEmpty()) {
            invite.setVenueZip(pin);
        }
        if(latitude !=null && !latitude.trim().isEmpty()){
            invite.setLatitude(latitude);
        }
        if(longitude !=null && !longitude.trim().isEmpty()){
            invite.setLatitude(longitude);
        }

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
