import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import steps.UserSteps;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private String email;
    private String password;
    private String name;

    private final UserSteps userSteps = new UserSteps();


    private CreateEditUserRequest createdUser;

    @BeforeEach
    public void setUp() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        this.email = "karnadasha+" + unique + "@yandex.ru";
        this.password = "qwerty12";
        this.name = "Дарья";
        this.createdUser = null;
    }

    @AfterEach
    public void tearDown() {
        // Удаляем только если пользователь реально был создан в тесте
        if (createdUser != null) {
            LoginUserRequest loginUserRequest = new LoginUserRequest(createdUser.getEmail(), createdUser.getPassword());
            userSteps.userDeleteAfterLogin(loginUserRequest);
            createdUser = null;
        }
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    public void createNewUser() {
        CreateEditUserRequest request = new CreateEditUserRequest(email, password, name);
        userSteps.userCreate(request)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);


        createdUser = request;
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {

        CreateEditUserRequest request = new CreateEditUserRequest(email, password, name);
        userSteps.userCreate(request).statusCode(200);
        createdUser = request;

        // Пытаемся создать дубликат
        userSteps.userCreate(request)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создать пользователя без почты")
    public void createUserWithoutEmail() {
        CreateEditUserRequest request = new CreateEditUserRequest(null, password, name);
        userSteps.userCreate(request)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("Создать пользователя без пароля")
    public void createUserWithoutPassword() {
        CreateEditUserRequest request = new CreateEditUserRequest(email, null, name);
        userSteps.userCreate(request)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создать пользователя без имени")
    public void createUserWithoutName() {
        CreateEditUserRequest request = new CreateEditUserRequest(email, password, null);
        userSteps.userCreate(request)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
}