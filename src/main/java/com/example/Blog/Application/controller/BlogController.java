package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.model.Blog;
import com.example.Blog.Application.model.BlogStatus;
import com.example.Blog.Application.repository.BlogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.apache.tomcat.jni.SSLConf.apply;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository)
    {
        this.blogRepository=blogRepository;
    }

    @GetMapping
    public List<Blog> getBlogs(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "date") String sort
    )
    {
        List<Blog> blogs;

        //Searching (MongoDb indexing) of query q on the title or on the content
        if(q !=null && !q.isBlank())
        {
            blogs = blogRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q,q)
                    .stream()
                    .filter(blog -> blog.status == BlogStatus.PUBLISHED)
                    .toList();
        }
        else if(category !=null && !category.isBlank())
        {
            blogs = blogRepository.findByCategoryIgnoreCaseAndStatus(category,BlogStatus.PUBLISHED);
        }
        else if(tag != null && !tag.isBlank())
        {
            blogs=blogRepository.findByTagsContainingAndStatus(tag, BlogStatus.PUBLISHED);
        }
        else{
            blogs=blogRepository.findByStatus(BlogStatus.PUBLISHED);
        }

        //This part is mainly meant for sorting the blogs based on the Popularity (Most views + Most likes)   Or    Default it takes the more recent one's (Current date)
        if("popular".equalsIgnoreCase(sort))
        {
            //We are converting blogs(which are in lists) to streams(streams are mainly used for sorting and filtering)
            //and after the operation they are converted back to lists using toList() function
            return blogs.stream()
                    .sorted(Comparator.comparing((Blog blog) -> blog.views + blog.likedBy.size()).reversed())
                    .toList();
        }

        //Based on the recent dates we are sorting
        return blogs.stream()
                .sorted(Comparator.comparing((Blog blog) -> blog.createdAt).reversed())
                .toList();
    }

    @GetMapping("/{id}")
    public Blog getBlog(@PathVariable String id, Authentication authentication)
    {
        Blog blog=blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        boolean isOwner = authentication != null && blog.authorId.equals(authentication.getName());
        if (blog.status != BlogStatus.PUBLISHED && !isOwner) {
            throw new RuntimeException("Blog not found");
        }

        if(blog.status == BlogStatus.PUBLISHED && !isOwner)
        {
            blog.views++;
            blogRepository.save(blog);
        }

        return blog;
    }

    @GetMapping("/mine")
    public List<Blog> myBlogs(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication required");
        }
        return blogRepository.findByAuthorId(authentication.getName());
    }

    @PostMapping
    public Blog createBlog(@RequestBody Dtos.BlogRequest request, Authentication authentication) {
        Blog blog = new Blog();
        blog.authorId = authentication.getName();
        apply(blog, request);
        return blogRepository.save(blog);
    }

    private void apply(Blog blog, Dtos.BlogRequest request) {
        blog.title = request.title();
        blog.content = request.content();
        blog.summary = request.summary();
        blog.category = request.category();
        blog.tags = request.tags();
        blog.imageUrl = request.imageUrl();
        blog.status = request.blogStatus() == null ? BlogStatus.DRAFT : request.blogStatus();
    }

    @PutMapping("/{id}")
    public Blog updateBlog(@PathVariable String id, @RequestBody Dtos.BlogRequest request, Authentication authentication)
    {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if(!blog.authorId.equals(authentication.getName()))
        {
            throw new RuntimeException("You can update only your own Blog");
        }

        apply(blog,request);
        blog.updatedAt = LocalDateTime.now();

        return blogRepository.save(blog);
    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable String id, Authentication authentication)
    {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if(!blog.authorId.equals(authentication.getName()))
        {
            throw new RuntimeException("You can delete only your own blog");
        }

        blogRepository.deleteById(id);
    }
}
