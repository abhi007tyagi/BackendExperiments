package com.tyagiabhinav.einvite;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.einvite.Network.ResponseReceiver;
import com.tyagiabhinav.einvite.UI.ExceptionActivity;
import com.tyagiabhinav.einvite.Util.PrefHelper;

/**
 * Created by abhinavtyagi on 13/03/16.
 */
public class Invite extends Application {

    public static final String UNCAUGHT_ERROR = "uncaught_error";

    ResponseReceiver receiver;
    Invitation invitation;

    @Override
    public void onCreate() {
        super.onCreate();
        PrefHelper.init(getApplicationContext());
    }

    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler exceptionHandler;

    public Invite() {
        exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler);
    }

    //Handle uncaught exception..  handler listener
    private Thread.UncaughtExceptionHandler unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {

                    Intent errorIntent = new Intent(getBaseContext(), ExceptionActivity.class);
                    errorIntent.putExtra(UNCAUGHT_ERROR,throwable);
//                    errorIntent.putExtra(UNCAUGHT_ERROR_STACK_TRACE,throwable.getStackTrace());

                    PendingIntent myActivity = PendingIntent.getActivity(getBaseContext(),
                            30006, errorIntent, PendingIntent.FLAG_ONE_SHOT);

                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 7000, myActivity);
                    System.exit(2);

                    // re-throw critical exception further to the os (important)
                    exceptionHandler.uncaughtException(thread, throwable);
                }
            };


    public ResponseReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(ResponseReceiver receiver) {
        this.receiver = receiver;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }
}
