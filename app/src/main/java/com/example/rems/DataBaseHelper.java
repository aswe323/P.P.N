package com.example.rems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageSwitcher;
import android.widget.Toast;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import module.ActivityTask;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;

import static java.sql.Types.NULL;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sysDB;
    private Context context;//TODO: @LIOR! do not do this, THIS IS A MEMORY LEAK!  REALLY REALLY BAD!
    private static String DATABASE_NAME = "remsDB.db";
    private static int DATABASE_VERSION = 1;
    private static DataBaseHelper DataBaseHelper_Instance;

    public static DataBaseHelper getInstance(Context context) {
        if (DataBaseHelper_Instance == null) DataBaseHelper_Instance = new DataBaseHelper(context);
        return DataBaseHelper_Instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

//region methods

    public int getMaxIdOfActivityTask() { //getting the latest id that added of the latest ActivityTask that was added to the database
        SQLiteDatabase db = this.getReadableDatabase();//open the database for read\write
        Cursor cursor = db.rawQuery("SELECT MAX(ActivityTaskID) FROM ActivityTasks", null);//to browse the max id of the table ActivityTasks
        int maxId = (cursor.moveToFirst() ? cursor.getInt(0) : 0);//go to the start of the cursor and check the ID column and return the int value,if the column is empty return 0 to the int
        cursor.close();
        return maxId;
    }

    /*
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *                                                                                                 *
    * in this section there are methods for the table @WordPriority will be int the order of C.R.U.D  *
    *                                                                                                 *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
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
                if(db.insert("WordPriority", null, values)==-1) {//insert into table WordPriority the new word, if returned -1 insert failed
                    db.close();
                    return false;
                }
            db.close();
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
                returned.put(data.getString(0), data.getInt(1));//WordPriority only got 2 columns, the Word(String), and the Priority(Int).
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
        return false;
    }


    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                               *
     * in this section there are methods for the table @SubActivity will be in the order of C.R.U.D  *
     *                                                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
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
            cursor.moveToFirst();
            do {
                SubActivity subActivity = new SubActivity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)); //create the SubActivity object from the DB data
                subarray.add(subActivity); //add the SubActivity to the ArrayList
            } while (cursor.moveToNext());//move to the next line,if there is no more lines then end the loop
            cursor.close();
        }
        db.close();
        return subarray;
    }

    public boolean updateSubActivity(SubActivity subActivity,String content){//updates the SubActivities
        SQLiteDatabase db = this.getWritableDatabase();//open the database for read/write

        if(db.isOpen()){
            ContentValues values = new ContentValues(); //will store the new value for the database
            values.put("Content", content);//inset the new value for column @Content
            db.update("SubActivity", values, "SubActivityID = " + subActivity.getSubActivityID(), null);//TODO: not sure if this is how the whereClause is used here.
            db.close();
            return true;
        }
        return false;
    }

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
    
    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                                                                 *
     * in this section there are methods for the table @ActivityTask will be int the order of C.R.U.D  *
     *                                                                                                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public boolean insertActivityTask(MasloCategorys category, Repetition repetition, String content, Date timeOfActivity, ArrayList<SubActivity> subActivity, int priority){
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask
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
                if(db.insert("WordPriority", null, values)==-1) {//insert into table ActivityTasks the new ActivityTask, if returned -1 insert failed
                    db.close();
                    return false;
                }
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
    public ArrayList<ActivityTask> queryForExactActivityTask(int activityTaskID, int priority, Date dateAndTime, String content, Repetition repetition, MasloCategorys masloCategory) throws ParseException {

        if (activityTaskID < 0 && priority < 0 && dateAndTime == null && content == null && repetition == null && masloCategory == null)
            return null;
        ArrayList<ActivityTask> activityTasks = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        if (db.isOpen()) {
            String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

            //region TODO: the idea is that the query whereClauseArgs parameter already takes care of sqlinjections, so i wanted to use that, the complicatoin comes from deciding where to put the AND operator in the where clause.
            ContentValues values = new ContentValues();
            values.put("ActivityTask ", activityTaskID >= 0 ? String.valueOf(activityTaskID) : "ActivityTask");
            values.put("DateAndTime ", dateAndTime != null ? String.valueOf(dateAndTime) : "DateAndTime");
            values.put("Content ", content != null ? content : "Content");
            values.put("Repetition ", repetition != null ? String.valueOf(repetition) : "Repetition");
            values.put("Category ", masloCategory != null ? String.valueOf(masloCategory) : "MasloCategory");
            values.put("Priority ", priority >= 0 ? String.valueOf(priority) : "Priority");
            ContentValues whereClauseArgs = new ContentValues();
            for (String key : values.keySet()) {
                String value = (String) values.get(key);
                if (!String.valueOf(values.get(key)).equals(key)) {//if the key is the same as the value, we got an empty argument, and it should not be included in the where clause.
                    whereClauseArgs.put(String.valueOf(key), whereClauseArgs.keySet().size() > 0 ? " AND " + key + " = " + value : key + " = " + value);//voodoo magic.
                }
            }
            String[] whereArgsArray = new String[5];
            int i = 0;
            for (String key : whereClauseArgs.keySet()) {
                whereArgsArray[i++] = String.valueOf(whereClauseArgs.get(key));
            }
            //endregion

            if (db.isOpen()) {
                Cursor cursor = db.query("ActivityTasks", columnsFromActTsk, "?  ?  ?  ?  ?  ?", whereArgsArray, null, null, null);
                cursor.moveToFirst();
                do {
                    ArrayList<SubActivity> relatedSubAct = queryForSubActivity(cursor.getInt(0));//the related subAct to the ActTsk
                    Date TextToDate = (Date) formatter.parse(cursor.getString(1)); //we need to parse the date that is a String in the database to Date
                    activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                            cursor.getInt(5),//priority
                            MasloCategorys.valueOf(cursor.getString(4)),//MasloCategory
                            Repetition.valueOf(cursor.getString(3)),//Repetition
                            cursor.getString(2),//Content
                            TextToDate,//DateTime
                            relatedSubAct//SubActivities
                    ));
                } while (cursor.moveToNext());
                cursor.close();
            }

        }

        db.close();
        return activityTasks;

    }

    //TODO: matan is an idiot, this is repeated code and is the devils work... all hail satan. :'(
    public ArrayList<ActivityTask> queryForActivityTaskByPriority(int priority) throws ParseException { //this method will return all the ActivityTasks with the @priority that was sent.
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "Priority = " + priority, null, null, null, null);
            //we retrieved the data, now we create the ActTasks LOCALLY(!!!) and retrieve their individual SubActivities to complete the data.
            retrivedActTsk.moveToFirst();//required to get to the first record!

            do {//for each record in the retrivedActTsk. preferred "do while" to illustrate the 0th element issue.

                //getting subActivities to attach to ActivityTask
                //TODO: this is inefficient, requiring a query for each record in the retrived retrivedActTsk.
                Cursor retrivedSubAct = db.rawQuery("SELECT SubActivityID,ActivityTaskID,Content FROM SubActivity WHERE ActivityTaskID = " + retrivedActTsk.getInt(0), null);

                ArrayList<SubActivity> relatedSubActivities = new ArrayList<>();//now we create the SubActivities
                retrivedSubAct.moveToFirst();
                do {
                    relatedSubActivities.add(new SubActivity(retrivedSubAct.getInt(0), retrivedSubAct.getInt(1), retrivedSubAct.getString(2)));
                } while (retrivedSubAct.moveToNext());
                retrivedSubAct.close();

                //get the data from the database and construct the ActivityTask
                Date TextToDate= (Date) formatter.parse(retrivedActTsk.getString(1)); //we need to parse the date that is a String in the database to Date
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        TextToDate,//DateTime
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();
        }
        db.close();
        return activityTasks;
    }

    public ArrayList<ActivityTask> queryForActivityTaskByTime(Date time) throws ParseException { //returns all the ActivityTasks with the save date and time
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it
        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask
        String DateInFormat=formatter.format(time);//making a String out of the @Date that was sent to use in the query

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "DateAndTime = " + DateInFormat, null, null, null, null);
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

                //get the data from the database and construct the ActivityTask
                Date TextToDate= (Date) formatter.parse(retrivedActTsk.getString(1)); //we need to parse the date that is a String in the database to Date
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        TextToDate,//DateTime
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();
        }
        db.close();
        return activityTasks;
    }

    public ArrayList<ActivityTask> queryForActivityTaskByRepetition(Repetition repetition) throws ParseException { //this method will return all the ActivityTasks with the @repetition that was sent.
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "Repetition = " + repetition.toString(), null, null, null, null);
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

                //get the data from the database and construct the ActivityTask
                Date TextToDate= (Date) formatter.parse(retrivedActTsk.getString(1)); //we need to parse the date that is a String in the database to Date
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        TextToDate,//DateTime
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();
        }
        db.close();
        return activityTasks;
    }

    public ArrayList<ActivityTask> queryForActivityTaskByCategory(MasloCategorys category) throws ParseException { //this method will return all the ActivityTasks with the @category that was sent.
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "Category = " + category.toString(), null, null, null, null);
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

                //get the data from the database and construct the ActivityTask
                Date TextToDate= (Date) formatter.parse(retrivedActTsk.getString(1)); //we need to parse the date that is a String in the database to Date
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        TextToDate,//DateTime
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();
        }
        db.close();
        return activityTasks;
    }

    public ArrayList<ActivityTask> queryForActivityTaskByString(String string) throws ParseException { //this method will return all the ActivityTasks with the @category that was sent.
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        ArrayList<ActivityTask> activityTasks = new ArrayList<>();
        String[] columnsFromActTsk = new String[]{"ActivityTaskID", "DateAndTime", "Content", "Repetition", "Category", "Priority"};// the columns we are looking for in the ActivityTask Table
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask

        if (db.isOpen()) {

            Cursor retrivedActTsk = db.query("ActivityTasks", columnsFromActTsk, "Content LIKE ?", new String[]{string+"%"}, null, null, null); //TODO: check if the like statement works
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

                //get the data from the database and construct the ActivityTask
                Date TextToDate= (Date) formatter.parse(retrivedActTsk.getString(1)); //we need to parse the date that is a String in the database to Date
                activityTasks.add(new ActivityTask(//FOREACH record in the retrivedActTsk Cursor, we create a task.
                        retrivedActTsk.getInt(5),//priority
                        MasloCategorys.valueOf(retrivedActTsk.getString(4)),//MasloCategory
                        Repetition.valueOf(retrivedActTsk.getString(3)),//Repetition
                        retrivedActTsk.getString(2),//Content
                        TextToDate,//DateTime
                        relatedSubActivities//SubActivities
                ));
            } while (retrivedActTsk.moveToNext());
            retrivedActTsk.close();
        }
        db.close();
        return activityTasks;
    }

    public boolean updateActivityTask(ActivityTask activityTask){//update the ActivityTask that was passed (using the exciting id)
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write in it

        if(db.isOpen()){
            ContentValues values = new ContentValues(); //will store the new value for the database
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");// for Date in ActivityTask
            String DateInFormat=formatter.format(activityTask.getTimeOfActivity());//create a string with the date that was sent in the format we want

            values.put("DateAndTime", DateInFormat);//inset the new date for column @DateAndTime
            values.put("Content", activityTask.getContent());//inset the new content for column @Content
            values.put("Repetition", activityTask.getRepetition().toString());//inset the new repetition for column @Repetition
            values.put("Category", activityTask.getCategory().toString());//inset the new category for column @Category
            values.put("Priority", activityTask.getPriority());//inset the new priority for column @Priority
            db.update("ActivityTasks ", values, "ActivityTaskID = " + activityTask.getActivityTaskID(), null);//TODO: not sure if this is how the whereClause is used here.
            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteActivityTask(ActivityTask activityTask){ // delete the ActivityTask that was passed
        SQLiteDatabase db = this.getWritableDatabase();//open the database to write/read

        if (db.isOpen()) {
            if (db.delete("ActivityTasks", "ActivityTaskID = " + activityTask.getActivityTaskID(), null) > 0)
                return true; //db.delete returns the number of effected rows, if it is larger the 0, something was deleted.
            db.close();
            return true;//note that ActivityTaskID is also the PK of the row, so there can only be 0/1 as a return value;
        }
        return false;
    }
//endregion methods
}
