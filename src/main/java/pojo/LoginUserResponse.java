package pojo;

public class LoginUserResponse {

private boolean success;
private String accessToken;
private String refreshToken;

public LoginUserResponse(boolean success, String accessToken, String refreshToken) {
    this.success = success;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
}
public LoginUserResponse() {}
public boolean isSuccess() {
    return success;
}
public void setSuccess(boolean success) {
    this.success = success;
}
public String getAccessToken() {
    return accessToken;
}
public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
}
public String getRefreshToken() {
    return refreshToken;
}
public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
}

}
