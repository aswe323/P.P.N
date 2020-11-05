package module;

import com.example.rems.DataBaseHelper;

public class SubActivity {
    private Integer subActivityID;
    private Integer ActivityTaskID;
    private String content;

    //TODO:remove the SubActivityID int to be allocated automaticly
    public SubActivity(Integer subActivityID, Integer activityTaskID, String content) {
        this.subActivityID = subActivityID;
        ActivityTaskID = activityTaskID;
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
