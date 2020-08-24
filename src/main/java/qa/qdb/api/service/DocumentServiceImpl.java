package qa.qdb.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;
import qa.qdb.api.repository.DocumentRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.AccessControlException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    @Override
    public List<Document> getAllDocuments(User user) {
        return documentRepository.findAllByOwner(user);
    }

    @Override
    public Optional<Document> findDocument(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document saveNewDocument(MultipartFile file, User user) {
        Document document = getDocumentFromFile(file);
        document.setOwner(user);

        return documentRepository.save(document);
    }

    @Override
    public void deleteDocument(Long id, User user) {
        Document document = documentRepository.findById(id).orElseThrow();
        if (!user.equals(document.getOwner())) {
            throw new AccessControlException("attempt to access not own document");
        }
        documentRepository.deleteById(id);
    }

    @Override
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    private Document getDocumentFromFile(MultipartFile file) {
        Document document = new Document();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        document.setName(fileName);
        document.setType(file.getContentType());
        try {
            document.setFile(file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return document;
    }
}
