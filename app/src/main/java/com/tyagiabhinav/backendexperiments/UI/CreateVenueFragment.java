package com.tyagiabhinav.backendexperiments.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tyagiabhinav.backendexperiments.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateVenueFragment extends Fragment implements Validator.ValidationListener {
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
    private Validator validator;

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

    @Override
    public void onValidationSucceeded() {

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
}
