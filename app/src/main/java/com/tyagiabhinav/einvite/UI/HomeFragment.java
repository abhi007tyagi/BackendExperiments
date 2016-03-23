package com.tyagiabhinav.einvite.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.einvite.DB.DBContract;
import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.Network.BackgroundService;
import com.tyagiabhinav.einvite.Network.ResponseReceiver;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.PrefHelper;
import com.tyagiabhinav.einvite.Util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class HomeFragment extends Fragment implements ResponseReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    private View rootView;
    @Bind(R.id.code1)
    EditText code1;
    @Bind(R.id.code2)
    EditText code2;
    @Bind(R.id.code3)
    EditText code3;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private static final int CURSOR_LOADER = 1006;

    private String inviteID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);

        code1.addTextChangedListener(codeWatcher);
        code2.addTextChangedListener(codeWatcher);
        code3.addTextChangedListener(codeWatcher);


        Bundle bundle = getArguments();
        String id = bundle.getString(InvitationFragment.INVITATION_ID);
        if(Util.isNull(id)){
            // continue
        }else {
            // deeplink activation.. fetch id
            deeplinkAccess(bundle);
        }
        return rootView;
    }

    private void deeplinkAccess(Bundle bundle){
        Log.d(LOG_TAG, "DeepLink Access");
        String id = bundle.getString(InvitationFragment.INVITATION_ID);
        code1.setText(id.substring(0,3));
        code2.setText(id.substring(3,6));
        code3.setText(id.substring(6,9));

        getLoaderManager().initLoader(CURSOR_LOADER, bundle, HomeFragment.this);
    }

    private final TextWatcher codeWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(LOG_TAG, "Count: " + count);

        }

        public void afterTextChanged(Editable s) {
            Log.d(LOG_TAG, "Length: " + s.length());
            int count = s.length();
            if (count == 3) {
                if (code1.isFocused()) {
                    Log.d(LOG_TAG, "moving to 2");
                    code2.requestFocus();
                } else if (code2.isFocused()) {
                    Log.d(LOG_TAG, "moving to 3");
                    code3.requestFocus();
                } else if (code3.isFocused()) {
                    Log.d(LOG_TAG, "Show invitation !!!");
                    inviteID = code1.getText().toString()+code2.getText().toString()+code3.getText().toString();
                    Log.d(LOG_TAG, "ID->" + inviteID);
                    Bundle bundle = new Bundle();
                    bundle.putString(InvitationFragment.INVITATION_ID,inviteID);
                    getLoaderManager().initLoader(CURSOR_LOADER, bundle, HomeFragment.this);
                }
            }
            if (count == 0) {
                if (code3.isFocused()) {
                    Log.d(LOG_TAG, "moving to 2");
                    code2.requestFocus();
                } else if (code2.isFocused()) {
                    Log.d(LOG_TAG, "moving to 1");
                    code1.requestFocus();
                } else if (code1.isFocused()) {
                    Log.d(LOG_TAG, "Do Nothing");
                }
            }
        }
    };

    @OnClick(R.id.createInviteFab)
    public void createInvite() {
        if (PrefHelper.isUserRegistered()) {
            // open invite screen
            Log.d(LOG_TAG, "Open Invitation Screen");
            Intent intent = new Intent(getActivity(), CreateInviteActivity.class);
            startActivity(intent);
        } else {
            // open user registration screen
            Log.d(LOG_TAG, "Open Registration Screen");
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(LOG_TAG,"onReceiveResult");
        switch (resultCode) {
            case BackgroundService.GET_INVITE:
                Invitation invite = ((Invite) getActivity().getApplication()).getInvitation();
                Uri uriUser = getActivity().getApplication().getContentResolver().insert(DBContract.UserEntry.CONTENT_URI, Util.getUserValues(invite.getInvitee(), true));
                Uri uriInvite = getActivity().getApplication().getContentResolver().insert(DBContract.InviteEntry.CONTENT_URI, Util.getInvitationValues(invite));

                if(uriInvite.equals(DBContract.InviteEntry.buildInviteUri()) && uriUser.equals(DBContract.UserEntry.buildUserUri())){
                    // success
                    Log.d(LOG_TAG,"Moving to invitation screen ID -->"+invite.getId());
                    Bundle bundle = new Bundle();
                    bundle.putString(InvitationFragment.INVITATION_ID, invite.getId());

                    Intent intent = new Intent(getActivity(),InvitationActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    Toast.makeText(getActivity(), "Invitation: " + invite.getTitle() + " received !!", Toast.LENGTH_LONG).show();
                }
                else{
                    // failed to save data
                    Toast.makeText(getActivity(),"Error Occurred. Try again Later!",Toast.LENGTH_LONG).show();
                }
                break;
            case BackgroundService.GET_INVITE_ERR:
                String errMSG = resultData.getString(BackgroundService.ERROR);
                Log.d(LOG_TAG,"ERROR GET INVITE : "+errMSG);

                Toast.makeText(getActivity(),errMSG,Toast.LENGTH_LONG).show();
                break;
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader ID-->"+bundle.getString(InvitationFragment.INVITATION_ID));
        Uri invitation = DBContract.InviteEntry.buildInvitationDataUri(bundle.getString(InvitationFragment.INVITATION_ID));
        return new CursorLoader(getContext(),
                invitation,
                null,
                DBContract.InviteEntry.TABLE_NAME + "." + DBContract.InviteEntry.COL_INVITEE + "=" + DBContract.UserEntry.TABLE_NAME + "." + DBContract.UserEntry.COL_USER_EMAIL + " AND " + DBContract.InviteEntry.TABLE_NAME + "." + DBContract.InviteEntry.COL_ID + " = ?",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished -->" + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_ID));
            Log.d(LOG_TAG, "Present on device ID-->" + id);
            Bundle bundle = new Bundle();
            bundle.putString(InvitationFragment.INVITATION_ID, id);

            Intent intent = new Intent(getActivity(),InvitationActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            // fetch from server
            Log.d(LOG_TAG, "Not present. Fetching from Server");
            progressBar.setVisibility(View.VISIBLE);
            ResponseReceiver receiver = new ResponseReceiver(new Handler());
            receiver.setReceiver(this);

            ((Invite) getActivity().getApplication()).setReceiver(receiver);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), BackgroundService.class);
            intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
            intent.putExtra(BackgroundService.INVITATION_ID, inviteID);
            getActivity().startService(intent);
        }
        Log.d(LOG_TAG, "Destroying Loader");
        getLoaderManager().destroyLoader(CURSOR_LOADER);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
