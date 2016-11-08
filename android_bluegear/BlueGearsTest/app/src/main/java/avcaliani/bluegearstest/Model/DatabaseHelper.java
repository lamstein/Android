package avcaliani.bluegearstest.Model;


import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import avcaliani.bluegearstest.Model.*;
import avcaliani.bluegearstest.View.Item;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "itens_bg.db";
    public static final String TABLE_NAME = "itens_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "DESC";
    public static final String COL_4 = "TIME";
    public static final String COL_5 = "IMG_PATH";

    protected SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_2 + " TEXT NOT NULL UNIQUE, " +
                    COL_3 + " TEXT NOT NULL, " +
                    COL_4 + " TEXT NOT NULL, " +
                    COL_5 + " TEXT NOT NULL) ");

        }catch (SQLException error){
            Log.e("SQL error!", error.getMessage());
        } catch (Exception error){
            Log.e("Error!", error.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }catch (SQLException error){
            Log.e("SQL error!", error.getMessage());
        }
    }

    public void closeConn(){
        mDatabase.close();
    }

    public boolean insertData (Item newItem) throws Exception {

        if (newItem == null || !(newItem instanceof Item))
            throw new Exception("Object 'Item' is null");

        Item myItem;

        if (newItem instanceof Cloneable){
            myItem = new Item(newItem);
        } else {
            myItem = newItem;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, myItem.getItemName());
        contentValues.put(COL_3, myItem.getItemDesc());
        contentValues.put(COL_4, myItem.getTime());
        contentValues.put(COL_5, myItem.getImagePath());

        // O metodo "insert" retorna  a linha em que foi adicionada os dados,
        // em caso de erro retorna -1
        long queryResult = mDatabase.insert(TABLE_NAME, null, contentValues);

        if (queryResult == -1)
            return false;
        return true;
    }



    public Cursor getAllData(){
        Cursor response = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return response;
    }

    public boolean deleteItemByName(String itemName) throws Exception{
        if (itemName == null || itemName.trim().matches(""))
            throw new Exception("Item name can't be null to execute this query!");

        Integer afectedRows =  mDatabase.delete(TABLE_NAME, COL_2 + " = ?", new String[] {itemName});

        if (afectedRows <= 0)
            return false;
        return true;
    }

} // End-Class
