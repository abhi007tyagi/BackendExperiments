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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mypopsy.maps.StaticMap;
import com.squareup.picasso.Picasso;
import com.tyagiabhinav.backend.backendService.model.Invitation;
import com.tyagiabhinav.backend.backendService.model.User;
import com.tyagiabhinav.einvite.DB.DBContract;
import com.tyagiabhinav.einvite.R;
import com.tyagiabhinav.einvite.Util.Encrypt;
import com.tyagiabhinav.einvite.Util.Util;

import java.net.MalformedURLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhinavtyagi on 20/03/16.
 */
public class InvitationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{//}, ParallaxScrollView.OnScrollChangedListener {
    private static final String LOG_TAG = InvitationFragment.class.getSimpleName();
    private View rootView;
    private String inviteID;
    private Invitation invitation;
    private static final int CURSOR_LOADER = 27;


    public static final String INVITATION_ID = "inviteID";
    private String lat = "", lon = "";

    @Bind(R.id.toolbar)
    Toolbar toolbar;


//    @Bind(R.id.scrollView)
//    ParallaxScrollView scrollView;

//    @Bind(R.id.imgLayout)
//    LinearLayout imgLayout;

//    @Bind(R.id.parallaxFrame)
//    FrameLayout parallaxFrame;

//    @Bind(R.id.title)
//    TextView title;

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.typeImg)
    ImageView typeImg;

    @Bind(R.id.message)
    TextView message;

    @Bind(R.id.map)
    ImageView map;

    @Bind(R.id.venueName)
    TextView venueName;

    @Bind(R.id.venueAddress)
    TextView venueAddress;

    @Bind(R.id.venuePhone)
    TextView venuePhone;

    @Bind(R.id.venueWebsite)
    TextView venueWebsite;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.timeDate)
    TextView timeDate;

    @Bind(R.id.address)
    TextView address;

    @Bind(R.id.phone)
    TextView phone;

    @Bind(R.id.email)
    TextView email;

    public InvitationFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.invitation_fragment, container, false);
        ButterKnife.bind(this, rootView);

//        scrollView.setOnScrollChangedListener(this);
        if (toolbar != null) {
            ((InvitationActivity)getActivity()).setSupportActionBar(toolbar);
            ((InvitationActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        inviteID = getArguments().getString(INVITATION_ID);

//        parallaxFrame.setBackgroundResource(android.R.color.transparent);

        Log.d(LOG_TAG, "InviteID -> " + inviteID);
        return rootView;
    }


    @OnClick(R.id.navigateFab)
    public void navigate() {
        Log.d(LOG_TAG, "navigate");

        String navURL = "";
        if (!Util.isNull(lat) && !Util.isNull(lon)) {
            navURL = "http://maps.google.com/maps?f=d&source=s_d&daddr=" + lat + "," + lon + "&hl=zh&t=m&dirflg=d";
        } else {
            navURL = "http://maps.google.com/maps?f=d&source=s_d&daddr=" + venueAddress.getText().toString() + "&hl=zh&t=m&dirflg=d";
        }
        Log.d(LOG_TAG, "NAV_URL-->" + navURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navURL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate menu resource file.
        inflater.inflate(R.menu.menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());

    }

    private Intent createShareForecastIntent() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (invitation != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, invitation.getMessage() + getResources().getString(R.string.invite_msg2) + "http://tyagiabhinav.com/einvite?id=" + inviteID);
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.invite_msg1) + "http://tyagiabhinav.com/einvite?id=" + inviteID);
        }
        return shareIntent;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Activity Created");
        getLoaderManager().initLoader(CURSOR_LOADER, getArguments(), this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader ID-->" + bundle.getString(InvitationFragment.INVITATION_ID));

        Uri invitation = DBContract.InviteEntry.buildInvitationDataUri(bundle.getString(InvitationFragment.INVITATION_ID));
        return new CursorLoader(getContext(),
                invitation,
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
            invitation = new Invitation();

            User user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_EMAIL)));
            user.setContact(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_CONTACT)));
            user.setAdd1(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_ADD1)));
            user.setAdd2(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_ADD2)));
            user.setCity(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_CITY)));
            user.setState(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_STATE)));
            user.setCountry(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_COUNTRY)));
            user.setZip(cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COL_USER_ZIP)));

            invitation.setInvitee(user);

            invitation.setId(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_ID)));
            invitation.setTitle(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_TITLE)));
            invitation.setType(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_TYPE)));
            invitation.setMessage(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_MESSAGE)));
            invitation.setTime(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_TIME)));
            invitation.setDate(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_DATE)));
            invitation.setVenueName(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_VENUE_NAME)));
            invitation.setVenueAddress(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_VENUE_ADDRESS)));
            invitation.setVenueContact(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_VENUE_CONTACT)));
            invitation.setWebsite(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_WEBSITE)));
            invitation.setLatitude(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_VENUE_LATITUDE)));
            invitation.setLongitude(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_VENUE_LONGITUDE)));
            invitation.setPlaceID(cursor.getString(cursor.getColumnIndex(DBContract.InviteEntry.COL_PLACE_ID)));


            //populate UI
