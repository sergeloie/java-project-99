package hexlet.code.app.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private String title;
    private String content;
    private String status;
    private List<String> labels;

    @JsonProperty("assignee_id")
    private Long assigneeId;

}