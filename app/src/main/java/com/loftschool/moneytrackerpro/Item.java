package com.loftschool.moneytrackerpro;

import java.io.Serializable;

/**
 * Created by fanre on 6/24/2017.
 */

public class Item implements Serializable {
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    public String name, type;
    public int id, price;


    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
