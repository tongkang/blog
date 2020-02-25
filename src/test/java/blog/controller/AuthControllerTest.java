package blog.controller;

import blog.service.AuthService;
import blog.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        AuthService authService = new AuthService(userService);
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager, authService)).build();
    }

    /*
     *@BeforeEach
     * 每测试一个类就要创建一个AuthController实例，所以用BeforeEach来循环
     */

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
//                System.out.printf(result.getResponse().getContentAsString());
                Assertions.assertTrue(result.getResponse().getContentAsString().contains("ç\u0094¨æ\u0088·æ²¡æ\u009C\u0089ç\u0099»å½\u0095"));
            }
        });
    }

//    @Test
//    void testLogin() throws Exception {
//        //未登录，/auth接口返回未登录状态
//        mvc.perform(get("/auth")).andExpect(status().isOk())
//                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));
//
//        //使用/auth/login登录
//        HashMap<String, String> usernamePassword = new HashMap<>();
//        usernamePassword.put("username", "MyUser");
//        usernamePassword.put("password", "Mypassword");
//
//        Mockito.when(userService.loadUserByUsername("MyUser")).thenReturn(new User("MyUser", bCryptPasswordEncoder.encode("MyPassword"), Collections.emptyList()));
//        Mockito.when(userService.getUserByUsername("MyUser")).thenReturn(new blog.entity.User(123, "MyUser", bCryptPasswordEncoder.encode("MyPassword")));
//
//        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(usernamePassword)))
//                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
//                .andReturn();
//
//        HttpSession session = response.getRequest().getSession();
//
//        // 再次检查/auth的返回值，处于登录状态
//        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
//                .andExpect(result -> {
//                    System.out.println(result.getResponse().getContentAsString());
//                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("MyUser"));
//                });
//    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }
}