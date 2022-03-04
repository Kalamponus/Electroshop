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
    Button btnSend;
    ContentValues contentValues;
    String cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Name = findViewById(R.id.etName);
        Address = findViewById(R.id.etAddress);
        Phone = findViewById(R.id.etPhone);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        cost = Integer.toString(getIntent().getIntExtra("Cost", 0));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                contentValues = new ContentValues();
                String name, address, phone;
                name = Name.getText().toString();
                address = Address.getText().toString();
                phone = Phone.getText().toString();
                contentValues.put(DBHelper.KEY_CUSTOMERNAME, name);
                contentValues.put(DBHelper.KEY_ADDREESS, address);
                contentValues.put(DBHelper.KEY_PHONE, phone);
                contentValues.put(DBHelper.KEY_COST, cost);
                long ordId = database.insert(DBHelper.TABLE_ORDERS, null, contentValues);
                contentValues = new ContentValues();
                for(int i = 0; i < ProdInfoList.prodInfo.size(); i++){
                    contentValues.put(DBHelper.KEY_LISTORDID, Long.toString(ordId));
                    contentValues.put(DBHelper.KEY_PRODID, Integer.toString(ProdInfoList.prodInfo.get(i).indx));
                    contentValues.put(DBHelper.KEY_QUANTITY, Integer.toString(ProdInfoList.prodInfo.get(i).quant));
                    database.insert(DBHelper.TABLE_PRODLIST, null, contentValues);
                    contentValues.clear();
                    }
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setText("Заказ на сумму " + cost + " составлен.");
                toast.show();
                Intent intent = new Intent(this, OrdersOutput.class);
                startActivity(intent);
                break;
        }


    }
}
