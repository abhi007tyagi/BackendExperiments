package com.tyagiabhinav.einvite.UI;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract.UserEntry;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Encrypt;
import com.tyagiabhinav.einvite.Util.PrefHelper;
import com.tyagiabhinav.einvite.Util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class RegistrationFragment extends Fragment implements ValidationListener {

    private static final String LOG_TAG = RegistrationFragment.class.getSimpleName();

    private View rootView;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    @NotEmpty
    @Bind(R.id.name)
    EditText name;
    @NotEmpty
    @Bind(R.id.country)
    EditText country;
    @NotEmpty
    @Bind(R.id.state)
    EditText state;
    @NotEmpty
    @Bind(R.id.street1)
    EditText street1;
    @Bind(R.id.street2)
    EditText street2;
    @NotEmpty
    @Bind(R.id.city)
    EditText city;
    @Bind(R.id.zip)
    EditText zip;
    @NotEmpty
    @Email
    @Bind(R.id.email)
    EditText email;
    @NotEmpty
    @Bind(R.id.phone)
    EditText phone;
    private Validator validator;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.registration_fragment, container, false);
        ButterKnife.bind(this, rootView);
        validator = new Validator(this);
        validator.setValidationListener(this);

//        if (toolbar != null) {
//            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        }
        return rootView;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(LOG_TAG, "Count: " + count);

        }

        public void afterTextChanged(Editable s) {
            Log.d(LOG_TAG, "Length: " + s.length());
        }
    };

    @OnClick(R.id.registerBtn)
    public void registration() {
        Log.d(LOG_TAG, "registration");

        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Log.d(LOG_TAG, "ValidationSucceeded");
        user = new User();
        try {
            user.setEmail(Encrypt.doAESEncryption(email.getText().toString()));
            user.setName(name.getText().toString());
            user.setContact(Encrypt.doAESEncryption(phone.getText().toString()));
            user.setCountry(Encrypt.doAESEncryption(country.getText().toString()));
            user.setState(Encrypt.doAESEncryption(state.getText().toString()));
            user.setAdd1(Encrypt.doAESEncryption(street1.getText().toString()));
            String add2 = street2.getText().toString();
            if (add2 != null && !add2.trim().isEmpty()) {
                user.setAdd2(Encrypt.doAESEncryption(add2));
            }
            user.setCity(Encrypt.doAESEncryption(city.getText().toString()));
            String pin = zip.getText().toString();
            if (pin != null && !pin.trim().isEmpty()) {
                user.setZip(Encrypt.doAESEncryption(pin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = getActivity().getApplication().getContentResolver().insert(UserEntry.CONTENT_URI, Util.getUserValues(user, true));
        // check for return uri and take action
        if (uri != null && uri.equals(UserEntry.buildUserUri())) {
            PrefHelper.setUserRegistered(true);
//            Intent intent = new Intent((), CreateInviteActivity.class);
//            startActivity(intent);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Error registering user !!!", Toast.LENGTH_LONG).show();
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
}
