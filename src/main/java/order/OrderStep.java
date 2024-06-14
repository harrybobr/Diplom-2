package order;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Order;
import settings.ApiConstants;
import settings.Spec;

public class OrderStep {
  public static final String AUTHORIZATION = "Authorization";

  @Step("Создание заказа POST api/orders")
  public ValidatableResponse createOrder(Order order, String accessToken) {
    return given()
        .spec(Spec.requestSpec())
        .headers(AUTHORIZATION, accessToken)
        .and()
        .body(order)
        .when()
        .post(ApiConstants.CREATE_ORDER)
        .then();
  }

  @Step("Получение всего списка ингредиентов GET api/ingredients/")
  public static ValidatableResponse getAllIngredients() {
    return given()
        .spec(Spec.requestSpec())
        .get(ApiConstants.INGREDIENTS)
        .then();
  }

  @Step("Получение заказа клиента GET api/orders")
  public ValidatableResponse getClientOrder(String accessToken) {
    return given()
        .spec(Spec.requestSpec())
        .headers(AUTHORIZATION, accessToken)
        .get(ApiConstants.VIEW_ORDER)
        .then();
  }
}
