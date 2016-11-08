package avcaliani.listviewapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users.db";
    public static final String TABLE_NAME = "users_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "TIME";

    protected SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_2 + " TEXT NOT NULL UNIQUE, " + COL_3 + " TEXT NOT NULL )");
        }catch (SQLException error){
            Log.e("SQL error!", error.getMessage());
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

    public boolean insertData (String username, String time) throws Exception {
        if (username == null || username.trim().matches(""))
            throw new Exception("Username field can't be null to execute query!");
        if (time == null || time.trim().matches(""))
            throw new Exception("Time field can't be null to execute query!");

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, time);

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

    public boolean deleteUserByUsername(String username) throws Exception{
        if (username == null || username.trim().matches(""))
            throw new Exception("Username can't be null to execute this query!");

        Integer afectedRows =  mDatabase.delete(TABLE_NAME, COL_2 + " = ?", new String[] {username});

        if (afectedRows <= 0)
            return false;
        return true;
    }
}
