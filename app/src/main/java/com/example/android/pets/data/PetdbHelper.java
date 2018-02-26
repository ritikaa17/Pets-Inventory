package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by Snigdha on 15-12-2017.
 */

public class PetdbHelper extends SQLiteOpenHelper {

    public static final int DB_version=1;
    public static final String DB_name="shelter.db";

    public PetdbHelper(Context context) {
        super(context, DB_name,null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE= "CREATE TABLE "+ PetEntry.TABLE_NAME+"("+ PetEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ PetEntry.NAME+" TEXT NOT NULL,"+PetEntry.BREED+" TEXT,"+ PetEntry.GENDER+" INTEGER NOT NULL,"+ PetEntry.WEIGHT+" INTEGER DEFAULT 0);";
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       /* db.execSQL(DELETE);
        onCreate(db);*/
    }
}
