package com.example.mini_projets_02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.example.mini_projets_02.db.ColorsContract;
import com.example.mini_projets_02.db.SettingsContract;
import com.example.mini_projets_02.models.BgColor;
import com.example.mini_projets_02.models.Setting;

import java.util.ArrayList;

public class SettingsDbOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Settings.db";


    SQLiteDatabase settingsDb = SettingsDbOpenHelper.this.getWritableDatabase();

    private static final String SQL_CREATE_COLORS = String.format("CREATE TABLE %s (" +
                    "%s TEXT PRIMARY KEY," +
                    "%s TEXT)", ColorsContract.ColorInfo.TABLE_NAME,
            ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME,
            ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE);

    private static final String SQL_DELETE_COLORS = String.format("DROP TABLE IF EXISTS %s",
            ColorsContract.ColorInfo.TABLE_NAME);

    private static final String SQL_CREATE_SETTINGS = String.format("CREATE TABLE %s (" +
                    "%s TEXT PRIMARY KEY," +
                    "%s TEXT," +
                    "FOREIGN KEY (%s) REFERENCES %s(%s))", SettingsContract.SettingInfo.TABLE_NAME,
            SettingsContract.SettingInfo.COLUMN_NAME_SETTING_NAME,
            SettingsContract.SettingInfo.COLUMN_NAME_SETTING_VALUE,
            SettingsContract.SettingInfo.COLUMN_NAME_SETTING_VALUE,
            ColorsContract.ColorInfo.TABLE_NAME,
            ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME);

    private static final String SQL_DELETE_SETTINGS = String.format("DROP TABLE IF EXISTS %s",
            SettingsContract.SettingInfo.TABLE_NAME);

    public SettingsDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLORS);
        db.execSQL(SQL_CREATE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_COLORS);
        db.execSQL(SQL_DELETE_SETTINGS);
        onCreate(db);
    }

    public void addBgColors(BgColor[] bgColors) {
        ContentValues values = new ContentValues();

        for (BgColor bgColor : bgColors) {
            values.put(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME, bgColor.getName());
            values.put(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE, bgColor.getCode());
            settingsDb.insert(ColorsContract.ColorInfo.TABLE_NAME, null, values);
        }
    }

    public void updateBgColorCode(BgColor bgColor) {
        ContentValues values = new ContentValues();
        values.put(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME, bgColor.getName());
        values.put(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE, bgColor.getCode());

        String selection = ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME + " = ?";
        String[] selectionArgs = {bgColor.getName()};

        settingsDb.update(ColorsContract.ColorInfo.TABLE_NAME, values, selection, selectionArgs);
    }

    public int getBgColorCode(String bgColorName) {
        String[] projection = {
                ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE
        };
        String selection = ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME + " = ?";
        String[] selectionArgs = {bgColorName};

        Cursor cursor = settingsDb.query(ColorsContract.ColorInfo.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        String bgColorCode = null;
        while (cursor.moveToNext()) {
            bgColorCode = cursor.getString(cursor.getColumnIndexOrThrow(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE));
        }
        cursor.close();
        return Color.parseColor(bgColorCode);
    }

    public ArrayList<BgColor> getBgColors() {
        ArrayList<BgColor> bgColors = new ArrayList<>();

        String[] projection = {
                ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME,
                ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE
        };

        Cursor cursor = settingsDb.query(ColorsContract.ColorInfo.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndexOrThrow(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_CODE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ColorsContract.ColorInfo.COLUMN_NAME_COLOR_NAME));
            bgColors.add(new BgColor(name, code));
        }
        cursor.close();
        return bgColors;
    }


    public void insertOrUpdateSetting(Setting setting) {
        ContentValues values = new ContentValues();
        values.put(SettingsContract.SettingInfo.COLUMN_NAME_SETTING_NAME, setting.getName());
        values.put(SettingsContract.SettingInfo.COLUMN_NAME_SETTING_VALUE, setting.getValue());

        if (getSettingValue(setting.getName()) == null) {
            settingsDb.insert(SettingsContract.SettingInfo.TABLE_NAME, null, values);
        } else {
            String selection = SettingsContract.SettingInfo.COLUMN_NAME_SETTING_NAME + " = ?";
            String[] selectionArgs = {setting.getName()};

            settingsDb.update(SettingsContract.SettingInfo.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    public String getSettingValue(String settingName) {
        String[] projection = {
                SettingsContract.SettingInfo.COLUMN_NAME_SETTING_VALUE
        };
        String selection = SettingsContract.SettingInfo.COLUMN_NAME_SETTING_NAME + " = ?";
        String[] selectionArgs = {settingName};
        Cursor cursor = settingsDb.query(SettingsContract.SettingInfo.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        String settingValue = null;

        while (cursor.moveToNext()) {
            settingValue = cursor.getString(cursor.getColumnIndexOrThrow(SettingsContract.SettingInfo.COLUMN_NAME_SETTING_VALUE));
        }
        cursor.close();
        return settingValue;
    }

}
