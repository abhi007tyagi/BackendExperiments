package com.tyagiabhinav.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by abhinavtyagi on 04/03/16.
 */
public class OfyService {

    static {
        ObjectifyService.register(Invitation.class);
        ObjectifyService.register(User.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
