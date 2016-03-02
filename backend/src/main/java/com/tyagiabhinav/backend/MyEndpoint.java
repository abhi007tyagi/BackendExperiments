/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.tyagiabhinav.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
    public User registerUser(User user) throws ConflictException {
        if(user.getId() != null){
            if(findRecord(user.getId()) != null){
                throw new ConflictException("Object already exists");
            }
            else{
                ofy().save().entity(user).now();
            }
        }
        return user;
    }

    //Private method to retrieve a <code>User</code> record
    private User findRecord(String id) {
        return ofy().load().type(User.class).id(id).now();
    }

}
