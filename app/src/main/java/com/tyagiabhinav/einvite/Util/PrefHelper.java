package com.tyagiabhinav.einvite.Util;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class PrefHelper {

    private static final String APP = "INVITE_PREFS";
    private static final String IS_USER_REGISTERED = "isUserRegistered";

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Context context;

    /**
     * Initializes the Preference Helper class
     *
     * @param ctx
     */
    public synchronized static void init(Context ctx) {
        context = ctx;
        prefs = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.commit();
    }

    /**
     * Set user registration flag
     *
     * @param isUserRegistered
     */
    public synchronized static void setUserRegistered(boolean isUserRegistered) {
        editor.putBoolean(IS_USER_REGISTERED, isUserRegistered);
        editor.commit();
    }

    /**
     * Check if user is registered or not
     *
     * @return
     */
    public synchronized static boolean isUserRegistered() {
        return prefs.getBoolean(IS_USER_REGISTERED, false);
    }

}
