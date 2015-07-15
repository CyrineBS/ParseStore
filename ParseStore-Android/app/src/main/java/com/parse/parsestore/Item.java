package com.parse.parsestore;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by madhav on 7/11/15.
 */
@ParseClassName("Item")
public class Item extends ParseObject {

    private String name;
    private Number price;
    private boolean hasSize;


    public String getName() {
        return getString("name");
    }


    public String getDescription(){
        return getString("description");
    }

    public Number getPrice() {
        return getNumber("price");
    }

    public boolean hasSize() {
        return getBoolean("hasSize");
    }

    public Number getQuantity(){
        return getNumber("quantityAvailable");
    }

    public ParseFile getItemImage(){
        return getParseFile("image");
    }

}
