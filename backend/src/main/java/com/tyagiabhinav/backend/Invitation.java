package com.tyagiabhinav.backend;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by abhinavtyagi on 12/03/16.
 */

@Entity
public class Invitation {

    @Id
    String id;
    String title;
    String type;
    String message;
    User invitee;
    String time;
    String date;
    String website;
    String venueName;
//    String venueEmail;

    String venueContact;
//    String venueAdd1;
//    String venueAdd2;
//    String venueCity;
//    String venueState;
//    String venueCountry;
//    String venueZip;
    String venueAddress;
    String placeID;
    String latitude;
    String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getInvitee() {
        return invitee;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

//    public String getVenueEmail() {
//        return venueEmail;
//    }
//
//    public void setVenueEmail(String venueEmail) {
//        this.venueEmail = venueEmail;
//    }

    public String getVenueContact() {
        return venueContact;
    }

    public void setVenueContact(String venueContact) {
        this.venueContact = venueContact;
    }

//    public String getVenueAdd1() {
//        return venueAdd1;
//    }
//
//    public void setVenueAdd1(String venueAdd1) {
//        this.venueAdd1 = venueAdd1;
//    }
//
//    public String getVenueAdd2() {
//        return venueAdd2;
//    }
//
//    public void setVenueAdd2(String venueAdd2) {
//        this.venueAdd2 = venueAdd2;
//    }
//
//    public String getVenueCity() {
//        return venueCity;
//    }
//
//    public void setVenueCity(String venueCity) {
//        this.venueCity = venueCity;
//    }
//
//    public String getVenueState() {
//        return venueState;
//    }
//
//    public void setVenueState(String venueState) {
//        this.venueState = venueState;
//    }
//
//    public String getVenueCountry() {
//        return venueCountry;
//    }
//
//    public void setVenueCountry(String venueCountry) {
//        this.venueCountry = venueCountry;
//    }
//
//    public String getVenueZip() {
//        return venueZip;
//    }
//
//    public void setVenueZip(String venueZip) {
//        this.venueZip = venueZip;
//    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
