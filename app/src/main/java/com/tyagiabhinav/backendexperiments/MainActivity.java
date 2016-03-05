package com.tyagiabhinav.backendexperiments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.tyagiabhinav.backend.backendService.BackendService;
import com.tyagiabhinav.backend.backendService.model.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataView = (TextView)findViewById(R.id.data);
        new EndpointsAsyncTask(this).execute();
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, List<User>> {
        private BackendService backendService = null;
        private Context context;

        EndpointsAsyncTask(Context context) {
            this.context = context;
            dataView.setText("Fetching data...");
        }

        @Override
        protected List<User> doInBackground(Void... params) {
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
                return backendService.listUsers().execute().getItems();
            } catch (IOException e) {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<User> result) {
            StringBuilder data = new StringBuilder();
            for (User user : result) {
                data.append("Name: "+user.getName()+"\n");
                data.append("Address: "+user.getAddress()+"\n");
                data.append("Contact1: "+user.getContact1()+"\n");
                data.append("Contact2: "+user.getContact2()+"\n");
                data.append("\n\n");
            }
            dataView.setText(data.toString());
        }
    }
}
