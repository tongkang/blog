package blog.service;


import blog.entity.User;
import blog.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    //因为HashMap不是安全，不同步的线程不安全，所以用ConcurrentHashMap
//    private Map<String, User> users = new ConcurrentHashMap<>();


    private UserMapper userMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }


    public void save(String username, String password) {
        userMapper.save(username, bCryptPasswordEncoder.encode(password));
    }


    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }


    //原本的userDetailsService，现在只要实现这个接口方法就行了
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }

        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(), Collections.emptyList());
    }
}
