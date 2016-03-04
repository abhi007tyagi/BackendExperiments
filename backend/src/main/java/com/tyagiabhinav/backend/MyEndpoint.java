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
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

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

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "registerUser")
    public User registerUser(User user) throws ConflictException, NotFoundException {
        if(user.getId() != null){
            if(getUser(user.getId()) != null){
                throw new ConflictException("User already exists");
            }
            else{
                ofy().save().entity(user).now();
            }
        }
        return user;
    }

    /**
     * This updates an existing <code>Quote</code> object.
     * @param user The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateUser")
    public User updateUser(User user)throws NotFoundException {
        if (getUser(user.getId()) == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This deletes an existing <code>Quote</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeUser")
    public void removeUser(@Named("id") String id) throws NotFoundException {
        User user = getUser(id);
        if(user == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().delete().entity(user).now();
    }

    @ApiMethod(name = "getUser")
    public User getUser(@Named("id") String id) throws NotFoundException {
        User user = ofy().load().type(User.class).id(id).now();
        if(user == null){
            throw new NotFoundException("User Record does not exist");
        }
        return user;
    }

}
