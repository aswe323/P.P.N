package module;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ActivityTasksUsed {
    static private ArrayList<ActivityTask> usedTasks = new ArrayList<>();

    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context


    //region methods ActivityTask
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
            usedTasks.remove(activityTask);
            usedTasks.add(activityTask);
            return true;
        }
        return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)//TODO: add to book
    static public ArrayList<ActivityTask> findExactActivityTask(int priority, LocalDateTime localDateTime, Repetition repetition, MasloCategorys masloCategorys, String content) {
        ArrayList<ActivityTask> returned = new ArrayList<>();
        for (ActivityTask task : usedTasks) {
            if (priority == task.getPriority() && localDateTime == task.getTimeOfActivity()
                    && repetition == task.getRepetition() && masloCategorys == task.getCategory()
                    && content == task.getContent()) {
                returned.add(task);
            }
        }

        if (returned == null) {
            returned = db.queryForExactActivityTask(0, priority, localDateTime, content, repetition, masloCategorys);
            usedTasks.addAll(returned);
        }

        return returned;

    }

    //TODO:add to book,change it to get the next 10 or so
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<ActivityTask> getCloseActivities(){
        return db.queryForAllActivityTasks();
    }

    //endregion ActivityTask

    //region methods WordPriority

    //endregion WordPriority

}
