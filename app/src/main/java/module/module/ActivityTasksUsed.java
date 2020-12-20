package module;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ActivityTasksUsed {
    static private ArrayList<ActivityTask> usedTasks = new ArrayList<>();

    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context
    static private int userPersonalScore = db.countCompletedTasks();


    //region methods ActivityTask

    /**
     * used to mark an activityTask as completed, changes the priority of the activityTask to negative.
     *
     * @param activityTask to be marked as completed
     * @return true on success.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Boolean markComplete(ActivityTask activityTask) {//TODO: add to book
        int priority = activityTask.getPriority();
        if (priority >= 0) {
            activityTask.setPriority(priority * -1);
            editActivityTask(activityTask);
            userPersonalScore = db.countCompletedTasks();
            return true;
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Boolean addActivityTask(ActivityTask activityTask) {//adding the new ActivityTask to the dataBase if: a same ActivityTask data doesn't match existing ActivityTask, if added successfully adding the ArrayList used to control and manipulate the data in the system
        if (db.queryForExactActivityTask(
                0,
                activityTask.getPriority(),
                activityTask.getTimeOfActivity(),
                activityTask.getContent(),
                activityTask.getRepetition(),
                activityTask.getCategory()).isEmpty() //check if data match something that is already in thr DataBase,
                && db.insertActivityTask(activityTask.getCategory(),
                activityTask.getRepetition(),
                activityTask.getContent(),
                activityTask.getTimeOfActivity(),
                activityTask.getSubActivities(),
                activityTask.getPriority())) {
            usedTasks.add(activityTask);
            return true;
        }
        return false;
    }


    static public Boolean removeActivityTask(ActivityTask activityTask) {
        if (db.deleteActivityTask(activityTask.getActivityTaskID())) {
            usedTasks.remove(activityTask);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Boolean editActivityTask(ActivityTask activityTask) {
        int taskID = activityTask.getActivityTaskID();
        if (db.updateActivityTask(activityTask, taskID)) {
            usedTasks.remove(activityTask);//ambiguous
            usedTasks.add(activityTask);
            return true;
        }
        return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)//TODO: add to book
    static public ArrayList<ActivityTask> findExactActivityTask(int activityTaskID,int priority, LocalDateTime localDateTime, Repetition repetition, MasloCategorys masloCategorys, String content) {
        ArrayList<ActivityTask> returned = new ArrayList<>();
        for (ActivityTask task : usedTasks) {
            if (priority == task.getPriority() && localDateTime == task.getTimeOfActivity()
                    && repetition == task.getRepetition() && masloCategorys == task.getCategory()
                    && content == task.getContent()) {
                returned.add(task);
            }
        }

        if (returned.isEmpty()) {
            returned = db.queryForExactActivityTask(activityTaskID, priority, localDateTime, content, repetition, masloCategorys);
            usedTasks.addAll(returned);
        }

        return returned;
    }

    //TODO:add to book
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<ActivityTask> getCloseActivities() { //gets the activities of the next 24 hours
        final LocalDateTime timeNdateNow=LocalDateTime.now();
        final LocalDateTime upToNextDay=LocalDateTime.now().plusDays(1);
        ArrayList<ActivityTask> returned = db.queryForAllActivityTasks();
        returned.removeIf(activityTask -> activityTask.getPriority() <= 0 ||
                            (activityTask.getTimeOfActivity().isBefore(timeNdateNow) ||
                            activityTask.getTimeOfActivity().isAfter(upToNextDay)));//delete if priority is less or equal to 0 and the time is before now and more then 24 hours
        return returned;
    }
    public static ArrayList<ActivityTask> getTodaysActivities(String theDay){//TODO:add to book
        return db.queryForToDaysActivities(theDay);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getUserPersonalScore() {
        return userPersonalScore;
    }

    public static boolean removeSubActivity(SubActivity subActivity){
        return db.deleteSubActivity(subActivity);
    }
    //endregion ActivityTask



}
