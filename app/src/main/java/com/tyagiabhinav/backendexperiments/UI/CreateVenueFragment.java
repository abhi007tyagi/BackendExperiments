package com.tyagiabhinav.backendexperiments.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyagiabhinav.backendexperiments.R;

import butterknife.ButterKnife;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class CreateVenueFragment extends Fragment {
    private static final String LOG_TAG = CreateVenueFragment.class.getSimpleName();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.create_venue_fragment, container, false);
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
}
