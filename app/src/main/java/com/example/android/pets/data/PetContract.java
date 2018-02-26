package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Snigdha on 14-12-2017.
 */

public final class PetContract {

    public static final String contentAuthority="com.example.android.pets";
    public static final Uri baseUri=Uri.parse("content://"+contentAuthority);
    public static final String type="pets";

    public static abstract class PetEntry implements BaseColumns {
        public static final String TABLE_NAME="pets";
        public static final String NAME="name";
        public static final String BREED="breed";
        public static final String GENDER="gender";
        public static final String WEIGHT="weight";
        public static final String _ID=BaseColumns._ID;

        //other variables
        public static final int UNKNOWN=0;
        public static final int MALE=1;
        public static final int FEMALE=2;

        public static boolean isValidGender( int gender) {
            if(gender==MALE|| gender==FEMALE|| gender==UNKNOWN)
                return true;
            else return false;
        }

        public static final Uri contentUri= Uri.withAppendedPath(baseUri,type);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + contentAuthority + "/" + type;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + contentAuthority + "/" + type;
    }
}
