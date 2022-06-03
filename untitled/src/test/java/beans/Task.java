package beans;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Task {
    private String boardid;
    private String taskid;
    private String column;
    private String title;
    private String description;
    private String priority;
    private String assignee;
    private String tags;
    private String position;
}
