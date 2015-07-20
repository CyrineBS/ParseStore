package com.parse.parsestore;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Madhav Chhura on 7/11/15.
 */
@ParseClassName("Item")
public class Item extends ParseObject {

    private String name;
    private String description;
    private Number price;
    private boolean hasSize;
    private String size;


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

    public void setSize(String size){
        if(hasSize())
            this.size = size;
        else
            Log.d("Item", "Cannot Set Size for this item");
    }
    public String getSize(){
        return size;
    }

    public Number getQuantity(){
        return getNumber("quantityAvailable");
    }

    public ParseFile getItemImage(){
        return getParseFile("image");
    }

}
