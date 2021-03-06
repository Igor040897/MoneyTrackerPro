package com.igor040897.moneytrackerpro.API;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanre on 6/24/2017.
 */

public class Item implements Parcelable {
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    public String name, type;
    public int price;
    public int id = -1;

    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    private Item(Parcel in) {
        name = in.readString();
        price = in.readInt();
        type = in.readString();
        id = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(type);
        dest.writeInt(id);
    }
}
