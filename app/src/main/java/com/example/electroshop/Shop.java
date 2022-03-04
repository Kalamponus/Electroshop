package com.example.electroshop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity implements View.OnClickListener{

    Button btnAdmin, btnOrder;
    DBHelper dbHelper;
    SQLiteDatabase database;
    int sum = 0;
    TextView sumInCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        sumInCart = findViewById(R.id.sumInCart);

        btnAdmin = (Button) findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(this);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);

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
            int descrIndex = cursor.getColumnIndex(DBHelper.KEY_PRODDESCR);
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

                TextView outputDescr = new TextView(this);
                params.weight = 3.0f;
                outputDescr.setLayoutParams(params);
                outputDescr.setText(cursor.getString(descrIndex));
                dbOutputRow.addView(outputDescr);

                Button btnToCart = new Button(this);
                btnToCart.setOnClickListener(this);
                params.weight = 1.0f;
                btnToCart.setLayoutParams(params);
                btnToCart.setText("Добавить в корзину");
                btnToCart.setTag("cart");
                btnToCart.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnToCart);


                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }
    public void ChangeSum(int sumToAdd)
    {
        sum = sum + sumToAdd;
        sumInCart.setText("Сумма заказа: "  + sum);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdmin:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOrder:
                Intent intentOrder = new Intent(this, OrderTaker.class);
                intentOrder.putExtra("Cost", sum);
                startActivity(intentOrder);
                ChangeSum(-sum);
                break;
            default:
                Cursor cursorUpdater = database.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
                cursorUpdater.move(v.getId());
                int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODPRICE);
                String priceKek = cursorUpdater.getString(priceIndex);
                ChangeSum(Integer.parseInt(priceKek));

                int columnIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODID);
                int prodIndex = cursorUpdater.getInt(columnIndex);
                boolean found = false;
                for(int i = 0; i < ProdInfoList.prodInfo.size(); i++){
                    if(ProdInfoList.prodInfo.get(i).indx == prodIndex){
                        ProdInfoList.prodInfo.get(i).quant+=1;
                        found = true;
                        break;
                    }

                }
                if(found == false){
                    ProdInfo newProd = new ProdInfo();
                    newProd.indx = prodIndex;
                    newProd.quant = 1;
                    ProdInfoList.prodInfo.add(newProd);
                }

                break;
        }
    }

}