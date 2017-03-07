package com.example.yoyo.databaseapplication;

import android.app.Activity;

/**
 * Created by yoyo on 4/21/2016.
 */
public class TestDatabase extends Activity
{

    DBHelper help = new DBHelper(getApplicationContext());
    Notes note = new Notes("Teo","Lets meet right over here");
 //   Notes insert = help.insertNote(note);


}
