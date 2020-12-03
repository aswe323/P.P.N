package module;

import android.content.Context;
import android.net.IpSecManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;
import com.example.rems.MainActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ActivityTask {

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
        this.priority = priority != 0 ? priority : 1;
        this.category = category;
        this.repetition = repetition;
        this.content = content;
        //timeOfActivity will be assigned in a AI_Assignment method.
        this.subActivities = subActivities;

        this.setActivityTaskID(DataBaseHelper.getInstance(null).getMaxIdOfActivityTask());


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
        this.priority = priority;
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


}
