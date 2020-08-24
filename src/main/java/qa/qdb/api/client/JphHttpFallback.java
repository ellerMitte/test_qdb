package qa.qdb.api.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JphHttpFallback implements JphHttpClient {
    @Override
    public List<Post> getPosts() {
        return Collections.emptyList();
    }

    @Override
    public Post getPostById(Long postId) {
        return null;
    }

    @Override
    public Post savePost(Post post) {
        log.info("post not saved: " + post);
        return null;
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return Collections.emptyList();
    }

    @Override
    public Comment saveComment(Long postId, Comment comment) {
        return null;
    }
}
