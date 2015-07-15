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
        Parse.initialize(this, "Parse App Id", "Parse Client Key");
    }
}
