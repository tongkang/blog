package blog.entity;

public class LoginResult extends Result<User> {
    boolean isLogin;

    protected LoginResult(ResultStatus status, String msg, User data, boolean isLogin) {
        super(status, msg, data);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg, boolean isLogin) {
        return new LoginResult(ResultStatus.Ok, msg, null, isLogin);
    }

    public static LoginResult success(User user) {
        return new LoginResult(ResultStatus.Ok, null, user, true);
    }

    public static LoginResult failure(String msg) {
        return new LoginResult(ResultStatus.FAIL, msg, null, false);
    }

    public static LoginResult success(String msg, User user) {
        return new LoginResult(ResultStatus.Ok, msg, user, true);
    }

    public boolean getIsLogin() {
        return isLogin;
    }
}
