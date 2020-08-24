package qa.qdb.api.service;

import org.springframework.web.multipart.MultipartFile;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> getAllDocuments(User user);

    Optional<Document> findDocument(Long id);

    Document saveNewDocument(MultipartFile file, User user);

    void deleteDocument(Long id, User user);

    Document updateDocument(Document document);
}
