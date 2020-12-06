package module;

import androidx.annotation.Nullable;

import com.example.rems.DataBaseHelper;

import java.util.ArrayList;

public class SubActivity {
    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context

    private Integer subActivityID;
    private Integer ActivityTaskID;
    private String content;

    //TODO:remove the SubActivityID int to be allocated automaticly
    public SubActivity(Integer subActivityID, Integer activityTaskID, String content) {
        this.subActivityID = subActivityID;
        this.ActivityTaskID = activityTaskID;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Integer getSubActivityID() {
        return subActivityID;
    }

    public Integer getActivityTaskID() {
        this.ActivityTaskID = DataBaseHelper.getInstance(null).getMaxIdOfActivityTask();
        return ActivityTaskID;
    }

}
