package com.example.electroshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class OrderTaker extends AppCompatActivity implements View.OnClickListener{

    DBHelper dbHelper;
    SQLiteDatabase database;
    EditText Name, Address, Phone;
    Button btnOrder;
    ContentValues contentValues;
    int cost;
    ArrayList<List<Integer>> prodInfo = new ArrayList<List<Integer>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Name = findViewById(R.id.etName);
        Address = findViewById(R.id.etAddress);
        Phone = findViewById(R.id.etPhone);
        btnOrder = findViewById(R.id.btnSend);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        cost = Integer.parseInt(getIntent().getStringExtra("Cost"));
        prodInfo = getIntent().getParcelableExtra("ProdInfo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOrder:
                contentValues = new ContentValues();
                String name, address;
                Integer phone;
                name = Phone.getText().toString();
                address = Address.getText().toString();
                phone = Integer.parseInt(Phone.getText().toString());
                int count = (int)DatabaseUtils.queryNumEntries(database, "TABLE_ORDERS");
                contentValues.put(DBHelper.KEY_CUSTOMERNAME, name);
                contentValues.put(DBHelper.KEY_ADDREESS, address);
                contentValues.put(DBHelper.KEY_PHONE, phone);
                contentValues.put(DBHelper.KEY_COST, cost);
                database.insert(DBHelper.TABLE_ORDERS, null, contentValues);
                contentValues.clear();
                for(int i = 0; i < prodInfo.size()-1; i++){
                    contentValues.put(DBHelper.KEY_LISTORDID, count);
                    contentValues.put(DBHelper.KEY_PRODID, prodInfo.get(i).get(0));
                    contentValues.put(DBHelper.KEY_QUANTITY, prodInfo.get(i).get(1));
                    database.insert(DBHelper.TABLE_PRODLIST, null, contentValues);
                    contentValues.clear();
                    }
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setText("Заказ на сумму " + cost + " составлен.");
                toast.show();
                break;
        }


    }
}
