package com.example.electroshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shopDb";
    public static final String TABLE_PRODUCTS = "products";

    public static final String KEY_PRODID = "idProd";
    public static final String KEY_PRODNAME = "prodName";
    public static final String KEY_PRODPRICE = "price";
    public static final String KEY_PRODDESCR = "description";


    public static final String TABLE_ORDERS = "orders";

    public static final String KEY_ORDERID = "idOrd";
    public static final String KEY_ADDREESS = "address";
    public static final String KEY_CUSTOMERNAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_COST = "cost";

    public static final String TABLE_PRODLIST = "productList";

    public static final String KEY_LISTID = "idList";
    public static final String KEY_LISTORDID = "idListOrder";
    public static final String KEY_PRODUCTID = "idListProd";
    public static final String KEY_QUANTITY = "quantity";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PRODUCTS + "(" + KEY_PRODID
                + " integer primary key," + KEY_PRODNAME + " text," + KEY_PRODPRICE + " float," + KEY_PRODDESCR + " text" + ")");
        db.execSQL("create table " + TABLE_ORDERS + "(" + KEY_ORDERID
                 + " text," + KEY_CUSTOMERNAME + " integer primary key," + KEY_ADDREESS + " text," + KEY_PHONE + " integer," + KEY_COST + " float" + ")");
        db.execSQL("create table " + TABLE_PRODLIST + "(" + KEY_LISTID
                + " integer primary key," + KEY_LISTORDID + " integer," + KEY_PRODUCTID + " integer," + KEY_QUANTITY + " integer" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PRODUCTS + TABLE_ORDERS + TABLE_PRODLIST);

        onCreate(db);

    }
}