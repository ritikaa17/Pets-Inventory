/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetdbHelper;

import static com.example.android.pets.data.PetContract.PetEntry.BREED;
import static com.example.android.pets.data.PetContract.PetEntry.GENDER;
import static com.example.android.pets.data.PetContract.PetEntry.MALE;
import static com.example.android.pets.data.PetContract.PetEntry.NAME;
import static com.example.android.pets.data.PetContract.PetEntry.WEIGHT;
import static com.example.android.pets.data.PetContract.PetEntry;
import static com.example.android.pets.data.PetContract.PetEntry.contentUri;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER=0;
    PetAdapter adapter;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
            ListView items=(ListView) findViewById(R.id.list);
            View emptyView=(View) findViewById(R.id.empty);
            items.setEmptyView(emptyView);

            adapter=new PetAdapter(this,null);
            items.setAdapter(adapter);

            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                    Uri currentUri= ContentUris.withAppendedId(PetEntry.contentUri,id);
                    intent.setData(currentUri);
                    startActivity(intent);
                }
            });
            getLoaderManager().initLoader(PET_LOADER,null,this);
    }


    private void insertPet() {
        ContentValues values=new ContentValues();
       // values.put(_ID,1);
        values.put(NAME,"Tommy");
        values.put(BREED,"Pomerian");
        values.put(GENDER,MALE);
        values.put(WEIGHT,12);
        Uri newUri=getContentResolver().insert(PetEntry.contentUri,values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    private void showDeleteDialogue() {
        AlertDialog.Builder del= new AlertDialog.Builder(this);
        del.setMessage("Are you sure you want to delete all the pets?");
        del.setPositiveButton("Delete all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePet();
            }
        });
        del.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null)
                    dialogInterface.dismiss();
            }
        });
        AlertDialog alert= del.create();
        alert.show();
    }

    private void deletePet() {
            int row=getContentResolver().delete(contentUri,null,null);
            if(row==0) Toast.makeText(CatalogActivity.this,"No pet deleted",Toast.LENGTH_SHORT);
            else Toast.makeText(CatalogActivity.this, row+" pets deleted",Toast.LENGTH_SHORT);
            finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
            { insertPet();
                return true; }
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                    showDeleteDialogue();
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection= {
          PetEntry._ID,PetEntry.NAME,PetEntry.BREED
        };
        return new CursorLoader(this,contentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
