import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class OrderListTest {

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
    @DisplayName("получить список заказов без авторизации")
    public void orderListWithoutAuthorization() {
        skipDeleteUser = true;
        OrderSteps orderSteps = new OrderSteps();
        orderSteps.orderList()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("получить список заказов с авторизацией")
    public void orderListAuthorized() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(createEditUserRequest);
        orderSteps.orderListAfterLogin(loginUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("orders", instanceOf(List.class))
                .and()
                .statusCode(200);
    }

}
