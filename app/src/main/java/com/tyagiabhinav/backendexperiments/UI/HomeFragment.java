package com.tyagiabhinav.backendexperiments.UI;

import android.content.Intent;
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

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backendexperiments.Invite;
import com.tyagiabhinav.backendexperiments.Network.BackgroundService;
import com.tyagiabhinav.backendexperiments.Network.ResponseReceiver;
import com.tyagiabhinav.backendexperiments.R;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class HomeFragment extends Fragment implements ResponseReceiver.Receiver{

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    private View rootView;
    private EditText code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        code = (EditText)rootView.findViewById(R.id.code1);
        code.addTextChangedListener(codeWatcher);
        return rootView;
    }


    private final TextWatcher codeWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(LOG_TAG, "Count: "+count);
            switch (count){
                case 3:
                    code.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 6:
                    code.setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
            }
        }

        public void afterTextChanged(Editable s) {
            Log.d(LOG_TAG, "Length: "+s.length());
        }
    };

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case BackgroundService.GET_INVITE:
                Invitation invite = ((Invite)getActivity().getApplication()).getInvitation();
                Toast.makeText(getActivity(), "Invitation: " + invite.getTitle() + " received !!", Toast.LENGTH_LONG).show();
                break;
            case BackgroundService.CREATE_INVITE:
                String id = resultData.getString(BackgroundService.INVITATION_ID);
                Toast.makeText(getActivity(),"ID: "+id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), BackgroundService.class);
                intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
                intent.putExtra(BackgroundService.INVITATION_ID,id);
                getActivity().startService(intent);
                break;
        }
    }
}
