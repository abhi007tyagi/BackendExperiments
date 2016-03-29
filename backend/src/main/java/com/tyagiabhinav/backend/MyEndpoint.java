package com.tyagiabhinav.backend;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;
import com.tyagiabhinav.IDGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tyagiabhinav.backend.OfyService.ofy;


/** An endpoint class we are exposing */
@Api(
  name = "backendService",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.tyagiabhinav.com",
    ownerName = "backend.tyagiabhinav.com",
    packagePath=""
  )
)
public class MyEndpoint {

    public static final String CALENDAR_FORMAT = "MM/dd/yyyy h:mm a";
    private long BUFFER_DAYS = 5;

//    @ApiMethod(name = "listUsers")
//    public CollectionResponse<User> listUser(@Nullable @Named("cursor") String cursorString,
//                                               @Nullable @Named("count") Integer count) {
//
//        Query<User> query = ofy().load().type(User.class);
//        if (count != null) query.limit(count);
//        if (cursorString != null && cursorString != "") {
//            query = query.startAt(Cursor.fromWebSafeString(cursorString));
//        }
//
//        List<User> users = new ArrayList<User>();
//        QueryResultIterator<User> iterator = query.iterator();
//        int num = 0;
//        while (iterator.hasNext()) {
//            users.add(iterator.next());
//            if (count != null) {
//                num++;
//                if (num == count) break;
//            }
//        }
//
//        //Find the next cursor
//        if (cursorString != null && cursorString != "") {
//            Cursor cursor = iterator.getCursor();
//            if (cursor != null) {
//                cursorString = cursor.toWebSafeString();
//            }
//        }
//        return CollectionResponse.<User>builder().setItems(users).setNextPageToken(cursorString).build();
//    }
//
//    @ApiMethod(name = "registerUser")
//    public User registerUser(User user) throws ConflictException, NotFoundException {
//        if(user.getEmail() != null){
//            if(findUserRecord(user.getEmail()) != null){
//                throw new ConflictException("User already exists");
//            }
//            else{
//                ofy().save().entity(user).now();
//            }
//        }
//        return user;
//    }
//
//    /**
//     * This updates an existing <code>User</code> object.
//     * @param user The object to be added.
//     * @return The object to be updated.
//     */
//    @ApiMethod(name = "updateUser")
//    public User updateUser(User user)throws NotFoundException {
//        User savedUser = findUserRecord(user.getEmail());
//        if (savedUser == null) {
//            throw new NotFoundException("User Record does not exist.. can't be updated");
//        }else{
//            if(user.getName() == null || user.getName().trim().isEmpty()){
//                user.setName(savedUser.getName());
//            }
//            if(user.getContact() == null || user.getContact().trim().isEmpty()){
//                user.setContact(savedUser.getContact());
//            }
//            if(user.getAdd1() == null || user.getAdd1().trim().isEmpty()){
//                user.setAdd1(savedUser.getAdd1());
//            }
//            if(user.getAdd2() == null || user.getAdd2().trim().isEmpty()){
//                user.setAdd2(savedUser.getAdd2());
//            }
//            if(user.getCity() == null || user.getCity().trim().isEmpty()){
//                user.setCity(savedUser.getCity());
//            }
//            if(user.getState() == null || user.getState().trim().isEmpty()){
//                user.setState(savedUser.getState());
//            }
//            if(user.getCountry() == null || user.getCountry().trim().isEmpty()){
//                user.setCountry(savedUser.getCountry());
//            }
//            if(user.getZip() == null || user.getZip().trim().isEmpty()){
//                user.setZip(savedUser.getZip());
//            }
//        }
//        ofy().save().entity(user).now();
//        return user;
//    }
//
//    /**
//     * This deletes an existing <code>User</code> object.
//     * @param id The id of the object to be deleted.
//     */
//    @ApiMethod(name = "removeUser")
//    public void removeUser(@Named("id") String id) throws NotFoundException {
//        User user = findUserRecord(id);
//        if(user == null) {
//            throw new NotFoundException("User Record does not exist.. can't be removed");
//        }
//        ofy().delete().entity(user).now();
//    }
//
//    /**
//     * This fetches an existing <code>User</code> object.
//     * @param id The id of the object to be deleted.
//     */
//    @ApiMethod(name = "getUser")
//    public User getUser(@Named("id") String id) throws NotFoundException {
//        User user = ofy().load().type(User.class).id(id).now();
//        if(user == null){
//            throw new NotFoundException("User Record does not exist");
//        }
//        return user;
//    }
//
//    //Private method to retrieve a <code>User</code> record
//    private User findUserRecord(String id) {
//        return ofy().load().type(User.class).id(id).now();
//        //or return ofy().load().type(User.class).filter("id",id).first.now();
//    }



