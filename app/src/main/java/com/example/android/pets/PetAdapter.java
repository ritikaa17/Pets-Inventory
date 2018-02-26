package com.example.android.pets;

/**
 * Created by Snigdha on 17-12-2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.R;
import com.example.android.pets.data.PetContract;

/**
 * Created by Snigdha on 17-12-2017.
 */

public class PetAdapter extends CursorAdapter {
    public PetAdapter(Context context, Cursor cursor) {
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pet_list,parent,false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text1=(TextView) view.findViewById(R.id.petName);
        TextView text2=(TextView) view.findViewById(R.id.petBreed);

        String name=cursor.getString(cursor.getColumnIndexOrThrow(PetContract.PetEntry.NAME));
       int breedCol=cursor.getColumnIndex(PetContract.PetEntry.BREED);
       String breed=cursor.getString(breedCol);

        if(!TextUtils.isEmpty(breed)) {
         breed=cursor.getString(cursor.getColumnIndexOrThrow(PetContract.PetEntry.BREED));}
        else { breed="Unknown breed";}

        text1.setText(name);
        text2.setText(breed);
    }

}

