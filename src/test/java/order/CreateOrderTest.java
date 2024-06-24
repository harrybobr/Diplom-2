package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import pojo.Order;
import pojo.User;
import user.UserGen;
import user.UserStep;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

public class CreateOrderTest {

  private UserStep userStep;
  private User user;
  private OrderStep orderStep;
  private Order order;
  private String accessToken;

  @Before
  public void setUp() {
    userStep = new UserStep();
    orderStep = new OrderStep();
    user = UserGen.getRandomUser();
    order = OrderGen.getListOrder();  // создаем заказ с ингредиентами
  }

  @After
  public void tearDown() {
    if (accessToken != null && !accessToken.isEmpty()) {
      userStep.deleteUser(accessToken);
    }
  }

  @Test
  @DisplayName("Создание заказа с авторизацией")
  @Description("Проверка создания заказа с авторизацией")
  public void createOrderWithLoginTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");
    userStep.loginUser(user);
    ValidatableResponse responseCreateOrder = orderStep.createOrder(order, accessToken);
    responseCreateOrder.assertThat().statusCode(SC_OK).body("success", is(true));
  }

  @Test  //тест не проходит, возвращает код 200
  @DisplayName("Создание заказа без авторизации")
  @Description("Проверка создания заказа без авторизации")
  public void createOrderWithoutLoginTest() {
    accessToken = "";
    ValidatableResponse responseCreateOrder = orderStep.createOrder(order, accessToken);
    responseCreateOrder.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false));
  }

  @Test
  @DisplayName("Создание заказа без ингредиентов")
  @Description("Проверка создания заказа без ингредиентов")
  public void createOrderWithoutIngredientsTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");
    userStep.loginUser(user);

    order.setIngredients(new ArrayList<>());

    ValidatableResponse responseCreateOrder = orderStep.createOrder(order, accessToken);
    responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST)
        .body("success", is(false))
        .and().body("message", is("Ingredient ids must be provided"));
  }

  @Test  // в документации ошибка - требует возвращать код 500 Internal Server Error
  @DisplayName("Создание заказа с неверным хешем ингредиента")
  @Description("Проверка создания заказа с неверным хешем ингредиента")
  public void createOrderWithWrongIngredientTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");
    userStep.loginUser(user);

    List<String> wrongIngredient = new ArrayList<>();
    wrongIngredient.add("609646e4dc916e00276b2870");

    order.setIngredients(wrongIngredient);

    ValidatableResponse responseCreateOrder = orderStep.createOrder(order, accessToken);
    responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST)
        .body("success", is(false))
        .and().body("message", is("One or more ids provided are incorrect"));
  }
}
