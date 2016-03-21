package com.tyagiabhinav.einvite.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateInviteFragment extends Fragment implements Validator.ValidationListener {

    private static final String LOG_TAG = CreateInviteFragment.class.getSimpleName();

    private View rootView;

    @NotEmpty
    @Bind(R.id.title)
    EditText title;
    @NotEmpty
    @Bind(R.id.time)
    EditText time;
    @NotEmpty
    @Bind(R.id.date)
    EditText date;
    @NotEmpty
    @Bind(R.id.message)
    EditText message;
    @Bind(R.id.msgLimit)
    TextView msgLimit;
    @Bind(R.id.type)
    Spinner type;
    private Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.create_invite_fragment, container, false);
        ButterKnife.bind(this, rootView);
        validator = new Validator(this);
        validator.setValidationListener(this);
        message.addTextChangedListener(messageWatcher);
        return rootView;
    }

    private final TextWatcher messageWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(LOG_TAG, "Count: " + count);

        }

        public void afterTextChanged(Editable s) {
            Log.d(LOG_TAG, "Length: " + s.length());
            msgLimit.setText(s.length() + "/150");
            if (s.length() >= 150) {
//                message.setEnabled(false);
                s.delete(150, 150);
            }
        }
    };

//    @OnItemSelected(R.id.type)
//    public void selectType(Spinner spinner, int position) {
//        Log.d(LOG_TAG, "select type");
//            String type = (String) spinner.getAdapter().getItem(position);
//
//        }

    @OnClick(R.id.time)
    public void selectTime(View view){
        Log.d(LOG_TAG,"select time");
        Util.showTimepickerDialog(getActivity(), view);
    }

    @OnClick(R.id.date)
    public void selectDate(View view){
        Log.d(LOG_TAG, "select date");
        Util.showDatePicker(view, getActivity(), true);
    }

    @OnClick(R.id.createVenueFab)
    public void createVenue() {
        Log.d(LOG_TAG, "move to create venue screen");
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        String eventTime = time.getText().toString();
        String eventDate = date.getText().toString();
        if(Util.isOldTimeAndDate(eventTime,eventDate)){
            Log.d(LOG_TAG, "Old Time/Date");
            Toast.makeText(getActivity(),"Can not select previous date or time! ",Toast.LENGTH_LONG).show();
        }
        else{
            Log.d(LOG_TAG, "Time/Date Correct");
            // save this screen's data and move to next
            Invitation invite = new Invitation();
            invite.setTitle(title.getText().toString());
            invite.setType(type.getSelectedItem().toString());
            invite.setTime(time.getText().toString());
            invite.setDate(date.getText().toString());
            invite.setMessage(message.getText().toString());

            ((Invite) getActivity().getApplication()).setInvitation(invite);
            ((CreateInviteActivity) getActivity()).showNextScreen();
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
