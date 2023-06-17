package com.example.mini_projets_02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mini_projets_02.db.FavoriteQuotesContract;
import com.example.mini_projets_02.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesDbOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quote.db";

    SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();

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

    private void addQuote(int id, String quote, String author) {
        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_ID, id);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_QUOTE, quote);
        values.put(FavoriteQuotesContract.Infos.COLUMN_NAME_AUTHOR, author);
        db.insert(FavoriteQuotesContract.Infos.TABLE_NAME, null, values);
    }

    public void saveQuote(Quote quote) {
        addQuote(quote.getId(), quote.getQuote(), quote.getAuthor());
    }

    public void deleteQuote(int id) {
        String selection = FavoriteQuotesContract.Infos.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.delete(FavoriteQuotesContract.Infos.TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<Quote> getAll() {
        ArrayList<Quote> quotes = new ArrayList<>();
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
            quotes.add(new Quote(id, quote, author));
        }
        cursor.close();
        return quotes;
    }

    public boolean isFavorite(int id) {
        String[] projection = {
                FavoriteQuotesContract.Infos.COLUMN_NAME_ID,
        };

        String selection = FavoriteQuotesContract.Infos.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = db.query(
                FavoriteQuotesContract.Infos.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        boolean state = cursor.moveToNext();
        cursor.close();
        return state;
    }

}
