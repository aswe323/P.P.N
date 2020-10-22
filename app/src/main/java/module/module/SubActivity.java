package module;

public class SubActivity {
    private Integer subActivityID;
    private Integer ActivityTaskID;
    private String content;

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
        return ActivityTaskID;
    }
}
