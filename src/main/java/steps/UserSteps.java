package steps;

import endpoints.Endpoints;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.CreateEditUserRequest;
import pojo.LoginUserRequest;
import pojo.LoginUserResponse;

import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserSteps {
    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(Endpoints.BASE_URL);
    }

    @Step("Создание нового пользователя")
    public ValidatableResponse userCreate(CreateEditUserRequest createEditUserRequest) {
        return requestSpecification()
                .body(createEditUserRequest)
                .post(USER_CREATE)
                .then();
    }
    @Step("Авторизовать пользователя")
    public ValidatableResponse userLogin(LoginUserRequest loginUserRequest) {
        return requestSpecification()
                .body(loginUserRequest)
                .post(USER_LOGIN)
                .then();
    }
    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse userEdit(CreateEditUserRequest createEditUserRequest) {
        return requestSpecification()
                .body(createEditUserRequest)
                .patch(USER)
                .then();
    }
    @Step("Изменить данные пользователя после авторизации")
    public ValidatableResponse userEditAfterLogin(LoginUserRequest loginUserRequest, CreateEditUserRequest createEditUserRequest) {
        Response response = userLogin(loginUserRequest)
                .extract().response();
        LoginUserResponse loginUserResponse = response.as(LoginUserResponse.class);
        String accessToken = loginUserResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .body(createEditUserRequest)
                .patch(USER)
                .then();
    }
    @Step("Удалить пользователя без авторизации")
    public ValidatableResponse userDelete(String accessToken) {
        return requestSpecification()
                .header("Authorization", accessToken)
                .delete(USER)
                .then();
    }
    @Step("Удалить пользователя после авторизации")
    public ValidatableResponse userDeleteAfterLogin(LoginUserRequest loginUserRequest) {
        Response response = userLogin(loginUserRequest)
                .extract().response();
        LoginUserResponse loginUserResponse = response.as(LoginUserResponse.class);
        String accessToken = loginUserResponse.getAccessToken();
        return userDelete(accessToken);
    }
}
