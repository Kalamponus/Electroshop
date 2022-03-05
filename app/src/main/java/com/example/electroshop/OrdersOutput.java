package com.example.electroshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class OrdersOutput extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderslist);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public String GetOrderGoods(String orderId)
    {
        String orderedGoodsList = "";
        Cursor c = database.rawQuery("SELECT idListProd, quantity FROM productList WHERE idListOrder = " + orderId, null);
        if (c.moveToFirst())
        {
            do
            {
                String goodId = c.getString(0);
                String quantity = c.getString(1);
                Cursor c2= database.rawQuery("SELECT prodName FROM products WHERE idProd = " + goodId, null);
                if (c2.moveToFirst())
                {
                    do
                    {
                        String goodName = c2.getString(0);
                        orderedGoodsList += goodName + " x " + quantity +";\n";
                    }
                    while(c2.moveToNext());
                }
                c2.close();
            }
            while(c.moveToNext());
        }
        c.close();
        return orderedGoodsList;
    }

    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_ORDERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ORDERID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_CUSTOMERNAME);
            int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDREESS);
            int phoneIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
            int costIndex = cursor.getColumnIndex(DBHelper.KEY_COST);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputAddress = new TextView(this);
                params.weight = 3.0f;
                outputAddress.setLayoutParams(params);
                outputAddress.setText(cursor.getString(addressIndex));
                dbOutputRow.addView(outputAddress);

                TextView outputPhone = new TextView(this);
                params.weight = 3.0f;
                outputPhone.setLayoutParams(params);
                outputPhone.setText(cursor.getString(phoneIndex));
                dbOutputRow.addView(outputPhone);

                TextView outputCost = new TextView(this);
                params.weight = 3.0f;
                outputCost.setLayoutParams(params);
                outputCost.setText(cursor.getString(costIndex));
                dbOutputRow.addView(outputCost);

                TextView outputProd = new TextView(this);
                params.weight = 3.0f;
                outputProd.setLayoutParams(params);
                String id = cursor.getString(idIndex);
                outputProd.setText(GetOrderGoods(id));
                dbOutputRow.addView(outputProd);

                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }

    @Override
    public void onClick(View v) {

    }
}
