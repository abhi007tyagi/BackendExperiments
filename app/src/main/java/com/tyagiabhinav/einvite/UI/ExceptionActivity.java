package com.tyagiabhinav.einvite.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tyagiabhinav.einvite.Invite;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        Log.d("###ExceptionActivity###", "***Unknown Error***");
        Throwable errorMsg = (Throwable) getIntent().getExtras().get(Invite.UNCAUGHT_ERROR);
        StringWriter errorStringWriter = new StringWriter();
        PrintWriter errorPrintWriter = new PrintWriter(errorStringWriter);
        errorMsg.printStackTrace(errorPrintWriter);
        Log.d("### ERROR ###", errorMsg.getMessage());
        Log.d("### ERROR STACK ###", errorStringWriter.toString());
//        errorMsg.printStackTrace();
        final String errorFile = Util.saveUncaughtError(errorMsg.getMessage(), errorStringWriter.toString());


        Button homeBtn = (Button) findViewById(R.id.home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ExceptionActivity", "onClick");
                Intent intent = new Intent(ExceptionActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        Button reportBtn = (Button) findViewById(R.id.report);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ExceptionActivity", "onClick");
                Toast.makeText(ExceptionActivity.this, "email clicked", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.fromFile(new File(errorFile));
                String[] TO = {"abhi007tyagi@yahoo.co.in"};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Error Report from Adherence");
                emailIntent.setType("*/*");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Adherence Error Report");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                finish();
            }
        });
    }
}
