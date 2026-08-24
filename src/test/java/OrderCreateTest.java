import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.CreateEditUserRequest;
import pojo.CreateOrderRequest;
import pojo.LoginUserRequest;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class OrderCreateTest {

    public static String email = "karnadasha@yandex.ru";
    public static String password = "qwerty12";
    public static String name = "Дарья";
    public static List<String> ingredients = new ArrayList<>();

    private boolean skipDeleteUser = false;

    @AfterEach
    public void deleteUser() {
        UserSteps userSteps = new UserSteps();
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        userSteps.userDeleteAfterLogin(loginUserRequest);
    }
    @AfterEach
    public void ingredientsClean() {
        if (!skipDeleteUser) {
            ingredients.clear();
        }
    }

    @Test
    @DisplayName("создание заказа с авторизацией")
    public void orderCreateAuthorized() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        ingredients.add("691577430cc94f001a65b859");
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(createEditUserRequest);
        orderSteps.orderCreateAfterLogin(loginUserRequest, createOrderRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.owner.email", equalTo(email))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("создание заказа без авторизации")
    public void orderCreateWithoutAuthorization() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        ingredients.add("691577430cc94f001a65b859");
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(createEditUserRequest);
        orderSteps.orderCreate(createOrderRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.number", isA(Integer.class))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("создать заказ без ингредиентов и с авторизацией")
    public void orderCreateAuthorizedWithoutIngredients() {
        skipDeleteUser = true;
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(createEditUserRequest);
        orderSteps.orderCreateAfterLogin(loginUserRequest, createOrderRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(400);
    }
    @Test
    @DisplayName("создать заказ с авторизацией и неверным ингредиентом")
    public void orderCreateAuthorizedWrongIngredient() {
        CreateEditUserRequest createEditUserRequest = new CreateEditUserRequest(email, password, name);
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        ingredients.add("691577430cc94f001a65b859");
        ingredients.add("wrongIngredient");
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(createEditUserRequest);
        orderSteps.orderCreateAfterLogin(loginUserRequest, createOrderRequest)
                .assertThat().statusCode(500);
    }
}
