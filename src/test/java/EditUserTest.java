import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class EditUserTest {

    public static String email = "karnadasha@yandex.ru";
    public static String password = "qwerty12";
    public static String name = "Дарья";

    public static String newEmail = "karandash@yandex.ru";
    public static String newPassword = "asdf12";
    public static String newName = "Наталья";

    @AfterEach
    public void deleteUser() {
        UserSteps userSteps = new UserSteps();
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        userSteps.userDeleteAfterLogin(loginUserRequest);
    }

    @Test
    @DisplayName("Изменить почту с авторизацией")
    public void userEditEmailAuthorized() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(newEmail, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        LoginUserRequest loginNewUserRequest = new LoginUserRequest(newEmail, password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(createEditUserRequest);
        userSteps.userEditAfterLogin(loginUserRequest, editUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo(newEmail))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменить почту без авторизации")
    public void userEditEmailWithoutAuthorization() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(newEmail, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userEdit(editUserRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("Изменить пароль с авторизацией")
    public void userEditPasswordAuthorized() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(email, newPassword, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        LoginUserRequest loginNewUserRequest = new LoginUserRequest(email, newPassword);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);

        userSteps.userEditAfterLogin(loginUserRequest, editUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        userSteps.userLogin(loginNewUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменить пароль без авторизации")
    public void userEditPasswordWithoutAuthorization() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(email, newPassword, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userEdit(editUserRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("Изменить имя с авторизацией")
    public void userEditNameAuthorized() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(email, password, newName);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userEditAfterLogin(loginUserRequest, editUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo(newName))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменить имя без авторизации")
    public void userEditNameWithoutAuthorization() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        CreateEditUserRequest editUserRequest = new CreateEditUserRequest(email, password, newName);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(createEditUserRequest);
        userSteps.userEdit(editUserRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

}
