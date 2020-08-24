package qa.qdb.api.service;

import qa.qdb.api.client.model.Post;

public interface PostService {
    Post getPostByDocumentId(Long docId);

    Post savePost(Post post, Long docId);
}
