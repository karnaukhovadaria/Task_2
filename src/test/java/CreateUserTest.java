import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

public static String email = "karnadasha@yandex.ru";
public static String password = "qwerty12";
public static String name = "Дарья";

private boolean skipDeleteUser = false;

@AfterEach
    public void deleteUser() {
    if (!skipDeleteUser) {
        UserSteps userSteps = new UserSteps();
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        userSteps.userDeleteAfterLogin(loginUserRequest);
    }
}
@Test
@DisplayName("Создать уникального пользователя")
public void createNewUser() {
    CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
    LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
    UserSteps userSteps = new UserSteps();
    userSteps.userCreate(createEditUserRequest)
            .assertThat().body("success", equalTo(true))
            .and()
            .statusCode(200);
}
@Test
@DisplayName("создать пользователя, который уже зарегистрирован")
public void createDuplicateUser() {
    CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
    LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
    UserSteps userSteps = new UserSteps();
    userSteps.userCreate(createEditUserRequest)
            .assertThat().body("success", equalTo(false))
            .and()
            .statusCode(403);
}
@Test
@DisplayName("создать пользователя без почты")
public void createUserWithoutEmail() {
    skipDeleteUser = true;
    CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(null, password, name);
    UserSteps userSteps = new UserSteps();
    userSteps.userCreate(createEditUserRequest)
            .assertThat().body("success", equalTo(false))
            .and()
            .statusCode(403);
}
@Test
@DisplayName("создать пользователя без пароля")
public void createUserWithoutPassword() {
    skipDeleteUser = true;
    CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, null, name);
    UserSteps userSteps = new UserSteps();
    userSteps.userCreate(createEditUserRequest)
            .assertThat().body("success", equalTo(false))
            .and()
            .statusCode(403);
}
@Test
@DisplayName("создать пользователя без имени")
public void createUserWithoutName() {
    skipDeleteUser = true;
    CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, null);
    UserSteps userSteps = new UserSteps();
    userSteps.userCreate(createEditUserRequest)
            .assertThat().body("success", equalTo(false))
            .and()
            .statusCode(403);
}

}
