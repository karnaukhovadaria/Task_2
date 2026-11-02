package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.CreateOrderRequest;
import pojo.LoginUserRequest;
import pojo.LoginUserResponse;

import java.util.List;

import static endpoints.Endpoints.INGREDIENTS;
import static endpoints.Endpoints.ORDERS;
import static io.restassured.RestAssured.requestSpecification;
import static steps.UserSteps.requestSpecification;

public class OrderSteps {

@Step("Создание заказа без авторизции")
    public ValidatableResponse orderCreate(CreateOrderRequest createOrderRequest) {
    return requestSpecification()
            .body(createOrderRequest)
            .post(ORDERS)
            .then();

}
@Step("Создание заказа с авторизацией")
    public ValidatableResponse orderCreateAfterLogin(LoginUserRequest loginUserRequest, CreateOrderRequest createOrderRequest) {
    UserSteps userSteps = new UserSteps();
    Response response = userSteps.userLogin(loginUserRequest)
            .extract().response();
    LoginUserResponse loginUserResponse = response.as(LoginUserResponse.class);
    String accessToken = loginUserResponse.getAccessToken();
    return requestSpecification()
            .header("Authorization", accessToken)
            .body(createOrderRequest)
            .post(ORDERS)
            .then();
}
@Step("Получить заказы без авторизации")
    public ValidatableResponse orderList() {
    return requestSpecification()
            .get(ORDERS)
            .then();
}
@Step("Получить заказы после авторизации")
    public ValidatableResponse orderListAfterLogin(LoginUserRequest loginUserRequest) {
    UserSteps userSteps = new UserSteps();
    Response response = userSteps.userLogin(loginUserRequest)
            .extract().response();
    LoginUserResponse loginUserResponse = response.as(LoginUserResponse.class);
    String accessToken = loginUserResponse.getAccessToken();
    return requestSpecification()
            .header("Authorization", accessToken)
            .get(ORDERS)
            .then();
}
    @Step("Получить список валидных ID ингредиентов")
    public List<String> getIngredientIds() {
        return requestSpecification()
                .get(INGREDIENTS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data._id");
    }
}
