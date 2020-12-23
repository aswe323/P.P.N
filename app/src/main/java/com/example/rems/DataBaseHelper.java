package com.example.rems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import module.ActivityTask;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;


public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sysDB;
    private Context context;
    private static String DATABASE_NAME = "remsDB.db";
    private static int DATABASE_VERSION = 1;
    private static DataBaseHelper DataBaseHelper_Instance;

    public static DataBaseHelper getInstance(Context context) {
        if (DataBaseHelper_Instance == null) DataBaseHelper_Instance = new DataBaseHelper(context);
        return DataBaseHelper_Instance;
    }
//make sure this is implemented in the Main →→→→ DataBaseHelper db = DataBaseHelper.getInstance(this);

    private DataBaseHelper(Context context) {
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
                "FOREIGN KEY(ActivityTaskID) REFERENCES ActivityTasks(ActivityTaskID) ON DELETE CASCADE" +
                ")");

        //creating table "WordPriority" with columns Word,Priority
        db.execSQL("CREATE TABLE WordPriority (" +
                "Word TEXT PRIMARY KEY," +
                "Priority INTEGER NOT NULL" +
                ")");

        //creating table "BucketWords" with columns Word,Priority
        db.execSQL("CREATE TABLE BucketWords (" +
                "Word TEXT PRIMARY KEY," +
                "Range TEXT NOT NULL" +
                ")");

        Toast.makeText(context, "DataBase was created", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//used when you want to update the database,when called need to increment the version
        //delete the old database by dropping the tables
        db.execSQL("DROP TABLE IF EXISTS " + "ActivityTasks");
        db.execSQL("DROP TABLE IF EXISTS " + "SubActivity");
        db.execSQL("DROP TABLE IF EXISTS " + "WordPriority");
        db.execSQL("DROP TABLE IF EXISTS " + "BucketWords");//TODO: what about the data? you created the database again with the tables, but lost ALL of the data you had there.
        // you didn't UPDATE anything.. you resetted the DB.
        //pls fix.
        //Toast.makeText(context, "database was created", Toast.LENGTH_SHORT).show();
        onCreate(db);// Create tables again
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    //region methods

    public int getMaxIdOfActivityTask() { //getting the latest id that added of the latest ActivityTask that was added to the database
        SQLiteDatabase db = this.getReadableDatabase();//open the database for read\write
        int maxId = 0;
        Cursor cursor = db.rawQuery("SELECT MAX(ActivityTaskID) FROM ActivityTasks", null);//to browse the max id of the table ActivityTasks
        if (cursor != null && cursor.getCount() > 0)
            maxId = (cursor.moveToFirst() ? cursor.getInt(0) : 0);//go to the start of the cursor and check the ID column and return the int value,if the column is empty return 0 to the int
        cursor.close();
        return maxId;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int countCompletedTasks() {//TODO: add to book
        ArrayList<ActivityTask> completed = queryForAllActivityTasks();
        for(int i=0;i<completed.size();i++){
            ActivityTask activityTask=completed.get(i);
            if (activityTask.getPriority() > 0) {
                completed.remove(activityTask);
            }
        }
        /*completed.forEach(activityTask ->
                {
                    if (activityTask.getPriority() > 0) {
                        completed.remove(activityTask);
                    }
                }
        );*/
        return completed.size();

    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                                 *
     * in this section there are methods for the table @WordPriority will be int the order of C.R.U.D  *
     *                                                                                                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    //region WordPriority
    public boolean insertPriorityWord(String word, int priority) {

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        //check if the database opened if not retuning false
        if (db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("Word", word);//insert the value to Content values
            values.put("Priority", priority);//insert the value to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
                if(db.insert("WordPriority", null, values)==-1) {//insert into table WordPriority the new word, if returned -1 insert failed
                    db.close();
                    return false;
                }
            db.close();

            //test toast
            Toast.makeText(context, "inserted word: "+word+" with priority: "+priority, Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }

    public Map<String, Integer> queryForPriorityWords() {// get ALL of the priority words from the table. and return them as a Map.
        Map<String, Integer> returned = new HashMap<>();//the Map that will be returned

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            Cursor data = db.rawQuery("select * from WordPriority", null);//we don't need Args because we wan't ALL of the data.
            if(!(data!=null && data.getCount()>0)) //check if there is data that was taken from the database,if not return the nulled map;
                return returned;
            data.moveToFirst();//required because the cursor is set on the 0th element, which hold is nothing.
            do {
                returned.put(data.getString(0), data.getInt(1));//WordPriority only got 2 columns, the Word(String), and the Priority(Int).
            } while (data.moveToNext());
            data.close();

            //test toast
            Toast.makeText(context, "sented a map of words", Toast.LENGTH_SHORT).show();
        }

        return returned;
    }

    //
    //TODO:Update the map of ActivityTaskUsed after every update
    //
    public boolean updatePrioritiyOfWord(String word, Integer newPriority) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("priority", newPriority);
            db.update("WordPriority", values, "Word = ?", new String[]{word});
            db.close();
            return true;
        }
        return false;
    }

    public boolean updateWord(String oldWord,String newWord,int score) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("Word", newWord);
            values.put("Priority", score);
            db.update("WordPriority", values, "Word = ?" , new String[]{oldWord});
            db.close();
            return true;
        }
        return false;
    }

    public boolean deletePrioritiyWord(String word) {
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            if (db.delete("WordPriority", "Word = ?", new String[]{word}) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, somthign was deleted.
            db.close();
            return true;//note that Word is also the PK of the row, so there can only be 0/1 as a return value;
        }
        return false;
    }
    //endregion

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                                 *
     * in this section there are methods for the table @BucketWords will be int the order of C.R.U.D  *
     *                                                                                                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    //region BucketWords

    /**
     * This method is used to add the new bucket word to the database.<br>
     * the method connect to the database, if connection failed returning false else adding to a ContentValues the @Word and the @Range.<br>
     * if ContentValues isn't empty we call an insert method to table BucketWords with our ContentValues,if inserting was successful pushing a toast and returning true.
     *
     * @param word String representing the word.
     * @param range String representing the rage of the bucket word.
     * @return true on successful insertion, false on insertion fail.
     */
    public boolean insertBucketWord(String word, String range) { //TODO:add to the book

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        //check if the database opened if not retuning false
        if (db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("Word", word);//insert the value to Content values
            values.put("Range", range);//insert the value to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
            if(db.insert("BucketWords", null, values)==-1) {//insert into table BucketWords the new word, if returned -1 insert failed
                db.close();
                return false;
            }
            db.close();

            //test toast
            Toast.makeText(context, "inserted word: "+word+" with range of: "+range, Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }

    /**
     * a method used to read all the bucket words from the database.<br>
     * the method create a map to contain the bucket words that will be returned and connect to the database.<br>
     * if connection failed returning false else using a Cursor to fetch the data with a query from the database,
     * if the Cursor isn't empty and not null we use a loop to put the contained bucket words in the Cursor to a map that then will be returned.
     *
     * @return map containing all the bucket words from the database.
     */
    public Map<String, String> queryForBucketWords() {// get ALL of the bucket words from the table. and return them as a Map. TODO:add to the book
        Map<String, String> returned = new HashMap<>();//the Map that will be returned

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            Cursor data = db.rawQuery("select * from BucketWords", null);//we don't need Args because we wan't ALL of the data.
            if(!(data!=null && data.getCount()>0)) //check if there is data that was taken from the database,if not return the nulled map;
                return returned;
            data.moveToFirst();//required because the cursor is set on the 0th element, which hold is nothing.
            do {
                returned.put(data.getString(0), data.getString(1));//BucketWords only got 2 columns, the Word(String), and the range(String).
            } while (data.moveToNext());
            data.close();

            //test toast
            Toast.makeText(context, "sented a map of bucket words", Toast.LENGTH_SHORT).show();
        }

        return returned;
    }

    /**
     * this method used for updating a bucket word itself.<br>
     * the method connect to the database, if connection failed returning false else adding to a ContentValues the @Word and the @Range then calls the update query to update the bucket word itself.
     *
     * @param oldWord String used to identify which word to update.
     * @param newWord String of the new word to update too.
     * @param Range String to save the range the word has from being deleted.
     * @return true on successful update, false on failure.
     */
    public boolean updateBucketWord(String oldWord,String newWord,String Range) {//update the bucket word  TODO:add to the book
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("Word", newWord);
            values.put("Range", Range);
            db.update("BucketWords", values, "Word = ?" , new String[]{oldWord});
            db.close();
            return true;
        }
        return false;
    }

    /**
     * this method is used to delete a bucket word.
     * the method connect to the database, if connection failed returning false else calling a delete query and returning true.
     *
     * @param word String used to identify which word to delete.
     * @return true on successful delete, false on failure.
     */
    public boolean deleteBucketWord(String word) {//delete a bucket word TODO:add to the book
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            if (db.delete("BucketWords", "Word = ?", new String[]{word}) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, somthign was deleted.
            db.close();
            return true;//note that Word is also the PK of the row, so there can only be 0/1 as a return value;
        }
        return false;
    }
    //endregion

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                               *
     * in this section there are methods for the table @SubActivity will be in the order of C.R.U.D  *
     *                                                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    //region SubActivity
    public boolean insertSubActivity(SubActivity subActivity, int ActivityTaskID){//inserting the SubActivity to the database
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        //check if the database opened if not retuning false
        if(db.isOpen()) {
            ContentValues values = new ContentValues(); //will hold data of the values to insert into the table
            values.put("Content", subActivity.getContent());//insert the value to Content values
            values.put("ActivityTaskID", ActivityTaskID);//insert the id of the ActivityTask to values
            //check if the item was added successfully if not retuning false
            if (values.size() < 1)
                return false;
            else
                if(db.insert("SubActivity", null, values)==-1) {//insert into table WordPriority the new word, if returned -1 insert failed
                    db.close();
                    return false;
                }

            db.close();
            return true;
        }
        return false;
    }

    public ArrayList<SubActivity> queryForSubActivity(int ActivityTaskID){//returning ArrayList of all the SubActivities of an ActivityTask (by the ActivityTaskID)
        ArrayList<SubActivity> subarray=new ArrayList<SubActivity>(); //creating an ArrayList that will store all SubActivities
        SQLiteDatabase db = this.getWritableDatabase();//open the database for read/write

        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from SubActivity where ActivityTaskID =" + ActivityTaskID,null); //selecting all the SubActivities of the ActivityTask
            if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
                do {
                    SubActivity subActivity = new SubActivity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)); //create the SubActivity object from the DB data
                    subarray.add(subActivity); //add the SubActivity to the ArrayList
                } while (cursor.moveToNext());//move to the next line,if there is no more lines then end the loop
            }
            cursor.close();
        }
        db.close();
        return subarray;
    }

    //
    //TODO:after update for the database we need to update the object in the program too
    //
    public boolean updateSubActivity(SubActivity subActivity,String content){//updates the SubActivities
        SQLiteDatabase db = this.getWritableDatabase();//open the database for read/write

        if(db.isOpen()){
            ContentValues values = new ContentValues(); //will store the new value for the database
            values.put("Content", content);//inset the new value for column @Content
            db.update("SubActivity", values, "SubActivityID = " + subActivity.getSubActivityID(), null);
            db.close();
            return true;
        }
        return false;
    }
    //
    //TODO:after update for the database we need to update the object in the program too
    //
    public boolean deleteSubActivity(SubActivity subActivity){//delete the SubActivity
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if (db.isOpen()) {
            if (db.delete("SubActivity", "SubActivityID = " + subActivity.getSubActivityID(), null) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, somthing was deleted.
            db.close();
            return true;//note that SubActivityID is also the PK of the row, so there can only be 0/1 as a return value;
        }
        return false;
    }
    //endregion


    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                                 *
     * in this section there are methods for the table @ActivityTask will be int the order of C.R.U.D  *
     *                                                                                                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    //region ActivityTask
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertActivityTask(MasloCategorys category, Repetition repetition, String content, LocalDateTime timeOfActivity, ArrayList<SubActivity> subActivity, int priority){
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");// for Date in ActivityTask
        String DateInFormat=formatter.format(timeOfActivity);//create a string with the date that was sent in the format we want

        if(db.isOpen()) {//check if the database opened if not retuning false
            ContentValues values = new ContentValues(); //will hold Strings of the values to insert into the table
            values.put("DateAndTime", DateInFormat);//insert the value of the date to Content values
            values.put("Content", content);//insert the value of content to values
            values.put("Category", category.toString());//insert the value of category to values
            values.put("Repetition", repetition.toString());//insert the value of Repetition to values
            values.put("Priority", priority);//insert the value of priority to values

            if (values.size() < 1) //check if the item was added successfully if not retuning false
                return false;
            else {
                if(db.insert("ActivityTasks", null, values)==-1) {//insert into table ActivityTasks the new ActivityTask, if returned -1 insert failed
                    db.close();
                    return false;
                }
                if(subActivity!=null && !subActivity.isEmpty())
                    for (SubActivity subactivityIterator : subActivity) //adding all of the SubActivities with iterator
                        insertSubActivity(subactivityIterator, subactivityIterator.getActivityTaskID()); //sending the SubActivities to be added to the database
            }
            db.close();
            return true;
        }
        return false;
    }

    /**
     * @param activityTaskID
     * @param priority
     * @param dateAndTime
     * @param content
     * @param repetition
     * @param masloCategory
     * @return
     * @throws ParseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ActivityTask> queryForExactActivityTask(int activityTaskID, int priority, LocalDateTime dateAndTime, String content, Repetition repetition, MasloCategorys masloCategory){
        String DateInFormat = null;
        if (activityTaskID < 0 && priority == 0 && dateAndTime == null && content == null && repetition == null && masloCategory == null)
            return null;
        ArrayList<ActivityTask> activityTasks = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        if (db.isOpen()) {
            String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");// for Date in ActivityTask
            if(dateAndTime!=null)
                DateInFormat=formatter.format(dateAndTime);//create a string with the date that was sent in the format we want

            ContentValues values = new ContentValues();
            values.put("activityTaskID ", activityTaskID > 0 ? String.valueOf(activityTaskID) : "activityTaskID");
            values.put("DateAndTime ", dateAndTime != null ? DateInFormat : "DateAndTime");
            values.put("Content ", content != null ? content : "Content");
            values.put("Repetition ", repetition != null ? String.valueOf(repetition) : "Repetition");
            values.put("Category ", masloCategory != null ? String.valueOf(masloCategory) : "Category");
            values.put("Priority ", priority > 0 ? String.valueOf(priority) : "Priority");
            ArrayList<String> stringArgsList=new ArrayList<>();
            String[] whereArgsArray;
            int i=0;
            String selectionArgs="";
            for (String key : values.keySet()) {
                if (!(values.get(key) + " ").equals(key)) {//if the key is the same as the value, we got an empty argument, and it should not be included in the where clause.
                    if(selectionArgs.equals(""))//if the selectionArgs is empty enter
                        if(!key.equals("Content ")) //if the condition is content then we need to you the LIKE statement in the else
                            selectionArgs = selectionArgs + key +" = ? ";
                        else {
                                selectionArgs = selectionArgs + " Content LIKE ?" ;
                                stringArgsList.add("%" + (String) values.get(key)+"%");

                            continue;
                            }
                    else{//if the selectionArgs wasn't empty enter here
                            if(!key.equals("Content "))//if the condition is content then we need to you the LIKE statement in the else
                                selectionArgs = selectionArgs + " and " + key +" = ? ";
                            else{
                                selectionArgs = selectionArgs + " and Content LIKE ?";
                                stringArgsList.add("%" + (String) values.get(key)+"%");

                                continue;
                            }
                        }
                    stringArgsList.add((String) values.get(key));
                }
            }
            whereArgsArray= new String[stringArgsList.size()];
            for(String toAdd:stringArgsList) //create the where array from the ArrayList
                whereArgsArray[i]=stringArgsList.get(i++);

            //whereArgsArray=whereString.split(" ");//create the String Array for the where values for the query
            if (db.isOpen()) {
                Cursor cursor = db.query("ActivityTasks", columnsFromActTsk, selectionArgs, whereArgsArray, null, null, null);
                if(cursor!=null && cursor.getCount()>0){ //if the cursor isn't empty enter
                    cursor.moveToFirst();
                    do {
                        ArrayList<SubActivity> relatedSubAct = queryForSubActivity(cursor.getInt(0));//the related subAct to the ActTsk
                        LocalDateTime TextToDate = LocalDateTime.parse(cursor.getString(1), formatter); //we need to parse the date that is a String in the database to Date
                        activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                                cursor.getInt(0),//activityTaskID
                                cursor.getInt(5),//priority
                                MasloCategorys.valueOf(cursor.getString(4)),//MasloCategory
                                Repetition.valueOf(cursor.getString(3)),//Repetition
                                cursor.getString(2),//Content
                                TextToDate,//DateTime
                                relatedSubAct//SubActivities
                        ));
                    } while (cursor.moveToNext());
                }
                cursor.close();

            }

        }
        db.close();
        return activityTasks;
    }

    //TODO:add to book
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ActivityTask> queryForAllActivityTasks(){
        ArrayList<ActivityTask> activities=new ArrayList<>(); //creating an ArrayList that will store all SubActivities
        SQLiteDatabase db = this.getWritableDatabase();//open the database for read/write
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");// for Date in ActivityTask
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from ActivityTasks",null); //selecting all the SubActivities of the ActivityTask
            if(cursor!=null && cursor.getCount()>0){
                cursor.moveToFirst();
                do {
                    LocalDateTime TextToDate = LocalDateTime.parse(cursor.getString(1), formatter);
                    ArrayList<SubActivity> relatedSubAct = queryForSubActivity(cursor.getInt(0));
                    ActivityTask activityTask = new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                            cursor.getInt(0),//activityTaskID
                            cursor.getInt(5),//priority
                            MasloCategorys.valueOf(cursor.getString(4)),//MasloCategory
                            Repetition.valueOf(cursor.getString(3)),//Repetition
                            cursor.getString(2),//Content
                            TextToDate,//DateTime
                            relatedSubAct//SubActivities
                    ); //create the SubActivity object from the DB data

                    activities.add(activityTask);
                } while (cursor.moveToNext());//move to the next line,if there is no more lines then end the loop
            }
            cursor.close();
        }
        db.close();
        return activities;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ActivityTask> queryForToDaysActivities(String theDay){//to schedule a reminder based on Before/After keyword in the content we need all the reminders of today that we get with this method get the reminders ordered from earliest today to last TODO:add to the book
        ArrayList<ActivityTask> activities=new ArrayList<>(); //creating an ArrayList that will store all SubActivities
        SQLiteDatabase db = this.getWritableDatabase();//open the database for read/write
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");// for Date in ActivityTask
        String rawQueryString="select * from ActivityTasks where DateAndTime > DATE('"+theDay+"','localtime') and DateAndTime < DATE('"+theDay+"','localtime','+1 day') GROUP by DateAndTime";
        if(db.isOpen()){
            Cursor cursor = db.rawQuery(rawQueryString,null); //selecting all the ActivityTask of today
            if(cursor!=null && cursor.getCount()>0){
                cursor.moveToFirst();
                do {
                    LocalDateTime TextToDate = LocalDateTime.parse(cursor.getString(1), formatter);
                    ArrayList<SubActivity> relatedSubAct = queryForSubActivity(cursor.getInt(0));
                    ActivityTask activityTask = new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                            cursor.getInt(0),//activityTaskID
                            cursor.getInt(5),//priority
                            MasloCategorys.valueOf(cursor.getString(4)),//MasloCategory
                            Repetition.valueOf(cursor.getString(3)),//Repetition
                            cursor.getString(2),//Content
                            TextToDate,//DateTime
                            relatedSubAct//SubActivities
                    ); //create the SubActivity object from the DB data

                    activities.add(activityTask);
                } while (cursor.moveToNext());//move to the next line,if there is no more lines then end the loop
            }
            cursor.close();
        }
        db.close();
        return activities;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)//for ease of use.
    public ArrayList<ActivityTask> queryForExactActivityTask(ActivityTask activityTask) {
        ArrayList<ActivityTask> activityTasks = queryForExactActivityTask(activityTask.getActivityTaskID(), activityTask.getPriority(), activityTask.getTimeOfActivity(), activityTask.getContent(), activityTask.getRepetition(), activityTask.getCategory());
        return activityTasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean updateActivityTask(ActivityTask activityTask, int ActivityTaskID){//update the ActivityTask that was passed (using the exciting id)
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if(db.isOpen()){
            ContentValues values = new ContentValues(); //will store the new value for the database
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");// for Date in ActivityTask
            String DateInFormat=formatter.format(activityTask.getTimeOfActivity());//create a string with the date that was sent in the format we want
            values.put("DateAndTime", DateInFormat);//inset the new date for column @DateAndTime
            values.put("Content", activityTask.getContent());//inset the new content for column @Content
            values.put("Repetition", activityTask.getRepetition().toString());//inset the new repetition for column @Repetition
            values.put("Category", activityTask.getCategory().toString());//inset the new category for column @Category
            values.put("Priority", activityTask.getPriority());//inset the new priority for column @Priority

            db.update("ActivityTasks ", values, "ActivityTaskID = " + ActivityTaskID, null);//TODO: need to make the ID getter when taken from DB if we want to replace the int @ActivityTaskID
            for(SubActivity subActivity:activityTask.getSubActivities()){
                    insertSubActivity(subActivity,ActivityTaskID);
            }


            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteActivityTask(int ActivityTaskID){ // delete the ActivityTask that was passed
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write/read

        if (db.isOpen()) {
            if (db.delete("ActivityTasks", "ActivityTaskID = " + ActivityTaskID, null) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, something was deleted.
            db.close();
            return true;//note that ActivityTaskID is also the PK of the row, so there can only be 0/1 as a return value;
        }
        return false;
    }
    //endregion
//endregion methods
}
