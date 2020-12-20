package module;

import android.content.Context;
import android.net.IpSecManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;
import com.example.rems.MainActivity;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityTask {

    //TODO: make sure the ID is assigned correctly! <-- CURRENT; pre-optimazation is the devils curse.
    private int activityTaskID;
    private int priority;
    private MasloCategorys category;
    private Repetition repetition;
    private String content;
    private LocalDateTime timeOfActivity;
    private ArrayList<SubActivity> subActivities;

    /**
     * for quering
     *
     * @param activityTaskID
     * @param priority
     * @param category
     * @param repetition
     * @param content
     * @param timeOfActivity
     * @param subActivities
     */
    public ActivityTask(int activityTaskID, int priority, MasloCategorys category, Repetition repetition, String content, LocalDateTime timeOfActivity, ArrayList<SubActivity> subActivities) {
        this.priority = priority != 0 ? priority : 1;
        this.category = category;
        this.repetition = repetition;
        this.content = content;
        this.timeOfActivity = timeOfActivity;
        this.subActivities = subActivities;
        if(activityTaskID!=0) //if wasn't given id it's mean we created new reminder and need to asign it with new id in else
            this.activityTaskID=activityTaskID;
        else
            this.setActivityTaskID(activityTaskID);
    }

    //this constructor is used when the system creates an ActivityTask by itself. notice the lack of Date. this ActivityTask should  be passed to AI_Assignment.assignDate method.

    /***
     *
     * @param priority
     * @param category
     * @param repetition
     * @param content
     * @param subActivities
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ActivityTask(int priority, MasloCategorys category, Repetition repetition, String content, ArrayList<SubActivity> subActivities) {//TODO:
        this.timeOfActivity=ActivityTask.AItimeSetter(content,priority);
        if(timeOfActivity!=null){
            this.setActivityTaskID(DataBaseHelper.getInstance(null).getMaxIdOfActivityTask());//sets the ID for current use (if we don't do so we would need to restart the app to get the ID from the DB)
            this.priority = priority != 0 ? priority : 1;
            this.category = category;
            this.repetition = repetition;
            this.content = content;
            //timeOfActivity will be assigned in a AI_Assignment method.
            this.subActivities = subActivities;
        }

    }

    //TODO: implament remove() method, using the databasehelper and ActivityTasksUsed classes.

    /***
     *
     * @param subActivity
     * @return
     */
    public boolean addSubActivity(SubActivity subActivity) {//if the subActivity already exist in the array, do not add it to the array.
        for (SubActivity subActivityCurrent : subActivities
        ) {
            if (subActivityCurrent.getContent() == subActivity.getContent()) {
                return false;
            }
        }
        subActivities.add(subActivity);
        return true;
    }

    /***
     *
     * @param content
     * @return
     */
    public boolean removeSubActivity(String content) {//if subActivity exists, remove it from the array, otherwise return false;
        for (SubActivity subActivityCurrent : subActivities
        ) {
            if (subActivityCurrent.getContent() == content) {
                subActivities.remove(subActivityCurrent);
                return true;
            }
        }
        return false;
    }

    /***
     *
     * @param newContent
     * @param newTime
     * @param newCategory
     * @param newRepetition
     * @return
     */
    public boolean editReminder(String newContent, LocalDateTime newTime, MasloCategorys newCategory, Repetition newRepetition) {
        if (newContent != null) this.setContent(newContent);
        if (newTime != null) this.setTimeOfActivity(newTime);
        if (newCategory != null) this.setCategory(newCategory);
        if (newRepetition != null) this.setRepetition(newRepetition);
        return true;
    }


    public int getActivityTaskID() {
        return activityTaskID;
    }

    public void setActivityTaskID(int activityTaskID) {
        if (!(activityTaskID <= 0)) this.activityTaskID = activityTaskID;
        this.activityTaskID = DataBaseHelper.getInstance(null).getMaxIdOfActivityTask(); //TODO:deleted the +1 because id made all ActivityTasks to be 1 id ahead when fetching from DB,make sure not bugging insertion
    }

    public void setPriority(int priority) {
        this.priority = priority != 0 ? priority : 1;
        ;
    }

    public void setCategory(MasloCategorys category) {
        this.category = category;
    }

    public void setRepetition(Repetition repetition) {
        this.repetition = repetition;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeOfActivity(LocalDateTime timeOfActivity) {
        this.timeOfActivity = timeOfActivity;
    }

    public void setSubActivities(ArrayList<SubActivity> subActivities) {
        this.subActivities = subActivities;
    }

    public int getPriority() {
        return priority;
    }

    public MasloCategorys getCategory() {
        return category;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimeOfActivity() {
        return timeOfActivity;
    }

    public ArrayList<SubActivity> getSubActivities() {
        return subActivities;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime AItimeSetter(String content,int priority){//set time by natty library if natty library filed to set time will notify used to set it by hand\change content TODO:add to the book
        String activityDate="";
        ArrayList<ActivityTask> remindersOfTheDay=null;
        //using natty library to see is word can generate a time and date
        List<DateGroup> groups;
        Parser parser = new Parser();
        groups = parser.parse(content);
        if (groups.size()>0){//check if i got a date
            List dates = groups.get(0).getDates();//get the date that natty created for us
            LocalDateTime ldt= ((Date) dates.get(0)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); //convert it to LocalDateTIme
            //check if a content contains a timeWord of a 'day' value,if so that means the reminder is not
            if(!ldt.isBefore(LocalDateTime.now())) {//check if the date is in the past then do not return it.
                activityDate=""+ldt.getYear()+"-"+ldt.getMonth()+"-"+ldt.getDayOfMonth();//convert the date to a string to get all the reminders at this day
                remindersOfTheDay = ActivityTasksUsed.getTodaysActivities(activityDate);//get all reminders at this day

                if (checkIfWordExist(content, "before")) { //check if there's before and there are reminders today
                    //region before time setter
                    //find the one before this reminder
                    int theOneBefore=0;//used to find the reminder before the time we got from natty
                    if(remindersOfTheDay.size()>0){//if we don't have reminders today we won't relay on it
                        for(ActivityTask activityTask:remindersOfTheDay){
                            if(activityTask.getTimeOfActivity().compareTo(ldt)>0){//if the time of the before is bigger then the ldt then stop
                                break;
                            }
                            theOneBefore++;//used to find the index in the list of the last reminder before the ldt time
                        }
                        ActivityTask taskBeforeNewOne=remindersOfTheDay.get(theOneBefore);//has the task before the time of the one we try to insert
                        ActivityTask taskNextOfBeforeOneInList=remindersOfTheDay.get(theOneBefore+1);//has the next task in the list after the one that is before the new one we try to insert
                        int minutesToAdd=30;
                        //TODO: change it to check priority,if the new one priority is bigger
                        if(AIPriority(remindersOfTheDay.get(theOneBefore).getPriority(), priority)){//if the before reminder has a bigger priority will return false, if returned true need to reschedule the before reminder
                            //will nee to call rescheduale()
                            //if got true then set new reminder time to the activitytask old time, else try to set time with a 30 minutes difference
                        }
                        while (taskNextOfBeforeOneInList.getTimeOfActivity().compareTo(taskBeforeNewOne.getTimeOfActivity().plusMinutes(minutesToAdd))>0)//if the after one is the same time as if we added 20 minutes to the before one then we need to decreas 4-7 minutes from the time we try to insert.
                            minutesToAdd-=5;
                        ldt=taskBeforeNewOne.getTimeOfActivity().plusMinutes(minutesToAdd);//set the new time of what's before + x minutes
                    }
                    return ldt;
                    //endregion before time setter
                }
                else if(checkIfWordExist(content,"after")) { //check if there's after and there are reminders today
                //region after time setter

                    //TODO:finish the before section and copy the code here
                    if (remindersOfTheDay.size()>0) {//if we don't have reminders today we won't relay on it
                        int theOneBefore = 0;
                        for (ActivityTask activityTask : remindersOfTheDay) {
                            theOneBefore++;//used to find the index in the list of the last reminder after the ldt time
                            if (activityTask.getTimeOfActivity().compareTo(ldt) > 0) {//if the time of the before is bigger then the ldt then stop
                                break;
                            }
                        }
                        ActivityTask taskBeforeNewOne=remindersOfTheDay.get(theOneBefore);//has the task before the time of the one we try to insert
                        ActivityTask taskNextOfBeforeOneInList=remindersOfTheDay.get(theOneBefore+1);//has the next task in the list after the one that is before the new one we try to insert
                        int minutesToAdd=20;
                     //if()
                          while (taskNextOfBeforeOneInList.getTimeOfActivity().compareTo(taskBeforeNewOne.getTimeOfActivity().plusMinutes(minutesToAdd))<0)//if the after one is the same time as if we added 20 minutes to the before one then we need to decreas 4-7 minutes from the time we try to insert.
                              minutesToAdd-=4;
                        ldt=taskBeforeNewOne.getTimeOfActivity().plusMinutes(minutesToAdd);//set the new time of what's before + x minutes
                    }
                }
            //endregion after time setter
            }
            else if(remindersOfTheDay.size()>0){//check if current time collide with another reminder
                for(ActivityTask activityTask:remindersOfTheDay){
                    if(ldt.compareTo(activityTask.getTimeOfActivity())==0){ //if there's a collusion reduce 4 minutes
                        ldt.minusMinutes(4);
                    }
                }
                return ldt;
            }
            return ldt;//if no collusion with another reminder with the same time and no before/after requirement then return the time
        }
        return null; //if didn't got date and time by natty library then return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean AIPriority(int priorityCurrentPlaceHolder,int priorityNewReminder){//if the current reminder has bigger priority return false, else if the new one has bigger priority return true//TODO:add to the book
        return priorityCurrentPlaceHolder>priorityNewReminder ? false:true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean ReReSchedule(ActivityTask activityTask){

    //TODO:make an dialog box asking id usr want to accept new times of rescheduale reminder and the installetion of the new one instad
        int minutesToSubtract=30;
        LocalDateTime dateAndTime=activityTask.getTimeOfActivity();
        String activityDate=""+dateAndTime.getYear()+"-"+dateAndTime.getMonth()+"-"+dateAndTime.getDayOfMonth();//convert the date to a string to get all the reminders at this day
        while (ActivityTasksUsed.findExactActivityTask(0,0,dateAndTime.minusMinutes(minutesToSubtract),null,null,null)!=null) {
            minutesToSubtract-=5;
        }
        dateAndTime=dateAndTime.minusMinutes(minutesToSubtract);
        activityTask.setTimeOfActivity(dateAndTime);

        //need to call method that create dialog box asking for confirmation
        //if confirmed return true, else return false

        return true;
    }

    public static String getPreviousWord(String str, String word){//used to get the previous word befor AN\PM to get the time TODO:not sure if needed if yes add to the book
        Pattern p = Pattern.compile("([^ ]+)\\W+"+word);//take all the words before the @word until you see a space '  '
        Matcher m = p.matcher(str);
        return  m.find()? m.group(1):"NAN";
    }

    public static boolean checkIfWordExist(String str, String word){//used check if word exist in the string TODO:add to the book
        String regex=".*?\\b(?i)("+word+")\\b.*";//check all string(.*) up to the first instance(?) start of boundary(\b) and ignore case sensitivity((?i)) the word is (word) end of boundary(\b) and all the next of the string after(.*)
        return  str.matches(regex);
    }
}

