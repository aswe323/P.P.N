package module;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ActivityTasksUsed {
    static private ArrayList<ActivityTask> usedTasks = new ArrayList<>();

    static private DataBaseHelper db = DataBaseHelper.getInstance(null);


    //region methods
    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Boolean addActivityTask(ActivityTask activityTask) {
        if (!db.queryForExactActivityTask(activityTask).isEmpty()
                && db.insertActivityTask(activityTask.getCategory(), activityTask.getRepetition(), activityTask.getContent(),
                activityTask.getTimeOfActivity(), activityTask.getSubActivities(), activityTask.getPriority())) {
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


    //endregion


}