//            title.setText(invitation.getTitle());
            setInviteTypeImg(invitation.getType());
            collapsingToolbarLayout.setTitle(invitation.getTitle());
            try {
                message.setText(Encrypt.doAESDecryption(invitation.getMessage()));
                timeDate.setText(invitation.getTime() + " on " + invitation.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }

            venueName.setText(invitation.getVenueName());
            venueAddress.setText(invitation.getVenueAddress());
            String vPhone = invitation.getVenueContact();
            if (vPhone != null && !vPhone.trim().isEmpty() && !vPhone.equalsIgnoreCase("null")) {
                venuePhone.setText(vPhone);
            } else {
                venuePhone.setVisibility(View.GONE);
            }
            String vWeb = invitation.getWebsite();
            if (vWeb != null && !vWeb.trim().isEmpty() && !vWeb.equalsIgnoreCase("null")) {
                venueWebsite.setText(vWeb);
            } else {
                venueWebsite.setVisibility(View.GONE);
            }

            name.setText(user.getName());
            StringBuilder add = new StringBuilder();
            try {
                add.append(Encrypt.doAESDecryption(user.getAdd1()) + ", ");


                String add2 = user.getAdd2();
                if (add2 != null && !add2.trim().isEmpty() && !add2.equalsIgnoreCase("null")) {
                    add.append(Encrypt.doAESDecryption(add2) + ", ");
                }

                add.append(Encrypt.doAESDecryption(user.getCity()) + ", ");
                add.append(Encrypt.doAESDecryption(user.getState()) + ", ");
                add.append(Encrypt.doAESDecryption(user.getCountry()));

                String zip =user.getZip();
                if (zip != null) {
                    zip = Encrypt.doAESDecryption(zip);
                    if (!zip.trim().isEmpty() && !zip.equalsIgnoreCase("null")) {
                        add.append("-" + zip);
                    }
                }

                address.setText(add.toString());
                email.setText(Encrypt.doAESDecryption(user.getEmail()));
                phone.setText(Encrypt.doAESDecryption(user.getContact()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = "";
            if (!Util.isNull(invitation.getLatitude()) && !Util.isNull(invitation.getLongitude())) {
                // lat-lon info present
                lat = invitation.getLatitude();
                lon = invitation.getLongitude();
                StaticMap mapUri = new StaticMap()
                        .size(1000, 300)
                        .marker(StaticMap.Marker.Style.RED.toBuilder().build(), new StaticMap.GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon)));
                try {
                    url = String.valueOf(mapUri.toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                // only address present
                StaticMap mapUri = new StaticMap()
                        .size(1000, 300)
                        .marker(StaticMap.Marker.Style.RED.toBuilder().build(), new StaticMap.GeoPoint(venueAddress.getText().toString()));
                try {
                    url = String.valueOf(mapUri.toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            Log.d(LOG_TAG, "Map URI -> " + url);
            Picasso.with(getActivity()).load(url).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(map);
//            new DownloadImageTask(map).execute(url);

            Log.d(LOG_TAG, "User -> " + user.getName() + " Venue -> " + invitation.getVenueName());
        }
        Log.d(LOG_TAG, "Destroying Loader");
        getLoaderManager().destroyLoader(CURSOR_LOADER);
    }

    private void setInviteTypeImg(String type) {
        try {
            int inviteType = Integer.parseInt(type);
            switch (inviteType) {
                case Type.BIRTHDAY:
                    typeImg.setImageResource(R.drawable.bday);
                    break;
                case Type.WEDDING:
                    typeImg.setImageResource(R.drawable.wed);
                    break;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
//            if (type.equalsIgnoreCase("Birthday")) {
//                typeImg.setImageResource(R.drawable.bday);
////            collapsingToolbarLayout.setBackgroundResource(R.color.birthday_bg);
////            collapsingToolbarLayout.setContentScrimResource(R.color.birthday_bg);
////            parallaxFrame.setBackgroundResource(R.color.birthday_bg);
////            title.setBackgroundResource(R.color.birthday_bg);
//            } else if (type.equalsIgnoreCase("Marriage")) {
//                typeImg.setImageResource(R.drawable.wed);
////            collapsingToolbarLayout.setBackgroundResource(R.color.wedding_bg);
////            collapsingToolbarLayout.setContentScrimResource(R.color.wedding_bg);
////            parallaxFrame.setBackgroundResource(R.color.wedding_bg);
////            title.setBackgroundResource(R.color.wedding_bg);
//            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

//    @Override
//    public void onScrollChanged(int deltaX, int deltaY) {
////        Log.d(LOG_TAG,"onScrollChanged dx:dy -> "+deltaX+":"+deltaY);
//        int scrollY = scrollView.getScrollY();
//
//        int max = title.getHeight();
////        Log.d(LOG_TAG,"onScrollChanged max -> "+max);
////        Log.d(LOG_TAG,"onScrollChanged title x -> "+title.getX());
////        Log.d(LOG_TAG,"onScrollChanged title y -> "+title.getY());
//
////        if(deltaY > 0){
////            title.setTranslationY(Math.max(max, title.getTranslationY()-deltaY/2));
////        } else {
////            title.setTranslationY(Math.min(0, title.getTranslationY()-deltaY/2));
////        }
//        // Add parallax effect
//        parallaxFrame.setTranslationY(scrollY * 0.7f);
//
////        title.setTranslationY(scrollY * 0.1f);
//
//
//    }


//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }
}
