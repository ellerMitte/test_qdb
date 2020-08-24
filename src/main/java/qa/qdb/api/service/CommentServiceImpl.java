package qa.qdb.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import qa.qdb.api.client.JphHttpClient;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final JphHttpClient jphHttpClient;
    private final PostService postService;

    @Override
    public Comment saveComment(Comment comment, Long docId) {
        log.info("save Comment: " + comment + " for Document id: " + docId);
        return jphHttpClient.saveComment(getPostByDocumentId(docId).orElseThrow().getId(), comment);
    }

    @Override
    public List<Comment> getCommentsByDocId(Long docId) {
        log.info("get Comment by Document id: " + docId);
        return jphHttpClient.getCommentsByPost(getPostByDocumentId(docId).orElseThrow().getId());
    }

    private Optional<Post> getPostByDocumentId(Long docId) {
        log.info("get Post by Document id: " + docId);
        return Optional.ofNullable(postService.getPostByDocumentId(docId));
    }
}
