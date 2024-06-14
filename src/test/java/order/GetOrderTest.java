package order;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import user.UserGen;
import user.UserStep;

public class GetOrderTest {
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
    order = OrderGen.getListOrder();
  }

  @After
  public void tearDown() {
    if (accessToken != null && !accessToken.isEmpty()) {
      userStep.deleteUser(accessToken);
    }
  }

  @Test
  @DisplayName("Получение заказов авторизованного пользователя")
  @Description("Проверка получения списка заказов авторизованного пользователя")
  public void getOrderWithLoginTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");
    userStep.loginUser(user);
    orderStep.createOrder(order, accessToken);

    ValidatableResponse responseOrderUser = orderStep.getClientOrder(accessToken);
    responseOrderUser.assertThat().statusCode(SC_OK).body("success", is(true));
  }

  @Test
  @DisplayName("Получение заказов неавторизованного пользователя")
  @Description("Проверка получения списка заказов неавторизованного пользователя")
  public void getOrderWithoutLoginTest() {
    accessToken = "";
    ValidatableResponse getClientOrder = orderStep.getClientOrder(accessToken);

    getClientOrder.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false))
        .and().body("message", is("You should be authorised"));
  }
}
