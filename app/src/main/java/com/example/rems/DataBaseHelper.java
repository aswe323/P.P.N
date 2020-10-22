package com.example.rems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import module.ActivityTask;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase sysDB;
    private static Context context;//TODO: @LIOR! do not do this, THIS IS A MEMORY LEAK!  REALLY REALLY BAD!
    private static String DATABASE_NAME = "remsDB.db";
    private static int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        sysDB = getWritableDatabase();
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
                "Repetition TEXT," +
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
        db.execSQL("DROP TABLE IF EXISTS " + "WordPriority");//TODO: what about the data? you created the database again with the tables, but lost ALL of the data you had there.
        // you didn't UPDATE anything.. you resetted the DB.
        //pls fix.

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
            values.put("Word", word);//insert the value to Content values
            values.put("Priority", priority);//insert the value to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
                db.insert("WordPriority", null, values);//insert into table WordPriority the new word

            return true;
        }
        return false;
    }

    public Map<String, Integer> queryForPriorityWords() {// get ALL of the priority words from the table. and return them as a Map.
        Map<String, Integer> returned = null;//the Map that will be returned

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            Cursor data = db.rawQuery("select * from WordPriority", null);//we don't need Args because we wan't ALL of the data.
            data.moveToFirst();//required because the cursor is set on the 0th element, which hold is nothing.
            do {
                returned.put(data.getString(0), data.getInt(0));//WordPriority only got 2 columns, the Word(String), and the Priority(Int).
            } while (data.moveToNext());
            data.close();
        }

        return returned;
    }

    public boolean updatePrioritiyOfWord(String word, Integer newPriority) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("priority", newPriority);
            db.update("WordPriority", values, "Word = " + word, null);//TODO: not sure if this is how the whereClause is used here.
            db.close();
            return true;
        }
        return false;
    }

    public boolean updateWord(String oldWord,String newWord) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("Word", newWord);
            db.update("WordPriority", values, "Word = " + oldWord, null);//TODO: not sure if this is how the whereClause is used here.
            db.close();
            return true;
        }
        return false;
    }

    public boolean deletePrioritiyWord(String word) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            if (db.delete("WordPriority", "Word = " + word, null) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, somthign was deleted.
            db.close();
            return true;//note that Word is also the PK of the row, so there can only be 0/1 as a return value;
        }
        db.close();
        return false;
    }

    /*
     *
     * in this section there are methods for the table @SubActivity will be int the order of C.R.U.D
     *
     */
    public boolean insertSubActivity(SubActivity subActivity, int ActivityTaskID){
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        //check if the database opened if not retuning false
        if(db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("Content", subActivity.getContent());//insert the value to Content values
            values.put("ActivityTaskID", ActivityTaskID);//insert the id of the ActivityTask to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
                db.insert("SubActivity", null, values);//insert into table WordPriority the new word

            db.close();
            return true;
        }
        return false;

    }

    /*
     *
     * in this section there are methods for the table @ActivityTask will be int the order of C.R.U.D
     *
     */
    /*public boolean insertActivityTask(MasloCategorys category, Repetition repetition, String content, Date timeOfActivity, ArrayList<SubActivity> subActivity, int priority){
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        //check if the database opened if not retuning false
        if(db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("DateAndTime", timeOfActivity);//insert the value to Content values
            values.put("Content", content);//insert the value to values
            values.put("Category", category);//insert the value to values
            values.put("Priority", repetition);//insert the value to values
            values.put("Priority", priority);//insert the value to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
                db.insert("WordPriority", null, values);//insert into table WordPriority the new word

            db.close();
            return true;
        }
        return false;
    }*/


    public ArrayList<ActivityTask> queryForActivityTaskByPriority(int priority) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "Priority = " + priority, null, null, null, null);
            //we retrieved the data, now we create the ActTasks LOCALLY(!!!) and retrieve their individual SubActivities to complete the data.
            retrivedActTsk.moveToFirst();//required to get to the first record!

            do
            {//for each record in the retrivedActTsk. preferred "do while" to illustrate the 0th element issue.

                //getting subActivities to attach to ActivityTask
                //TODO: this is inefficient, requiring a query for each record in the retrived retrivedActTsk.
                Cursor retrivedSubAct = db.rawQuery("SELECT SubActivityID,ActivityTaskID,Content FROM SubActivity WHERE ActivityTaskID = " + retrivedActTsk.getInt(0), null);

                ArrayList<SubActivity> relatedSubActivities = new ArrayList<>();//now we create the SubActivities
                retrivedSubAct.moveToFirst();
                do {
                    relatedSubActivities.add(new SubActivity(retrivedSubAct.getInt(0), retrivedSubAct.getInt(1), retrivedSubAct.getString(2)));
                } while (retrivedSubAct.moveToNext());
                retrivedSubAct.close();
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();

        }
        db.close();
        return activityTasks;
    }
}
