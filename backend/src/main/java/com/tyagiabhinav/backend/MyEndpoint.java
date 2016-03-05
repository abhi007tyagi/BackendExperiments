/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.tyagiabhinav.backend;

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

import java.util.ArrayList;
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


    @ApiMethod(name = "listUsers")
    public CollectionResponse<User> listQuote(@Nullable @Named("cursor") String cursorString,
                                               @Nullable @Named("count") Integer count) {

        Query<User> query = ofy().load().type(User.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<User> users = new ArrayList<User>();
        QueryResultIterator<User> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            users.add(iterator.next());
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
        return CollectionResponse.<User>builder().setItems(users).setNextPageToken(cursorString).build();
    }

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "registerUser")
    public User registerUser(User user) throws ConflictException, NotFoundException {
        if(user.getId() != null){
            if(findRecord(user.getId()) != null){
                throw new ConflictException("User already exists");
            }
            else{
                ofy().save().entity(user).now();
            }
        }
        return user;
    }

    /**
     * This updates an existing <code>User</code> object.
     * @param user The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateUser")
    public User updateUser(User user)throws NotFoundException {
        User savedUser = findRecord(user.getId());
        if (savedUser == null) {
            throw new NotFoundException("User Record does not exist.. can't be updated");
        }else{
            if(user.getName() == null || user.getName().trim().isEmpty()){
                user.setName(savedUser.getName());
            }
            if(user.getAddress() == null || user.getAddress().trim().isEmpty()){
                user.setAddress(savedUser.getAddress());
            }
            if(user.getContact1() == null || user.getContact1().trim().isEmpty()){
                user.setContact1(savedUser.getContact1());
            }
            if(user.getContact2() == null || user.getContact2().trim().isEmpty()){
                user.setContact2(savedUser.getContact2());
            }
        }
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This deletes an existing <code>User</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeUser")
    public void removeUser(@Named("id") String id) throws NotFoundException {
        User user = findRecord(id);
        if(user == null) {
            throw new NotFoundException("User Record does not exist.. can't be removed");
        }
        ofy().delete().entity(user).now();
    }

    /**
     * This fetches an existing <code>User</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "getUser")
    public User getUser(@Named("id") String id) throws NotFoundException {
        User user = ofy().load().type(User.class).id(id).now();
        if(user == null){
            throw new NotFoundException("User Record does not exist");
        }
        return user;
    }

    //Private method to retrieve a <code>User</code> record
    private User findRecord(String id) {
        return ofy().load().type(User.class).id(id).now();
        //or return ofy().load().type(User.class).filter("id",id).first.now();
    }

}
