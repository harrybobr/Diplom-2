package user;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

public class CreateUserTest {
  private UserStep userStep;
  private User user;
  private String accessToken;

  @Before
  public void setUp() {
    user = UserGen.getRandomUser();
    userStep = new UserStep();
  }

  @After
  public void tearDown() {
    if (accessToken != null && !accessToken.isEmpty()) {
      userStep.deleteUser(accessToken);
    }
  }

  @Test
  @DisplayName("Создание уникального пользователя")
  @Description("Проверка создания уникального пользователя")
  public void createUserTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");
    responseReg.assertThat().statusCode(SC_OK).body("success", is(true));
  }

  @Test
  @DisplayName("Создание не уникального пользователя")
  @Description("Проверка создания не уникального пользователя")
  public void createAlreadyExistsUserTest() {
    ValidatableResponse responseRegFirstUser = userStep.regUser(user);
    accessToken = responseRegFirstUser.extract().path("accessToken");

    ValidatableResponse responseRegSecondUser = userStep.regUser(user);
    responseRegSecondUser.assertThat().statusCode(SC_FORBIDDEN)
        .body("success", is(false))
        .body("message", is("User already exists"));
  }

  @Test
  @DisplayName("Создание пользователя с пустым именем")
  @Description("Проверка создания пользователя с пустым именем")
  public void createUserWithoutNameTest() {
    user.setName("");
    ValidatableResponse responseReg = userStep.regUser(user);
    responseReg.assertThat().statusCode(SC_FORBIDDEN)
        .body("success", is(false))
        .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя с пустым email")
  @Description("Проверка создания пользователя с пустым email")
  public void createUserWithoutEmailTest() {
    user.setEmail("");
    ValidatableResponse responseReg = userStep.regUser(user);
    responseReg.assertThat().statusCode(SC_FORBIDDEN)
        .body("success", is(false))
        .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя с пустым паролем")
  @Description("Проверка создания пользователя с пустым паролем")
  public void createUserWithoutPasswordTest() {
    user.setPassword("");
    ValidatableResponse responseReg = userStep.regUser(user);
    responseReg.assertThat().statusCode(SC_FORBIDDEN)
        .body("success", is(false))
        .body("message", is("Email, password and name are required fields"));
  }
}
