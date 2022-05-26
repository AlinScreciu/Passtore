package me.alin.passtore;

import static me.alin.passtore.MainActivity.MASTER_PASS_ENTRY_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    final static String DB_NAME = "PasstoreDB";
    final static String ID = "ID";
    final static String TABLE_NAME = "PASSWORDS";
    final static String SERVICE_NAME = "SERVICE_NAME";
    final static String SERVICE_USERNAME = "USERNAME";
    final static String SERVICE_PASSWORD = "PASSWORD";
    final static int DB_VERSION = 11;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SERVICE_NAME + " TEXT, "
                + SERVICE_USERNAME + " TEXT, "
                + SERVICE_PASSWORD + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public AccountModel getAccount(Integer id) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        // SELECT * FROM TABLE WHERE NAME = 'nume'
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            sqLiteDatabase.close();
            return null;
        }
        cursor.moveToFirst();
        String service = cursor.getString(1).trim();
        String username = cursor.getString(2).trim();
        String password = cursor.getString(3).trim();
        AccountModel account = new AccountModel(service, username, password);
        account.setId(cursor.getInt(0));
        cursor.close();
        sqLiteDatabase.close();

        return account;
    }

    public List<AccountModel> getAllAccounts() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + SERVICE_NAME + " != '" + MASTER_PASS_ENTRY_NAME + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        List<AccountModel> accounts = new ArrayList<>();
        if (cursor.getCount() == 0) {
            cursor.close();
            sqLiteDatabase.close();
            return accounts;
        }

        while (cursor.moveToNext()) {
            AccountModel account = new AccountModel();
            account.setId(cursor.getInt(0));
            account.service = cursor.getString(1).trim();
            account.username = cursor.getString(2).trim();
            account.password = cursor.getString(3).trim();
            accounts.add(account);
        }
        cursor.close();
        sqLiteDatabase.close();
        return accounts;
    }

    public void addAccount(AccountModel account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(SERVICE_NAME, account.service);
        row.put(SERVICE_USERNAME, account.username);
        row.put(SERVICE_PASSWORD, account.password);
        sqLiteDatabase.insert(TABLE_NAME, null, row);
        sqLiteDatabase.close();
    }

    public void removeAccount(Integer id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }
}
