package com.example.rems;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase sysDB;
    private static Context context;
    private static String  DATABASE_NAME = "remsDB.db";
    private static int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        sysDB=getWritableDatabase();
    }


    /*
    *
    * when app is installed this method will be called and build the database
    *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "database was created", Toast.LENGTH_SHORT).show();
        //execSQL used to send SQL statement that isn't returning data (such as select)

        //creating table "ActivityTasks" with columns ActivityTaskID,DateAndTime,Content,Category,Priority
        db.execSQL("CREATE TABLE ActivityTasks (" +
                "ActivityTaskID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DateAndTime TEXT," +
                "Content TEXT," +
                "Category TEXT," +
                "Priority INTEGER" +
                ")");

        //creating table "SubActivity" with columns SubActivityID,ActivityTaskID,Content
        db.execSQL("CREATE TABLE SubActivity (" +
                "SubActivityID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ActivityTaskID INTEGER," +
                "Content TEXT," +
                "FOREIGN KEY(ActivityTaskID) REFERENCES ActivityTasks(ActivityTaskID)" +
                ")");

        //creating table "WordPriority" with columns Word,Priority
        db.execSQL("CREATE TABLE WordPriority (" +
                "Word TEXT PRIMARY KEY," +
                "Priority INTEGER NOT NULL" +
                ")");

    }

    //used when you want to update the database,when called need to increment the version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete the old database by dropping the tables
        db.execSQL("DROP TABLE IF EXISTS " + "ActivityTasks");
        db.execSQL("DROP TABLE IF EXISTS " + "SubActivity");
        db.execSQL("DROP TABLE IF EXISTS " + "WordPriority");

        onCreate(db);// Create tables again
    }

    /*
    *
    * in this section there are methods for the table @WordPriority will be int the order of C.R.U.D
    *
     */
    public boolean insertPriorityWord(String word, int priority){

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        //check if the database opened if not retuning false
        if(db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("Content", word);//insert the value to Content values
            values.put("Priority", priority);//insert the value to values
            //check if the item was added successfully if not retuning false
            if(values.size()<1)
                return false;
            else
                db.insert("WordPriority", null, values);//insert into table WordPriority the new word

            return  true;
        }
        return false;
    }


}
