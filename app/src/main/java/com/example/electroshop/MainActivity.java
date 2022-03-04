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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear, btnShop;
    EditText etName, etPrice, etDesc;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDesc = (EditText) findViewById(R.id.etDesc);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnShop = (Button) findViewById(R.id.btnShop);
        btnShop.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }
    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_PRODID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_PRODNAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRODPRICE);
            int descIndex = cursor.getColumnIndex(DBHelper.KEY_PRODDESCR);
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

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputPrice);

                TextView outputDesc = new TextView(this);
                params.weight = 3.0f;
                outputDesc.setLayoutParams(params);
                outputDesc.setText(cursor.getString(descIndex));
                dbOutputRow.addView(outputDesc);

                Button buttonDelete = new Button(this);
                buttonDelete.setOnClickListener(this);
                params.weight = 1.0f;
                buttonDelete.setLayoutParams(params);
                buttonDelete.setText("Удалить товар");
                buttonDelete.setTag("delete");
                buttonDelete.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(buttonDelete);

                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                contentValues = new ContentValues();
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                String desc = etDesc.getText().toString();
                contentValues.put(DBHelper.KEY_PRODNAME, name);
                contentValues.put(DBHelper.KEY_PRODPRICE, price);
                contentValues.put(DBHelper.KEY_PRODDESCR, desc);
                long id = database.insert(DBHelper.TABLE_PRODUCTS, null, contentValues);
                UpdateTable();
                etName.setText("");
                etPrice.setText("");
                etDesc.setText("");
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_PRODUCTS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            case R.id.btnShop:
                Intent intent = new Intent(this, Shop.class);
                startActivity(intent);
                break;
            default:
                if(v.getTag() == "delete") {
                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();
                    database.delete(dbHelper.TABLE_PRODUCTS, dbHelper.KEY_PRODID + " = ?", new String[]{String.valueOf(v.getId())});

                    contentValues = new ContentValues();
                    Cursor cursorUpdater = database.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
                    if (cursorUpdater.moveToFirst()) {
                        int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODID);
                        int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODNAME);
                        int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODPRICE);
                        int priceDesc = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODDESCR);
                        int realID = 1;
                        do {
                            if (cursorUpdater.getInt(idIndex) > realID) {
                                contentValues.put(dbHelper.KEY_PRODID, realID);
                                contentValues.put(dbHelper.KEY_PRODNAME, cursorUpdater.getString(nameIndex));
                                contentValues.put(dbHelper.KEY_PRODPRICE, cursorUpdater.getString(priceIndex));
                                contentValues.put(dbHelper.KEY_PRODDESCR, cursorUpdater.getString(priceDesc));
                                database.replace(dbHelper.TABLE_PRODUCTS, null, contentValues);
                            }
                            realID++;
                        }
                        while (cursorUpdater.moveToNext());
                        if (cursorUpdater.moveToLast()) {
                            if (cursorUpdater.moveToLast() && v.getId() != realID) {
                                database.delete(dbHelper.TABLE_PRODUCTS, dbHelper.KEY_PRODID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                            }
                        }
                        UpdateTable();
                    }
                }

               break;
        }
    }
}