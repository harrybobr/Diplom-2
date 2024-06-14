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

public class ModifyUserDataTest {
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
  @DisplayName("Изменение данных пользователя с авторизацией")
  @Description("Проверка изменения данных пользователя с авторизацией")
  public void modifyUserWithLoginTest() {
    ValidatableResponse responseReg = userStep.regUser(user);
    accessToken = responseReg.extract().path("accessToken");

    User modifyUser = UserGen.getRandomUser();

    ValidatableResponse responseModify = userStep.modifyUser(modifyUser, accessToken);
    responseModify.assertThat().statusCode(SC_OK).body("success", is(true));
  }

  @Test
  @DisplayName("Изменение данных пользователя без авторизации")
  @Description("Проверка изменения данных пользователя без авторизации")
  public void modifyUserWithoutLoginTest() {
    User modifyUser = UserGen.getRandomUser();

    ValidatableResponse responseModify = userStep.modifyUser(modifyUser, "");
    responseModify.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false))
        .and().body("message", is("You should be authorised"));
  }
}
