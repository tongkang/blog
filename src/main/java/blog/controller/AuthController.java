package blog.controller;

import blog.entity.LoginResult;
import blog.entity.Result;
import blog.entity.User;
import blog.service.AuthService;
import blog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private AuthService authService;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager, AuthService authService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }


    @RequestMapping("/auth")
    @ResponseBody
    public Object auth() {
        return authService.getCurrentUser()
                .map(LoginResult::success)
                .orElse(LoginResult.success("用户没有登录！", false));
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (username == null || password == null) {
            return LoginResult.failure("username/password == null");
        }

        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("invalid username");
        }

        if (password.length() < 6 || password.length() > 16) {
            return LoginResult.failure("invalid password");
        }

        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("user already exists");
        }

        return LoginResult.success("注册成功!", userService.getUserByUsername(username));

    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> usernameAndPassword, HttpServletRequest request) {
        if (request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")) {
            return "死爬虫去死吧";
        }
        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();

        //取出用户名来比对密码对不对
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        //鉴权，正确会把相应的权限放入进去
        try {
            authenticationManager.authenticate(token);
            //把用户信息保存在一个地方
            //  Cookie
            SecurityContextHolder.getContext().setAuthentication(token);

            return LoginResult.success("登录成功", userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        SecurityContextHolder.clearContext();
        return authService.getCurrentUser()
                .map(user -> LoginResult.success("success", false))
                .orElse(LoginResult.failure("用户没有登录"));

    }

}
