import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {

    public static String email = "karnadasha@yandex.ru";
    public static String password = "qwerty12";
    public static String name = "Дарья";

    @AfterEach
    public void deleteUser() {
        UserSteps userSteps = new UserSteps();
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        userSteps.userDeleteAfterLogin(loginUserRequest);
    }

    @Test
    @DisplayName("логин под существующим пользователем")
    public void userLogin() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userLogin(loginUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("логин с неверным логином")
    public void userLoginWrongEmail() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest userWrongLoginRequest = new LoginUserRequest("wrongEmail", password);
        LoginUserRequest userRightLoginRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userLogin(userWrongLoginRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("логин с неверным паролем")
    public void userLoginWrongPassword() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest userWrongLoginRequest = new LoginUserRequest(email, "wrongPassword");
        LoginUserRequest userRightLoginRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userLogin(userWrongLoginRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
