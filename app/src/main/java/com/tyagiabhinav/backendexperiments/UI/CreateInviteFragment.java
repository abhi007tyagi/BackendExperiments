package com.tyagiabhinav.backendexperiments.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tyagiabhinav.backendexperiments.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateInviteFragment extends Fragment {

    private static final String LOG_TAG = CreateInviteFragment.class.getSimpleName();

    private View rootView;

    @Bind(R.id.title) EditText title;
    @Bind(R.id.time) EditText time;
    @Bind(R.id.date) EditText date;
    @Bind(R.id.message) EditText message;
    @Bind(R.id.msgLimit) TextView msgLimit;
    @Bind(R.id.type) Spinner type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.create_invite_fragment, container, false);
        ButterKnife.bind(this, rootView);
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

    @OnClick(R.id.createVenueFab)
    public void createVenue(){
        Log.d(LOG_TAG,"move to create venue screen");

        // save this screen's data and move to next
        ((CreateInviteActivity)getActivity()).showNextScreen();
    }
}
