package qa.qdb.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;
import qa.qdb.api.config.JphClientConfiguration;

import java.util.List;

@FeignClient(name = "jplaceholder",
        url = "https://jsonplaceholder.typicode.com/",
        configuration = JphClientConfiguration.class,
        fallback = JphHttpFallback.class)
public interface JphHttpClient {
    @GetMapping("/posts")
    List<Post> getPosts();

    @GetMapping(value = "/posts/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);

    @PostMapping(value = "/posts", produces = "application/json")
    Post savePost(@RequestBody Post post);

    @GetMapping(value = "/posts/{postId}/comments", produces = "application/json")
    List<Comment> getCommentsByPost(@PathVariable("postId") Long postId);

    @PostMapping(value = "/posts/{postId}/comments", produces = "application/json")
    Comment saveComment(@PathVariable("postId") Long postId, @RequestBody Comment comment);

}
