package qa.qdb.api.service;

import qa.qdb.api.client.model.Comment;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment, Long docId);

    List<Comment> getCommentsByDocId(Long docId);
}
