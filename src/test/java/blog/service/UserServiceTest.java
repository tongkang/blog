package blog.service;

import blog.entity.User;
import blog.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    BCryptPasswordEncoder mockEncoder;

    @Mock
    UserMapper mockMapper;

    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {

        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodePassword");

        userService.save("myUser", "myPassword");

        Mockito.verify(mockMapper).save("myUser", "myEncodePassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("myUser");

        Mockito.verify(mockMapper).findUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        Mockito.when(mockMapper.findUserByUsername("myUser"))
                .thenReturn(new User(123, "myUser", "myEncodePassword"));

        UserDetails userDetails = userService.loadUserByUsername("myUser");

        Assertions.assertEquals("myUser", userDetails.getUsername());
        Assertions.assertEquals("myEncodePassword", userDetails.getPassword());
    }
}