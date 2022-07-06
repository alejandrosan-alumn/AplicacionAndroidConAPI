package com.example.QueMeVeo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public static final int versionBD = 1;
    public static final String nombreBD = "QueMeVeo.db";

    public ConexionSQLiteHelper(@Nullable Context context) {
        super(context, nombreBD, null, versionBD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE Peliculas (IDpelicula INTEGER PRIMARY KEY)");
        sqLiteDatabase.execSQL("CREATE TABLE Series (IDSeries INTEGER PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Peliculas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Series");
        onCreate(sqLiteDatabase);
    }
}
