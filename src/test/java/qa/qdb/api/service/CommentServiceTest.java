package qa.qdb.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import qa.qdb.api.client.JphHttpClient;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private JphHttpClient jphHttpClient;
    @Mock
    private PostService postService;
    private CommentService commentService;
    private Comment comment;
    private Post post;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(jphHttpClient, postService);
        post = Post.builder()
                .id(1L)
                .title("test post")
                .body("test")
                .build();
        comment = Comment.builder()
                .id(1L)
                .name("test comment")
                .body("test")
                .postId(2L)
                .build();
        Mockito.when(postService.getPostByDocumentId(1L))
                .thenReturn(post);
    }

    @Test
    void saveComment() {
        Mockito.when(jphHttpClient.saveComment(post.getId(), comment))
                .thenReturn(comment);

        assertEquals(comment, commentService.saveComment(comment, 1L), "save Comment");
    }

    @Test
    void getCommentsByDocId() {
        Mockito.when(jphHttpClient.getCommentsByPost(post.getId()))
                .thenReturn(List.of(comment));

        assertEquals(List.of(comment), commentService.getCommentsByDocId(1L), "get Comments by Document id");
    }
}
