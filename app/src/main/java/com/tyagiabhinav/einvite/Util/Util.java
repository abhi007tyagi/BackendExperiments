package com.tyagiabhinav.einvite.Util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract.InviteEntry;
import com.tyagiabhinav.einvite.DB.DBContract.UserEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by abhinavtyagi on 16/03/16.
 */
public class Util {
    private static DatePickerDialog datePicker;
    public  static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String TIME_DISPLAY_FORMAT = "h:mm a";
    public static final String TIME_PARSE_FORMAT = "H:mm";
    public static final String CALENDAR_FORMAT = "MM/dd/yyyy h:mm a";

    public static ContentValues getUserValues(User user, boolean isSelf) {
        ContentValues userValues = new ContentValues();
        userValues.put(UserEntry.COL_USER_EMAIL, user.getEmail());
        userValues.put(UserEntry.COL_USER_NAME, user.getName());
        userValues.put(UserEntry.COL_USER_CONTACT, user.getContact());
        userValues.put(UserEntry.COL_USER_ADD1, user.getAdd1());
        userValues.put(UserEntry.COL_USER_ADD2, user.getAdd2());
        userValues.put(UserEntry.COL_USER_CITY, user.getCity());
        userValues.put(UserEntry.COL_USER_STATE, user.getState());
        userValues.put(UserEntry.COL_USER_COUNTRY, user.getCountry());
        userValues.put(UserEntry.COL_USER_ZIP, user.getZip());
        if (isSelf) {
            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_SELF);
        } else {
            userValues.put(UserEntry.COL_USER_TYPE, UserEntry.USER_TYPE_OTHER);
        }

        return userValues;
    }

    public static ContentValues getInvitationValues(Invitation invitation) {
        ContentValues inviteValues = new ContentValues();
        inviteValues.put(InviteEntry.COL_ID, invitation.getId());
        inviteValues.put(InviteEntry.COL_TITLE, invitation.getTitle());
        inviteValues.put(InviteEntry.COL_TYPE, invitation.getType());
        inviteValues.put(InviteEntry.COL_MESSAGE, invitation.getMessage());
        inviteValues.put(InviteEntry.COL_TIME, invitation.getTime());
        inviteValues.put(InviteEntry.COL_DATE, invitation.getDate());
        inviteValues.put(InviteEntry.COL_WEBSITE, invitation.getWebsite());
        inviteValues.put(InviteEntry.COL_VENUE_NAME, invitation.getVenueName());
//        inviteValues.put(InviteEntry.COL_VENUE_EMAIL, invitation.getVenueEmail());
        inviteValues.put(InviteEntry.COL_VENUE_CONTACT, invitation.getVenueContact());
        inviteValues.put(InviteEntry.COL_VENUE_ADDRESS, invitation.getVenueAddress());
//        inviteValues.put(InviteEntry.COL_VENUE_ADD1, invitation.getVenueAdd1());
//        inviteValues.put(InviteEntry.COL_VENUE_ADD2, invitation.getVenueAdd2());
//        inviteValues.put(InviteEntry.COL_VENUE_CITY, invitation.getVenueCity());
//        inviteValues.put(InviteEntry.COL_VENUE_STATE, invitation.getVenueState());
//        inviteValues.put(InviteEntry.COL_VENUE_COUNTRY, invitation.getVenueCountry());
//        inviteValues.put(InviteEntry.COL_VENUE_ZIP, invitation.getVenueZip());
        inviteValues.put(InviteEntry.COL_VENUE_LATITUDE, invitation.getLatitude());
        inviteValues.put(InviteEntry.COL_VENUE_LONGITUDE, invitation.getLongitude());
        inviteValues.put(InviteEntry.COL_PLACE_ID, invitation.getPlaceID());
        inviteValues.put(InviteEntry.COL_INVITEE, invitation.getInvitee().getEmail());

        return inviteValues;
    }

    public static void showDatePicker(final View fieldView, final Activity activity, final boolean doPreviousDateCheck) {
        final Calendar cal = Calendar.getInstance();
        int monthOfYear = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
//                final Date date = new Date(year - 1900, selectedMonth, selectedDay);
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.YEAR, selectedYear);
                selectedCalendar.set(Calendar.MONTH, selectedMonth);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                if (doPreviousDateCheck) {
                    if (cal.before(selectedCalendar)) {
                        final String selectedDate = dateFormat.format(selectedCalendar.getTime());
//                    selectedDate = Uri.encode(selectedDate);
//                    final String encodedDate = selectedDate;
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ((EditText) fieldView).setText(selectedDate);
                            }
                        });
                    } else {
                        Toast.makeText(activity, "Can not select previous date! ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    final String selectedDate = dateFormat.format(selectedCalendar.getTime());
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ((EditText) fieldView).setText(selectedDate);
                        }
                    });
                }
                datePicker.dismiss();
            }
        }, year, monthOfYear, day);
        datePicker.show();
    }

    public static void showTimepickerDialog(Context context, final View view) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int hourOfDay, final int minute) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ((EditText) view).setText(convert24to12(hourOfDay + ":" + editedTime(minute)));
                    }
                });
            }
        }, mHour, mMinute, false);
        tpd.show();
    }


    public static String editedTime(int minute){
        String  editedMinute = "" + minute;
        if(String.valueOf(minute).length() == 1){
            editedMinute = "0" + minute;
        }
        return editedMinute;
    }

    public static String convert24to12(String time) {
        {
            String convertedTime ="";
            try {
                SimpleDateFormat displayFormat = new SimpleDateFormat(TIME_DISPLAY_FORMAT);
                SimpleDateFormat parseFormat = new SimpleDateFormat(TIME_PARSE_FORMAT);
                Date date = parseFormat.parse(time);
                convertedTime=displayFormat.format(date);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            return convertedTime;
        }
    }

    public static boolean isOldTimeAndDate(String time, String date){
        Calendar presentCal = Calendar.getInstance();
        Calendar selectedCal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(CALENDAR_FORMAT);
        try {
            Date selectedDate = sdf.parse(date+" "+time);
            selectedCal.setTimeInMillis(selectedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return selectedCal.before(presentCal);
    }

    public static boolean isNull(String text){
        if(text != null && !text.trim().isEmpty() && !text.equalsIgnoreCase("null")){
            return false;
        }else{
            return true;
        }

    }

}
