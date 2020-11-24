package module;

import androidx.annotation.Nullable;

import com.example.rems.DataBaseHelper;

public class SubActivity {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj); //TODO:check if sub is same
    }

    public Integer getActivityTaskID() {
        this.ActivityTaskID = DataBaseHelper.getInstance(null).getMaxIdOfActivityTask();
        return ActivityTaskID;
    }
}
