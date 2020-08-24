package qa.qdb.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;
import qa.qdb.api.repository.DocumentRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    private DocumentService documentService;
    private final byte[] bytes = new byte[1];
    private Document document;
    private User user;

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        documentService = new DocumentServiceImpl(documentRepository);
        user = User.builder()
                .id(1L)
                .username("user")
                .password("1")
                .email("user@mail.ru")
                .build();
        document = Document.builder()
                .id(1L)
                .name("test doc")
                .type("pdf")
                .file(bytes)
                .owner(user)
                .build();
    }

    @Test
    void getAllDocuments() {
        Mockito.when(documentRepository.findAllByOwner(user))
                .thenReturn(List.of(document));

        List<Document> expected = List.of(document);
        List<Document> actual = documentService.getAllDocuments(user);

        assertEquals(expected, actual, "get all documents");
    }

    @Test
    void getDocument() {
        Mockito.when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document));

        Optional<Document> expected = Optional.of(document);
        Optional<Document> actual = documentService.findDocument(1L);

        assertEquals(expected, actual, "get document by id");
    }

    @Test
    void whenGetBytesThrowIOException_thenThrowIOException() throws IOException {
        Mockito.when(file.getBytes())
                .thenThrow(UncheckedIOException.class);
        Mockito.when(file.getContentType())
                .thenReturn("pdf");
        Mockito.when(file.getOriginalFilename())
                .thenReturn("test doc");

        assertThrows(UncheckedIOException.class, () -> documentService.saveNewDocument(file, user), "file not saved");
    }

    @Test
    void whenDeleteDocument_thenCallDeleteRepository() {
        Mockito.when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document));
        Mockito.doAnswer(invocation -> {
            assertEquals(1L, (Long) invocation.getArgument(0));
            return null;
        }).when(documentRepository).deleteById(1L);

        documentService.deleteDocument(1L, user);
    }

    @Test
    void whenDeleteNotExistingDocument_thenThrowException() {
        Mockito.doThrow(NoSuchElementException.class)
                .when(documentRepository).findById(2L);

        assertThrows(NoSuchElementException.class, () -> documentService.deleteDocument(2L, user), "delete not existing document");
    }

    @Test
    void updateDocument() {
        Mockito.when(documentRepository.save(document))
                .thenReturn(document);

        assertEquals(document, documentService.updateDocument(document));
    }
}
