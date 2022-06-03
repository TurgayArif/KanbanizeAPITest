package tests;

import beans.Task;
import helpers.ReadAndWriteResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TaskApiTest extends BaseTest {
    ReadAndWriteResponse rw = new ReadAndWriteResponse();
    private String createTaskResponseFileName = "createTaskResponse.xml";
    private String deleteTaskResponseFileName = "deleteTaskResponse.xml";
    private String moveTaskResponseFileName = "moveTaskResponse.xml";
    private String taskDetailsResponseFileName = "taskDetailsResponse.xml";

    enum Columns {
        BACKLOG("Backlog"),
        REQUESTED("Requested"),
        IN_PROGRESS("In Progress"),
        DONE("Done"),
        TEMP_ARCHIVE("Temp Archive"),
        DEFAULT_SWIMLANE("Default Swimlane");

        private String columnName;

        Columns(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return this.columnName;
        }
    }

    @Test
    @DisplayName("1. Can create new task")
    public void canCreateNewTask() {
        Task taskBody = Task.builder()
                .boardid("1")
                .title("test title")
                .column(Columns.BACKLOG.getColumnName())
                .description("test desc")
                .assignee("test assignee")
                .tags("test1 test2")
                .position("0")
                .build();
        Task taskBody1 = Task.builder()
                .boardid("1")
                .title("test title1")
                .column(Columns.REQUESTED.getColumnName())
                .description("test desc1")
                .build();

        taskApi.createNewTask(taskBody);
        taskApi.createNewTask(taskBody1);

        String createNewTaskResponse = taskApi.getTaskDetails(Task.builder().boardid("1").boardid("1").build()).getBody().prettyPrint();
        rw.writeResponseToFile(createTaskResponseFileName, createNewTaskResponse);

        canGetTaskDetails();

        String expected = rw.getLastAttributeTextContent(taskDetailsResponseFileName, "taskid");
        String actual = rw.getLastAttributeTextContent(createTaskResponseFileName, "taskid");
        Assertions.assertEquals(expected, actual);

        String expectedPosition = rw.getLastAttributeTextContent(createTaskResponseFileName, "position");
        String actualPosition = rw.getLastAttributeTextContent(taskDetailsResponseFileName, "position");
        Assertions.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("2. Can move task to another position")
    public void canMoveTaskToAnotherPosition() {
        Task taskBody = Task.builder()
                .boardid("1")
                .taskid(rw.getLastAttributeTextContent(taskDetailsResponseFileName, "taskid"))
                .position("0")
                .build();
        taskApi.moveTask(taskBody);
        canGetTaskDetails();
        String actualPosition = rw.getLastAttributeTextContent(taskDetailsResponseFileName, "position");
        Assertions.assertEquals("0", actualPosition);
    }


    @Test
    @DisplayName("3. Can move task to column")
    public void canMoveTaskToColumn() {
        if (!rw.getLastAttributeTextContent(taskDetailsResponseFileName, "item").isEmpty()) {
            Task taskBody = Task.builder()
                    .boardid("1")
                    .taskid(rw.getLastAttributeTextContent(taskDetailsResponseFileName, "taskid"))
                    .column(Columns.IN_PROGRESS.getColumnName())
                    .build();
            String moveTask = taskApi.moveTask(taskBody).getBody().prettyPrint();
            rw.writeResponseToFile(moveTaskResponseFileName, moveTask);
        }
        if (rw.getLastAttributeTextContent(moveTaskResponseFileName, "item").contains("1")) {
            Assertions.assertEquals("1", rw.getLastAttributeTextContent(moveTaskResponseFileName, "item"));
        }
        canGetTaskDetails();
        if (!rw.getLastAttributeTextContent(taskDetailsResponseFileName, "columnname").isEmpty()) {
            Assertions.assertEquals(Columns.IN_PROGRESS.getColumnName(), rw.getLastAttributeTextContent(taskDetailsResponseFileName, "columnname"));
        }
    }

    @Test
    @DisplayName("4. Can get task details")
    public void canGetTaskDetails() {
        Response response = taskApi.getTaskDetails(Task.builder().boardid("1").boardid("1").build());
        String s = response.getBody().prettyPrint();
        rw.writeResponseToFile(taskDetailsResponseFileName, s);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("5. Can delete task")
    public void canDeleteTask() {
        rw.writeResponseToFile(taskDetailsResponseFileName, taskApi.getTaskDetails(Task.builder().boardid("1").boardid("1").build()).getBody().prettyPrint());
        Task taskBody = Task.builder()
                .boardid("1")
                .taskid(rw.getLastAttributeTextContent(taskDetailsResponseFileName, "taskid"))
                .build();
        String deleteTask = taskApi.deleteTask(taskBody).getBody().prettyPrint();
        rw.writeResponseToFile(deleteTaskResponseFileName, deleteTask);
        if (rw.getLastAttributeTextContent(deleteTaskResponseFileName, "item").contains("taskid")) {
            System.out.println("There are no tasks to be deleted.");
        } else {
            Assertions.assertEquals("1", rw.getLastAttributeTextContent(deleteTaskResponseFileName, "item"));
        }
    }

    @Test
    @DisplayName("6. Can delete all tasks")
    public void canDeleteAllTasks() {
        rw.writeResponseToFile(taskDetailsResponseFileName, taskApi.getTaskDetails(Task.builder().boardid("1").boardid("1").build()).getBody().prettyPrint());
        for (int i = 0; i < taskDetailsResponseFileName.length(); i++) {
            if (!rw.getLastAttributeTextContent(taskDetailsResponseFileName, "item").isEmpty()) {
                canDeleteTask();
            } else {
                break;
            }
        }
        rw.deleteFile(createTaskResponseFileName);
        rw.deleteFile(deleteTaskResponseFileName);
        rw.deleteFile(moveTaskResponseFileName);
        rw.deleteFile(taskDetailsResponseFileName);
    }
}
