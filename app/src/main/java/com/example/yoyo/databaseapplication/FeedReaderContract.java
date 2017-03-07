package com.example.yoyo.databaseapplication;

import android.provider.BaseColumns;

/**
 * Created by yoyo on 4/19/2016.
 */
public final class FeedReaderContract
{
    public FeedReaderContract()
    {

    }
public static abstract class FeedEntry implements BaseColumns
{
    public static final String DATABASE_NAME = "Notes";
    public static final String Notes_TABLE_NAME = "notes";
    public static final String Notes_COLUMN_ID = "id";
    public static final String Notes_COLUMN_Topic = "topic";
    public static final String Notes_COLUMN_Description = "description";
    public static final String Notes_COLUMN_Image = "image";
    //for the location table
//    public static final String Location_TABLE_NAME = "locations";
//    public static final String Location_COLUMN_ID ="id";
    public static final String Notes_Location_LNG ="lng";
    public static final String Notes_Location_LAT ="lat";
    //
    //public static final String CONTACTS_COLUMN_STREET = "street";////PICTURE
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.Notes_TABLE_NAME;
//    public static final String SQL_DELETE_LOCATION_ENTRIES =
//            "DROP TABLE IF EXISTS " + FeedEntry.Location_TABLE_NAME;
}

}
