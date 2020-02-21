package blog.mapper;

import blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("insert into user(username, encryptedPassword,updated_at,created_at) " +
            "values(#{username}, #{encryptedPassword}, now(), now())")
    void save(@Param("username") String username,@Param("encryptedPassword") String encryptedPassword);
}
