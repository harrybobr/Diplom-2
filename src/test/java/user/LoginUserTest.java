package user;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

public class LoginUserTest {
  private UserStep userStep;
  private User user;
  private String accessToken;

  @Before
  public void setUp() {
    user = UserGen.getRandomUser();
    userStep = new UserStep();

    ValidatableResponse responseReg = userStep.regUser(user);    // вынес в Before, так как используется
    accessToken = responseReg.extract().path("accessToken");  // в каждом тесте
  }

  @After
  public void tearDown() {
    if (accessToken != null && !accessToken.isEmpty()) {
      userStep.deleteUser(accessToken);
    }
  }

  @Test
  @DisplayName("Логин под существующим пользователем")
  @Description("Проверка логина под существующим пользователем")
  public void loginUser() {
    ValidatableResponse responseLogin = userStep.loginUser(user);
    responseLogin.assertThat().statusCode(SC_OK).body("success", is(true));
  }

  @Test
  @DisplayName("Логин с неверным email")
  @Description("Проверка логина с неверным email")
  public void loginUserWithWrongEmail() {
    user.setEmail("wrongEmail@example.com");
    ValidatableResponse responseLogin = userStep.loginUser(user);
    responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
        .body("success", is(false))
        .body("message", is("email or password are incorrect"));
  }

  @Test
  @DisplayName("Логин с неверным паролем")
  @Description("Проверка логина с неверным паролем")
  public void loginUserWithWrongPass() {
    user.setPassword("wrongPassword");
    ValidatableResponse responseLogin = userStep.loginUser(user);
    responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
        .body("success", is(false))
        .body("message", is("email or password are incorrect"));
  }
}
