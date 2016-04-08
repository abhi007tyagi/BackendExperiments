package com.tyagiabhinav.einvite.UI;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by abhinavtyagi on 06/04/16.
 */
public class Type {


    private int type;

    public static final int BIRTHDAY = 0;
    public static final int WEDDING = 1;

    @IntDef({BIRTHDAY, WEDDING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface InviteType {
    }

    @InviteType
    public int getType() {
        return type;
    }

    public void setType(@InviteType int type) {
        this.type = type;
    }

}
