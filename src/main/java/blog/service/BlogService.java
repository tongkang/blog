package blog.service;

import blog.dao.BlogDao;
import blog.entity.Blog;
import blog.entity.BlogListResult;
import blog.entity.BlogResult;
import blog.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BlogService {
    private BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);
            int count = blogDao.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogById(int blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.selectBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult inserBlog(Blog newBlog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(newBlog));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlog(int blogId, Blog targetBlog) {
        Blog blogInDb = blogDao.selectBlogById(blogId);

        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(blogId, blogInDb.getId())) {
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            targetBlog.setId(blogId);
            return BlogResult.success("修改成功", blogDao.updateBlog(targetBlog));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlog(int blogId, User user) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(user.getId(), blogInDb.getUserId())) {
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            blogDao.deleteBlog(blogId);
            return BlogResult.success("修改成功");
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }
}
