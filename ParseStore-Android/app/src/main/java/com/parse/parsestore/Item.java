package com.parse.parsestore;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by madhav on 7/11/15.
 */
@ParseClassName("Item")
public class Item extends ParseObject {

    private String name;
    private Number price;
    private boolean hasSize;

    public Item(){

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return getString("description");
    }

    public void setDescription(String description){
        put("description",description);
    }


    public Number getPrice() {
        return getNumber("price");
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean hasSize() {
        return getBoolean("hasSize");
    }

    public Number getQuantity(){
        return getNumber("quantityAvailable");
    }
}
