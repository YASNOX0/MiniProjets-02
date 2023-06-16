package com.example.mini_projets_02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mini_projets_02.db.FavoriteQuotesContract;

public class FavoriteQuotesDbOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quote.db";

    private static final String SQL_CREATE_FAVORITE_QUOTES = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY," +
                    "%s TEXT," +
                    "%s TEXT)", FavoriteQuotesContract.Infos.TABLE_NAME,
            FavoriteQuotesContract.Infos.COLUMN_NAME_ID,
            FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE,
            FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR);


    private static final String SQL_DELETE_FAVORITE_QUOTES = String.format("DROP TABLE IF EXISTS %s",
            FavoriteQuotesContract.Infos.TABLE_NAME);

    public FavoriteQuotesDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_FAVORITE_QUOTES);
        onCreate(db);
    }

    public void add(int id, String quote, String author) {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_ID, id);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE, quote);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR, author);
        db.insert(FavoriteQuotesContract.Infos.TABLE_NAME, null, values);
    }

    public void getAll() {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();


        String[] projection = {
                FavoriteQuotesContract.Infos.COLUMN_NAME_ID,
                FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE,
                FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR
        };

        Cursor cursor = db.query(
                FavoriteQuotesContract.Infos.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR));
            Log.e("Sqlite", String.format("Quote , %d , %s ,%s", id, quote, author));
        }
        cursor.close();
    }

}
