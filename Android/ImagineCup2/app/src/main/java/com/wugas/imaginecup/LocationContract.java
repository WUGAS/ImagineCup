package com.wugas.imaginecup;

import android.provider.BaseColumns;

/**
 * Created by suhon_000 on 12/19/2014.
 * Container for constants that define names for URI, tables, and columns
 */
public class LocationContract {

    public LocationContract() {}

    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry"; //db_base_url
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LONG = "lon";
        public static final String COLUMN_ID = "_id";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
