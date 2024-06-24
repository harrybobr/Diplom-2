package settings;

import static settings.ApiConstants.BURGER_URL;
import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Spec {
  public static RequestSpecification requestSpec(){
    return given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .baseUri(BURGER_URL);
  }
}