    @ApiMethod(name = "listInvitations")
    public CollectionResponse<Invitation> listInvitations(@Nullable @Named("cursor") String cursorString,
                                             @Nullable @Named("count") Integer count) {

        Query<Invitation> query = ofy().load().type(Invitation.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Invitation> invitations = new ArrayList<Invitation>();
        QueryResultIterator<Invitation> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            invitations.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }

        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<Invitation>builder().setItems(invitations).setNextPageToken(cursorString).build();
    }

    @ApiMethod(name = "registerInvitation")
    public Invitation registerInvitation(Invitation invitation) throws ConflictException, NotFoundException {
        invitation.setId(IDGenerator.getRandomID());
        if(invitation.getId() != null){
            while(findInvitationRecord(invitation.getId()) != null){
//                throw new ConflictException("Invitation already exists");
                invitation.setId(IDGenerator.getRandomID());
            }
//            else{
                ofy().save().entity(invitation).now();
//            }
        }
        return invitation;
    }

    /**
     * This updates an existing <code>Invitation</code> object.
     * @param invitation The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateInvitation")
    public Invitation updateInvitation(Invitation invitation)throws NotFoundException {
        Invitation savedInvitation = findInvitationRecord(invitation.getId());
        if (savedInvitation == null) {
            throw new NotFoundException("Invitation Record does not exist.. can't be updated");
        }else{
            if(invitation.getTitle() == null || invitation.getTitle().trim().isEmpty()){
                invitation.setTitle(savedInvitation.getTitle());
            }
            if(invitation.getType() == null || invitation.getType().trim().isEmpty()){
                invitation.setType(savedInvitation.getType());
            }
            if(invitation.getMessage() == null || invitation.getMessage().trim().isEmpty()){
                invitation.setMessage(savedInvitation.getMessage());
            }
            if(invitation.getTime() == null || invitation.getTime().trim().isEmpty()){
                invitation.setTime(savedInvitation.getTime());
            }
            if(invitation.getDate() == null || invitation.getDate().trim().isEmpty()){
                invitation.setDate(savedInvitation.getDate());
            }
            if(invitation.getWebsite() == null || invitation.getWebsite().trim().isEmpty()){
                invitation.setWebsite(savedInvitation.getWebsite());
            }
            if(invitation.getVenueName() == null || invitation.getVenueName().trim().isEmpty()){
                invitation.setVenueName(savedInvitation.getVenueName());
            }
//            if(invitation.getVenueEmail() == null || invitation.getVenueEmail().trim().isEmpty()){
//                invitation.setVenueEmail(savedInvitation.getVenueEmail());
//            }
//            if(invitation.getVenueAdd1() == null || invitation.getVenueAdd1().trim().isEmpty()){
//                invitation.setVenueAdd1(savedInvitation.getVenueAdd1());
//            }
//            if(invitation.getVenueAdd2() == null || invitation.getVenueAdd2().trim().isEmpty()){
//                invitation.setVenueAdd2(savedInvitation.getVenueAdd2());
//            }
//            if(invitation.getVenueCity() == null || invitation.getVenueCity().trim().isEmpty()){
//                invitation.setVenueCity(savedInvitation.getVenueCity());
//            }
//            if(invitation.getVenueState() == null || invitation.getVenueState().trim().isEmpty()){
//                invitation.setVenueState(savedInvitation.getVenueState());
//            }
//            if(invitation.getVenueCountry() == null || invitation.getVenueCountry().trim().isEmpty()){
//                invitation.setVenueCountry(savedInvitation.getVenueCountry());
//            }
//            if(invitation.getVenueZip() == null || invitation.getVenueZip().trim().isEmpty()){
//                invitation.setVenueZip(savedInvitation.getVenueZip());
//            }
            if(invitation.getVenueAddress() == null || invitation.getVenueAddress().trim().isEmpty()){
                invitation.setVenueAddress(savedInvitation.getVenueAddress());
            }
            if(invitation.getPlaceID() == null || invitation.getPlaceID().trim().isEmpty()){
                invitation.setPlaceID(savedInvitation.getPlaceID());
            }
            if(invitation.getLatitude() == null || invitation.getLatitude().trim().isEmpty()){
                invitation.setLatitude(savedInvitation.getLatitude());
            }
            if(invitation.getLongitude() == null || invitation.getLongitude().trim().isEmpty()){
                invitation.setLongitude(savedInvitation.getLongitude());
            }
            if(invitation.getInvitee() != null){
                User savedUser = savedInvitation.getInvitee();
                User user = invitation.getInvitee();
                if(user.getName() == null || user.getName().trim().isEmpty()){
                    user.setName(savedUser.getName());
                }
                if(user.getContact() == null || user.getContact().trim().isEmpty()){
                    user.setContact(savedUser.getContact());
                }
                if(user.getAdd1() == null || user.getAdd1().trim().isEmpty()){
                    user.setAdd1(savedUser.getAdd1());
                }
                if(user.getAdd2() == null || user.getAdd2().trim().isEmpty()){
                    user.setAdd2(savedUser.getAdd2());
                }
                if(user.getCity() == null || user.getCity().trim().isEmpty()){
                    user.setCity(savedUser.getCity());
                }
                if(user.getState() == null || user.getState().trim().isEmpty()){
                    user.setState(savedUser.getState());
                }
                if(user.getCountry() == null || user.getCountry().trim().isEmpty()){
                    user.setCountry(savedUser.getCountry());
                }
                if(user.getZip() == null || user.getZip().trim().isEmpty()){
                    user.setZip(savedUser.getZip());
                }

                invitation.setInvitee(user);
            }
        }
        ofy().save().entity(invitation).now();
        return invitation;
    }

    /**
     * This deletes an existing <code>Invitation</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeInvitation")
    public void removeInvitation(@Named("id") String id) throws NotFoundException {
        Invitation invitation = findInvitationRecord(id);
        if(invitation == null) {
            throw new NotFoundException("Invitation Record does not exist.. can't be removed");
        }
        ofy().delete().entity(invitation).now();
    }

    /**
     * This fetches an existing <code>Invitation</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "getInvitation")
    public Invitation getInvitation(@Named("id") String id) throws NotFoundException {
        Invitation invitation = ofy().load().type(Invitation.class).id(id).now();
        if(invitation == null){
            throw new NotFoundException("Invitation Record does not exist");
        }
        return invitation;
    }

    /**
     * This deletes an existing <code>Invitation</code> object.
     */
    @ApiMethod(name = "cleanInvitation")
    public void cleanInvitation() throws NotFoundException {
        CollectionResponse<Invitation> invitations = listInvitations(null, null);
        for (Invitation invite : invitations.getItems()) {
                if(isOld(invite.getTime(), invite.getDate())){
                    removeInvitation(invite.getId());
                }
        }
    }

    //Private method to retrieve a <code>Invitation</code> record
    private Invitation findInvitationRecord(String id) {
        return ofy().load().type(Invitation.class).id(id).now();
    }

    private boolean isOld(String time, String date){
        Calendar today = Calendar.getInstance();
        Calendar inviteDate = Calendar.getInstance();
        inviteDate.set(Calendar.SECOND, 0);
        inviteDate.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat(CALENDAR_FORMAT);
        try {
            Date selectedDate = sdf.parse(date+" "+time);
            inviteDate.setTimeInMillis(selectedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(inviteDate.before(today)){
            long difference = today.getTimeInMillis() - inviteDate.getTimeInMillis();
            if(difference/(24 * 60 * 60 * 1000) > BUFFER_DAYS){
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

}
