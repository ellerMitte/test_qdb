package qa.qdb.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import qa.qdb.api.client.JphHttpClient;
import qa.qdb.api.client.model.Post;
import qa.qdb.api.model.Document;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final DocumentService documentService;
    private final JphHttpClient jphHttpClient;

    @Override
    public Post getPostByDocumentId(Long docId) {
        Document document = documentService.findDocument(docId).orElseThrow();
        return jphHttpClient.getPostById(document.getPostId());
    }

    @Override
    public Post savePost(Post post, Long docId) {
        Post savedPost = jphHttpClient.savePost(post);
        if (savedPost != null) {
            Document document = documentService.findDocument(docId).orElseThrow();
            document.setPostId(savedPost.getId());
            documentService.updateDocument(document);
        }
        return savedPost;
    }
}
