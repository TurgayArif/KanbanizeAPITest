package api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Defaults;

public class HTTPClient {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static final String apiKeyType = "apikey";

    public static void setupRequestDefaults(){
        RestAssured.baseURI = Defaults.BASE_URL;
        RestAssured.basePath = "/index.php/api/kanbanize/";
        RestAssured.authentication = RestAssured.preemptive().basic(Defaults.EMAIL, Defaults.PASSWORD);
    }

    protected Response get(String url){
        Response response = RestAssured.given()
                .header(apiKeyType, Defaults.API_KEY)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(url);
        response.prettyPrint();
        return response;
    }

    protected Response delete(String url, String body){
        Response response = RestAssured.given()
                .header(apiKeyType, Defaults.API_KEY)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete(url);
        response.prettyPrint();
        return response;
    }

    protected Response post(String url, String body){
        Response response = RestAssured.given()
                .header(apiKeyType, Defaults.API_KEY)
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post(url);
        response.prettyPrint();
        return response;
    }

    protected Response put(String url, String body){
        Response response = RestAssured.given()
                .header(apiKeyType, Defaults.API_KEY)
                .body(body)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .put(url);
        response.prettyPrint();
        return response;
    }

}
