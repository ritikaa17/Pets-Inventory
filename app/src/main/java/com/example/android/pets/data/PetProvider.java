package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static com.example.android.pets.data.PetContract.PetEntry;
import android.util.Log;
import android.widget.Toast;

import com.example.android.pets.EditorActivity;

/**
 * Created by Snigdha on 16-12-2017.
 */

public class PetProvider extends ContentProvider {
    public static final String LOG_TAG=PetProvider.class.getSimpleName();
    private static final int PETS=100;
    private static final int PETS_ID=101;
    private static final UriMatcher sUriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
    static  {
        sUriMatcher.addURI(PetContract.contentAuthority,PetContract.type,PETS);
        sUriMatcher.addURI(PetContract.contentAuthority,PetContract.type+"/#",PETS_ID);
    }

    private PetdbHelper helper;
    @Override
    public boolean onCreate() {
        helper=new PetdbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg,String sortOrder) {
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        switch(match) {
            case PETS:
                cursor=db.query(PetEntry.TABLE_NAME,projection,selection,selectionArg,null,null,sortOrder);
                break;
            case PETS_ID:
                selection= PetEntry._ID+"=?";
                selectionArg= new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(PetEntry.TABLE_NAME,projection,selection,selectionArg,null,null,sortOrder);
                break;

                default: throw new IllegalArgumentException("Can not query, unknown URI: "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match=sUriMatcher.match(uri);
        switch (match) {
            case PETS: return PetEntry.CONTENT_LIST_TYPE;
            case PETS_ID: return PetEntry.CONTENT_ITEM_TYPE;
            default: throw new IllegalStateException("Unknown URI: "+uri+", with match: "+match);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match=sUriMatcher.match(uri);
        switch(match) {

            case PETS: return insertPet(uri,values);
            default: throw new IllegalArgumentException("Insertion isn't supported for: "+uri);
        }
    }

    private Uri insertPet(Uri uri,ContentValues values) {
        SQLiteDatabase db=helper.getWritableDatabase();
        String name=values.getAsString(PetEntry.NAME);
        if(name==null||name.length()==0) {
            throw new IllegalArgumentException("Name required");
        }
        Integer gender=values.getAsInteger(PetEntry.GENDER);
        if(gender==null||!PetContract.PetEntry.isValidGender(gender)) throw new IllegalArgumentException("Gender required");

        Integer weight=values.getAsInteger(PetEntry.WEIGHT);
        if(weight<0) throw new IllegalArgumentException("Weight required");

        long id=db.insert(PetEntry.TABLE_NAME,null,values);
        if(id==-1) {
            Log.e(LOG_TAG,"Failed to insert row for: "+uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArg) {
        int match=sUriMatcher.match(uri);
        SQLiteDatabase db=helper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri,null);
        switch(match) {
            case PETS: return db.delete(PetEntry.TABLE_NAME,selection,selectionArg);
            case PETS_ID:
                selection=PetEntry._ID+"=?";
                selectionArg=new String[] {String.valueOf(ContentUris.parseId(uri))};
                return db.delete(PetEntry.TABLE_NAME,selection,selectionArg);
                default: throw new IllegalArgumentException("Delete isn't supported for: "+uri);
        }

    }
    @Override
    public int update(Uri uri, ContentValues values,String selection, String[] selectionArg) {
        SQLiteDatabase db=helper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        switch (match) {
            case PETS: return updatePet(uri,values,selection,selectionArg);
            case PETS_ID:
                selection=PetEntry._ID+"=?";
                selectionArg= new String[] {String.valueOf(ContentUris.parseId(uri))};
               return updatePet(uri,values,selection,selectionArg);
               default:throw new IllegalArgumentException("Update is not supported for: "+uri);
        }
     }

     private int updatePet(Uri uri,ContentValues values, String selection, String[] selectionArg) {
        SQLiteDatabase db=helper.getWritableDatabase();

        if(!values.containsKey(values.getAsString(PetEntry.NAME)))
        {         String name=values.getAsString(PetEntry.NAME);
         if(name==null) throw new IllegalArgumentException("Name required");
         }

         if(!values.containsKey(PetEntry.GENDER)) {
             Integer gender = values.getAsInteger(PetEntry.GENDER);
             if (gender == null || !PetContract.PetEntry.isValidGender(gender))
                 throw new IllegalArgumentException("Gender required");
         }

         if(!values.containsKey(PetEntry.WEIGHT)) {
             Integer weight = values.getAsInteger(PetEntry.WEIGHT);
             if (weight < 0) throw new IllegalArgumentException("Weight required");
         }
         getContext().getContentResolver().notifyChange(uri,null);
         if(values.size()==0) return 0;
         return db.update(PetEntry.TABLE_NAME,values,selection,selectionArg);

     }
}
