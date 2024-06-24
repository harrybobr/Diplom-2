package user;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.Matchers.is;


import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import pojo.User;
import settings.ApiConstants;
import settings.Spec;

public class UserStep {
  public static final String AUTHORIZATION = "Authorization";

  @Step("Регистрация пользователя POST api/auth/register")
  public ValidatableResponse regUser(User user) {
    return given()
        .spec(Spec.requestSpec())
        .and()
        .body(user)
        .when()
        .post(ApiConstants.USER_REGISTER)
        .then();
  }

  @Step("Логин пользователя POST api/auth/login")
  public ValidatableResponse loginUser(User user) {
    return given()
        .spec(Spec.requestSpec())
        .and()
        .body(user)
        .when()
        .post(ApiConstants.USER_LOGIN)
        .then();
  }

  @Step("Удаление пользователя DELETE api/auth/user")
  public ValidatableResponse deleteUser(String accessToken) {
    return given()
        .spec(Spec.requestSpec())
        .headers(AUTHORIZATION, accessToken)
        .delete(ApiConstants.USER_DELETE)
        .then()
        .statusCode(SC_ACCEPTED)
        .and()
        .body("message", is("User successfully removed"));
  }

  @Step("Изменение данных пользователя PATCH api/auth/user")
  public ValidatableResponse modifyUser(User user, String accessToken) {
    return given()
        .spec(Spec.requestSpec())
        .headers(AUTHORIZATION, accessToken)
        .contentType(ContentType.JSON)
        .and()
        .body(user)
        .when()
        .patch(ApiConstants.USER_MODIFY)
        .then();
  }
}
