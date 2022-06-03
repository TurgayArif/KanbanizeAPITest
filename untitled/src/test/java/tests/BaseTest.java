package tests;

import api.HTTPClient;
import api.TaskApi;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected TaskApi taskApi = new TaskApi();

    @BeforeAll
    public static void beforeAll() {
        HTTPClient.setupRequestDefaults();
    }
}
