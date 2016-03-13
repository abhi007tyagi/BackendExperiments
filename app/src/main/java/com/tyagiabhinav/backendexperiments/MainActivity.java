package com.tyagiabhinav.backendexperiments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.tyagiabhinav.backend.backendService.BackendService;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.backendexperiments.Network.BackgroundService;
import com.tyagiabhinav.backendexperiments.Network.ResponseReceiver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResponseReceiver.Receiver{

    TextView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User();
        user.setName("Tyagi Abhinav");
        user.setEmail("tyagi@tyagi.com");
        user.setContact("9876543210");
        user.setCountry("India");
        user.setState("Delhi");
        user.setCity("New Delhi");
        user.setAdd1("Vikas Puri");
        user.setAdd2("Near Oxford School");
        user.setZip("110018");

        Invitation invite = new Invitation();
        invite.setInvitee(user);
        invite.setTitle("My Birthday");
        invite.setType("B'Day");
        invite.setTime("7:00pm");
        invite.setDate("2016/09/27");
        invite.setMessage("Come and attend my b'day... :)");
        invite.setVenueName("Taj Palace Hotel");
        invite.setVenueEmail("taj@taj.com");
        invite.setVenueContact("9876543210");
        invite.setVenueCountry("India");
        invite.setVenueState("Delhi");
        invite.setVenueCity("New Delhi");
        invite.setVenueAdd1("Manshingh Road");
        invite.setVenueAdd2("Near India Gate");
        invite.setVenueZip("110001");
        invite.setWebsite("tyagiabhinav.com");

        ((Invite)getApplication()).setInvitation(invite);

        ResponseReceiver receiver = new ResponseReceiver(new Handler());
        receiver.setReceiver(this);

        ((Invite)getApplication()).setReceiver(receiver);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
        intent.putExtra(BackgroundService.ACTION, BackgroundService.CREATE_INVITE);
        startService(intent);

        dataView = (TextView)findViewById(R.id.data);
//        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case BackgroundService.GET_INVITE:
                Invitation invite = ((Invite)getApplication()).getInvitation();
                Toast.makeText(getApplicationContext(),"Invitation: "+invite.getTitle() + " received !!", Toast.LENGTH_LONG).show();
                break;
            case BackgroundService.CREATE_INVITE:
                String id = resultData.getString(BackgroundService.INVITATION_ID);
                Toast.makeText(getApplicationContext(),"ID: "+id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
                intent.putExtra(BackgroundService.ACTION, BackgroundService.GET_INVITE);
                intent.putExtra(BackgroundService.INVITATION_ID,id);
                startService(intent);
                break;
        }
    }


    class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Invitation>> {
        private BackendService backendService = null;
        private Context context;

        EndpointsAsyncTask(Context context) {
            this.context = context;
            dataView.setText("Fetching data...");
        }

        @Override
        protected List<Invitation> doInBackground(Void... params) {
            if(backendService == null) { // Only do this once
                BackendService.Builder builder = new BackendService.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://javatestbackend.appspot.com/_ah/api")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                backendService = builder.build();
            }

            try {
                return backendService.listInvitations().execute().getItems();
            } catch (IOException e) {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Invitation> result) {
            StringBuilder data = new StringBuilder();
            for (Invitation user : result) {
//                data.append("Name: "+user.getName()+"\n");
//                data.append("Address: "+user.getAddress()+"\n");
//                data.append("Contact1: "+user.getContact1()+"\n");
//                data.append("Contact2: "+user.getContact2()+"\n");
                data.append("\n\n");
            }
            dataView.setText(data.toString());
        }
    }
}
