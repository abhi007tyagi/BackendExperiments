package com.tyagiabhinav.backendexperiments.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backendexperiments.DB.DBContract;
import com.tyagiabhinav.backendexperiments.Invite;
import com.tyagiabhinav.backendexperiments.Network.BackgroundService;
import com.tyagiabhinav.backendexperiments.Network.ResponseReceiver;
import com.tyagiabhinav.backendexperiments.R;
import com.tyagiabhinav.backendexperiments.Util.PrefHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 14/03/16.
 */
public class HomeFragment extends Fragment implements ResponseReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    private View rootView;
    @Bind(R.id.code1) EditText code1;
    @Bind(R.id.code2) EditText code2;
    @Bind(R.id.code3) EditText code3;
    private int CURSOR_LOADER = 1006;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);
        code1.addTextChangedListener(codeWatcher);
        code2.addTextChangedListener(codeWatcher);
        code3.addTextChangedListener(codeWatcher);
        return rootView;
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
            if(count == 3){
                if(code1.isFocused()){
                    Log.d(LOG_TAG, "moving to 2");
                    code2.requestFocus();
                }else if(code2.isFocused()){
                    Log.d(LOG_TAG, "moving to 3");
                    code3.requestFocus();
                }else if(code3.isFocused()){
                    Log.d(LOG_TAG, "Show invitation !!!");
                }
            }
        }
    };

    @OnClick(R.id.createInviteFab)
    public void createInvite(){
        if(PrefHelper.isUserRegistered()){
            // open invite screen
            Log.d(LOG_TAG, "Open Invitation Screen");
            Intent intent = new Intent(getActivity(),CreateInviteActivity.class);
            startActivity(intent);
        } else {
            // open user registration screen
            Log.d(LOG_TAG, "Open Registration Screen");
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case BackgroundService.GET_INVITE:
                Invitation invite = ((Invite) getActivity().getApplication()).getInvitation();
                Toast.makeText(getActivity(), "Invitation: " + invite.getTitle() + " received !!", Toast.LENGTH_LONG).show();
                break;
            case BackgroundService.CREATE_INVITE:
                String id = resultData.getString(BackgroundService.INVITATION_ID);
                Toast.makeText(getActivity(), "ID: " + id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), BackgroundService.class);
                intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
                intent.putExtra(BackgroundService.INVITATION_ID, id);
                getActivity().startService(intent);
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri trailer = DBContract.InviteEntry.buildInvitationDataUri("737DT608H");
        return new CursorLoader(getContext(),
                trailer,
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
            String title = cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_TITLE));
            String name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_NAME));

            Log.d(LOG_TAG, "Title-->"+title + "||  Name-->"+name);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
