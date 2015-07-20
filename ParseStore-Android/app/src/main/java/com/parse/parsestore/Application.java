package com.parse.parsestore;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Madhav Chhura on 7/11/15.
 */
public class Application extends android.app.Application{
    public void onCreate(){

        // Enable Local Datastore.
        Parse.enableLocalDatastore(getApplicationContext());

        ParseObject.registerSubclass(Item.class);
        Parse.initialize(this, "ParseApplicationID", "ParseClientKey");

    }
}
