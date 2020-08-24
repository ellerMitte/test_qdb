package qa.qdb.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import qa.qdb.api.client.JphHttpClient;
import qa.qdb.api.client.model.Post;
import qa.qdb.api.model.Document;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private DocumentService documentService;
    @Mock
    private JphHttpClient jphHttpClient;
    private PostService postService;
    private Document document;
    private Post post;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(documentService, jphHttpClient);
        document = Document.builder()
                .id(1L)
                .name("test doc")
                .type("pdf")
                .postId(1L)
                .build();
        post = Post.builder()
                .id(1L)
                .title("test post")
                .body("test")
                .build();
        Mockito.when(documentService.findDocument(document.getId()))
                .thenReturn(Optional.of(document));
    }

    @Test
    void getPostByDocumentId() {
        Mockito.when(jphHttpClient.getPostById(1L))
                .thenReturn(post);

        assertEquals(post, postService.getPostByDocumentId(document.getId()));
    }

    @Test
    void savePost() {
        Mockito.when(documentService.updateDocument(document))
                .thenReturn(document);
        Mockito.when(jphHttpClient.savePost(post))
                .thenReturn(post);

        assertEquals(post, postService.savePost(post, document.getId()));
    }
}
