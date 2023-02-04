package api;

import beans.Task;
import io.restassured.response.Response;

public class TaskApi extends HTTPClient {
    private static final String CREATE_NEW_TASK = "create_new_task/";
    private static final String DELETE_TASK = "delete_task/";
    private static final String GET_TASK_DETAILS = "get_all_tasks/";
    private static final String MOVE_TASK = "move_task/";

    public Response createNewTask(Task body) {
        return post(CREATE_NEW_TASK, GSON.toJson(body));
    }

    public Response deleteTask(Task body) {
        return post(DELETE_TASK, GSON.toJson(body));
    }

    public Response getTaskDetails(Task body) {
        return post(GET_TASK_DETAILS, GSON.toJson(body));
    }

    public Response moveTask(Task body) {
        return post(MOVE_TASK, GSON.toJson(body));
    }
}
