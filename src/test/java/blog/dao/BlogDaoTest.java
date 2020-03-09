package blog.dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class BlogDaoTest {

    /**
     * 若使用下面这种注入，这个bean是在jvm生命周期中
     *
     * @Autowired private final SqlSession sqlSession;
     * <p>
     * 使用下面这种方式注入对于测试来说更好
     * private final SqlSession sqlSession;
     * @Inject public BlogDao(SqlSession sqlSession) {
     * this.sqlSession = sqlSession;
     * }
     */

    private SqlSession mockSession = Mockito.mock(SqlSession.class);
    BlogDao blogDao = new BlogDao(mockSession);

    @Test
    public void testGetBlogs() {
        /**
         *  当我传递page=2，pageSize=10,userId=3
         *  数据库得到的参数是：
         *      user_id=3
         *      offset=10
         *      limit=10
         */
        Mockito.when(mockSession.selectList(Mockito.anyString(),Mockito.anyMap())).thenThrow(new RuntimeException());

        blogDao.getBlogs(2, 10, 3);
        HashMap<String, Object> expectedParma = new HashMap<>();
        expectedParma.put("user_id", 3);
        expectedParma.put("offset", 10);
        expectedParma.put("limit", 10);

        Mockito.verify(mockSession).selectList("selectBlog", expectedParma);
    }
}
