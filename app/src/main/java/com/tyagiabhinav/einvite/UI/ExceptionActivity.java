package com.tyagiabhinav.einvite.UI;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

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

import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionActivity extends AppCompatActivity {

    private Throwable errorMsg;
    private StringWriter errorStringWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        Log.d("###ExceptionActivity###", "***Unknown Error***");
        errorMsg = (Throwable) getIntent().getExtras().get(Invite.UNCAUGHT_ERROR);
        errorStringWriter = new StringWriter();
        PrintWriter errorPrintWriter = new PrintWriter(errorStringWriter);
        errorMsg.printStackTrace(errorPrintWriter);
        Log.d("### ERROR ###", errorMsg.getMessage());
        Log.d("### ERROR STACK ###", errorStringWriter.toString());
//        errorMsg.printStackTrace();
//        final String errorFile = Util.saveUncaughtError(this,errorMsg.getMessage(), errorStringWriter.toString());


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
                Toast.makeText(ExceptionActivity.this, "Select email client to send report", Toast.LENGTH_SHORT).show();
//                Uri uri = Uri.fromFile(new File(errorFile));
                String[] TO = {"abhi007tyagi@gmail.com"};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Error Report from eInvite\n\n##ERROR##\n"+errorMsg.getMessage()+"\n\n##ERROR STACK##\n"+errorStringWriter.toString());
                emailIntent.setType("*/*");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "eInvite Error Report");
//                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                finish();
            }
        });
    }
}
