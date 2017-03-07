package com.example.yoyo.databaseapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{

    Context context;
    public static final int DATABASE_VERSION = 19;
    public static final String DATABASE_NAME="Notes";
    private static final String TEXT_TYPE =" TEXT ";
    private static final String INTEGER_TYPE =" INTEGER ";
    private static final String BLOB_TYPE =" BLOB ";
    private static final String COMMA_SEP = " , ";
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE "+ FeedReaderContract.FeedEntry.Notes_TABLE_NAME+
            "("+ FeedReaderContract.FeedEntry.Notes_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL "+COMMA_SEP+
            FeedReaderContract.FeedEntry.Notes_COLUMN_Topic + TEXT_TYPE + COMMA_SEP +
            FeedReaderContract.FeedEntry.Notes_COLUMN_Description + TEXT_TYPE +COMMA_SEP+
            FeedReaderContract.FeedEntry.Notes_COLUMN_Image+TEXT_TYPE+COMMA_SEP+
            FeedReaderContract.FeedEntry.Notes_Location_LAT+TEXT_TYPE+COMMA_SEP+
            FeedReaderContract.FeedEntry.Notes_Location_LNG + TEXT_TYPE+
            ")";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
//        db.execSQL(
//                "create table contacts " +
//                        "(id integer primary key, name text,phone text,email text, street text,place text)"
//        );
       db.execSQL(SQL_CREATE_ENTRIES);
     //   db.execSQL(SQL_CREATE_LOCATION);
    }

    public Notes insertNote (Notes note,Bitmap picture)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic, note.getTopic());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Description, note.getDescription());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_Location_LAT, note.getLat());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_Location_LNG, note.getLng());
       // contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Image,img);
        int newid = (int) db.insert(FeedReaderContract.FeedEntry.Notes_TABLE_NAME, null, contentValues);
        //why did i needed the primary key ???
        note.set_id(newid);
        String path = insertPhoto(newid,picture);
        note.setImage(path);
        db.close();
        return note;
    }

    public Notes insertNoteWithoutBitmap  (Notes note)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic, note.getTopic());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Description, note.getDescription());
        // contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Image,img);
        contentValues.put(FeedReaderContract.FeedEntry.Notes_Location_LAT, note.getLat());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_Location_LNG, note.getLng());
        int newid = (int) db.insert(FeedReaderContract.FeedEntry.Notes_TABLE_NAME, null, contentValues);
        //why did i needed the primary key ???
        note.set_id(newid);
      //  String path =   insertPhoto(newid,picture);
      //  note.setImage(path)
        db.close();
        return note;
    }

    public Notes getData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {FeedReaderContract.FeedEntry.Notes_COLUMN_ID, FeedReaderContract.FeedEntry.Notes_COLUMN_Topic, FeedReaderContract.FeedEntry.Notes_COLUMN_Description,
                FeedReaderContract.FeedEntry.Notes_COLUMN_Image};
        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID+ "=?";
        //default order is asd
        String sortOrder = FeedReaderContract.FeedEntry.Notes_COLUMN_Topic;

        String[] arguments = new String[]{String.valueOf(id)};
        Cursor c = db.query(FeedReaderContract.FeedEntry.Notes_TABLE_NAME,projection,selection,arguments,null,null,sortOrder);

        Notes note = new Notes();
        if(c!=null && c.moveToFirst())
        {
          //  c.moveToFirst();
          //  byte[] blob = c.getBlob(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Image));  !!! Its fine its byte[] !!!
            note = new Notes(c.getInt(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_ID)),c.getString(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic)),
                c.getString(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Description)),c.getString(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Image)));
           // byte[] blob = c.getBlob(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Image));

        }else
        {
            note = null;
        }
        db.close();
        return note;
    }

    public int numberOfRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FeedReaderContract.FeedEntry.Notes_TABLE_NAME);
        db.close();
        return numRows;
    }

    public String insertPhoto(int id,Bitmap picture)
    {
        String picturePath = "";
        File internalStorage = context.getDir("ReportPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, id + ".png");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            picture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }

        // Updates the database entry for the report to point to the picture
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments = new String[]{String.valueOf(id)};
        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID+ "=?";
        ContentValues newPictureValue = new ContentValues();
        newPictureValue.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Image,
                picturePath);

        db.update(FeedReaderContract.FeedEntry.Notes_TABLE_NAME,
                newPictureValue,
                selection,
                arguments);

        return picturePath;
    }


    public boolean updateNote(Notes note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic, note.getTopic());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Description, note.getDescription());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Image,note.getImage());


        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID + "=?";//izkaraj navun
        String[] arguments = new String[]{String.valueOf(note.get_id())};
        db.update(FeedReaderContract.FeedEntry.Notes_TABLE_NAME, contentValues, selection, arguments);
        return true;
    }

    public boolean updateContactwithBitmap (Notes note,Bitmap map)
    {
        String path = insertPhoto(note.get_id(),map);
        note.setImage(path);


        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID + "=?";//izkaraj navun
        String[] arguments = new String[]{String.valueOf(note.get_id())};


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic, note.getTopic());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Description, note.getDescription());
        contentValues.put(FeedReaderContract.FeedEntry.Notes_COLUMN_Image,note.getImage());

        db.update(FeedReaderContract.FeedEntry.Notes_TABLE_NAME, contentValues, selection, arguments);
        return true;
    }

    public Bitmap getNotesPicture(String picturePath)
    {

        if (picturePath == null || picturePath.length() == 0)
            return (null);

        Bitmap reportPicture = BitmapFactory.decodeFile(picturePath);

        return (reportPicture);
    }

    public boolean deleteById(int id)
    {
        Notes note = getData(id);
        deleteNoteByObject(note);

        return true;
    }

    public boolean deleteNoteByObject (Notes note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID + "=?";
        String[] arguments = new String[]{String.valueOf(note.get_id())};

        int id = note.get_id();
        String picturepath = getReportPicturePath(id);

        if (picturepath != null && picturepath.length() != 0)
        {
            File reportFilePath = new File(picturepath);
            reportFilePath.delete();
        }


        db.delete(FeedReaderContract.FeedEntry.Notes_TABLE_NAME,
                selection,
                arguments);
        db.close();

//        if (!rs.isClosed())
//        {
//            rs.close();
//        }
        return true;
    }

    private String getReportPicturePath(int reportId)
    {
        // Gets the database in the current database helper in read-only mode
        SQLiteDatabase db = getReadableDatabase();
        String selection = FeedReaderContract.FeedEntry.Notes_COLUMN_ID+ "=?";
        String[] arguments = new String[]{String.valueOf(reportId)};
        // After the query, the cursor points to the first database row
        // returned by the request
        Cursor reportCursor = db.query(FeedReaderContract.FeedEntry.Notes_TABLE_NAME,
                null,
               selection,
                arguments,
                null,
                null,
                null);
        reportCursor.moveToNext();

        // Get the path of the picture from the database row pointed by
        // the cursor using the getColumnIndex method of the cursor.
        String picturePath = reportCursor.getString(reportCursor.
                getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Image));

        return (picturePath);
    }




    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FeedReaderContract.FeedEntry.SQL_DELETE_ENTRIES);
       // db.execSQL(FeedReaderContract.FeedEntry.SQL_DELETE_LOCATION_ENTRIES);
        onCreate(db);
    }


    public ArrayList<Notes> getAllNotes()
    {
        ArrayList<Notes> array_list = new ArrayList<Notes>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from notes", null);
        if(res.moveToFirst())
        {
            do
            {
              Notes note = new Notes();
                //c.getInt(c.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_ID))
                note.set_id(res.getInt(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_ID)));
//                int indexTopic = res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic);
//
//                note.setTopic(indexTopic);
                note.setDescription(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Description)));
                note.setTopic(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Topic)));
                note.setImage(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_COLUMN_Image)));
                note.setLat(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_Location_LAT)));
                note.setLng(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.Notes_Location_LNG)));
                array_list.add(note);

            }while(res.moveToNext());
        }

         db.close();
        return array_list;///check isEmpty
    }

}