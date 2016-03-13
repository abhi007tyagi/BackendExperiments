package com.tyagiabhinav.backend;

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
    String website;
    String venueName;
    String venueEmail;

    String venueContact;
    String venueAdd1;
    String venueAdd2;
    String venueCity;
    String venueState;
    String venueCountry;
    String venueZip;
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

    public String getVenueEmail() {
        return venueEmail;
    }

    public void setVenueEmail(String venueEmail) {
        this.venueEmail = venueEmail;
    }

    public String getVenueContact() {
        return venueContact;
    }

    public void setVenueContact(String venueContact) {
        this.venueContact = venueContact;
    }

    public String getVenueAdd1() {
        return venueAdd1;
    }

    public void setVenueAdd1(String venueAdd1) {
        this.venueAdd1 = venueAdd1;
    }

    public String getVenueAdd2() {
        return venueAdd2;
    }

    public void setVenueAdd2(String venueAdd2) {
        this.venueAdd2 = venueAdd2;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public String getVenueState() {
        return venueState;
    }

    public void setVenueState(String venueState) {
        this.venueState = venueState;
    }

    public String getVenueCountry() {
        return venueCountry;
    }

    public void setVenueCountry(String venueCountry) {
        this.venueCountry = venueCountry;
    }

    public String getVenueZip() {
        return venueZip;
    }

    public void setVenueZip(String venueZip) {
        this.venueZip = venueZip;
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
