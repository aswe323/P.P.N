package module;

import android.net.IpSecManager;

import java.util.ArrayList;
import java.util.Date;

public class ActivityTask {

    //TODO: make sure the ID is assigned correctly!
    private int activityTaskID;
    private int priority;
    private MasloCategorys category;
    private Repetition repetition;
    private String content;
    private Date timeOfActivity;
    private ArrayList<SubActivity> subActivities;


    //this constructor is used when creating an ActivityTask manuelly.
    public ActivityTask(int priority, MasloCategorys category, Repetition repetition, String content, Date timeOfActivity, ArrayList<SubActivity> subActivities) {
        this.priority = priority;
        this.category = category;
        this.repetition = repetition;
        this.content = content;
        this.timeOfActivity = timeOfActivity;
        this.subActivities = subActivities;


        //TODO: handle pulling tasks from the databse, making sure you can create a new ActivityTask object with the correct ID.
        //this.activityTaskID = DataBaseHelper.getMaxIdOfActivityTask() + 1;// unimplamented yet
    }

    //this constructor is used when the system creates an ActivityTask by itself. notice the lack of Date. this ActivityTask should  be passed to AI_Assignment.assignDate method.
    public ActivityTask(int priority, MasloCategorys category, Repetition repetition, String content, ArrayList<SubActivity> subActivities) {
        this.priority = priority;// the priority is
        this.category = category;
        this.repetition = repetition;
        this.content = content;
        //timeOfActivity will be assigned in a AI_Assignment method.
        this.subActivities = subActivities;


        //this.activityTaskID = DataBaseHelper.getMaxIdOfActivityTask() + 1;// unimplamented yet
    }

    //TODO: implament remoce() method, using the databasehelper and ActivityTasksUsed classes.

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

    public boolean editReminder(String newContent, Date newTime, MasloCategorys newCategory, Repetition newRepetition) {
        if (newContent != null) this.setContent(newContent);
        if (newTime != null) this.setTimeOfActivity(newTime);
        if (newCategory != null) this.setCategory(newCategory);
        if (newRepetition != null) this.setRepetition(newRepetition);
        return true;
    }


    public int getActivityTaskID() {
        return activityTaskID;
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

    public void setTimeOfActivity(Date timeOfActivity) {
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

    public Date getTimeOfActivity() {
        return timeOfActivity;
    }

    public ArrayList<SubActivity> getSubActivities() {
        return subActivities;
    }


}
