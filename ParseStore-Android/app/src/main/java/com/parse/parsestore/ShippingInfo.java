package com.parse.parsestore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Madhav Chhura on 7/15/15.
 */
public class ShippingInfo implements Parcelable {

    private String name, email, address, cityState, postalCode;

    public ShippingInfo(String name, String email, String address,
                        String cityState, String postalCode){
        this.name = name;
        this.email = email;
        this.address = address;
        this.cityState = cityState;
        this.postalCode = postalCode;
    }

    // Parcelling part
    public ShippingInfo(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.name = data[0];
        this.email = data[1];
        this.address = data[2];
        this.cityState = data[3];
        this.postalCode = data[4];

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name,
                this.email,
                this.address,
                this.cityState,
                this.postalCode,});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ShippingInfo createFromParcel(Parcel in) {
            return new ShippingInfo(in);
        }

        public ShippingInfo[] newArray(int size) {
            return new ShippingInfo[size];
        }
    };
}
