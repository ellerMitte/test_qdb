package qa.qdb.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;
import qa.qdb.api.security.ApiUserDetailService;
import qa.qdb.api.security.ApiUserPrincipal;
import qa.qdb.api.service.CommentService;
import qa.qdb.api.service.DocumentService;
import qa.qdb.api.service.PostService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final PostService postService;
    private final CommentService commentService;
    private final ApiUserDetailService userDetailService;

    @GetMapping("")
    public ResponseEntity<List<Document>> getDocuments(Authentication authentication) {
        log.info("get all documents: " + authentication.getName());
        return ResponseEntity.ok(documentService.getAllDocuments(getUser(authentication)));
    }

    @GetMapping("/{docId}")
    public ResponseEntity<byte[]> getDocument(@PathVariable("docId") Long docId) {
        log.info("get Document by id: " + docId);
        Document document = documentService.findDocument(docId).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(document.getFile());
    }

    @PostMapping("")
    public ResponseEntity<Document> saveDocument(@RequestParam("file") MultipartFile file, Authentication authentication) {
        log.info("upload file: " + file.getOriginalFilename());

        Document document = documentService.saveNewDocument(file, getUser(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/{docId}/posts")
    public ResponseEntity<Post> getPostByDocument(@PathVariable("docId") Long docId) {
        log.info("get Post by Document id: " + docId);

        return ResponseEntity.ok(postService.getPostByDocumentId(docId));
    }

    @PostMapping("/{docId}/posts")
    public ResponseEntity<Post> savePost(@RequestBody Post post, @PathVariable("docId") Long docId) {
        log.info("save Post associated with Document: " + docId);

        Post savedPost = postService.savePost(post, docId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @GetMapping("/{docId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByDocument(@PathVariable("docId") Long docId) {
        log.info("get all Comments by Document id: " + docId);

        return ResponseEntity.ok(commentService.getCommentsByDocId(docId));
    }

    @PostMapping("/{docId}/comments")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment, @PathVariable("docId") Long docId) {
        log.info("save Comment associated with Document: " + docId);

        Comment savedComment = commentService.saveComment(comment, docId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @DeleteMapping("/{docId}")
    public ResponseEntity<String> deleteDocument(@PathVariable("docId") Long docId, Authentication authentication) {
        log.info("delete Document: " + docId);

        documentService.deleteDocument(docId, getUser(authentication));
        return ResponseEntity.ok("deleted");
    }

    private User getUser(Authentication authentication) {
        ApiUserPrincipal principal = userDetailService.getApiPrincipal(authentication);
        return principal.getUser();
    }
}
