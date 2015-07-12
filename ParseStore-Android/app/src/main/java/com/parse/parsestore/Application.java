package com.parse.parsestore;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by madhav on 7/11/15.
 */
public class Application extends android.app.Application{
    public void onCreate(){
        // Enable Local Datastore.

        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(Item.class);
        Parse.initialize(this, "Z3OTbpQCILL7UHIskRT6PqEygMRg1B4DQx8CxMKQ", "nMiCzwvQnPODP2RPQyj6TBbCjalv3ZeuX5bZD78X");
    }
}
