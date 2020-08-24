package qa.qdb.api.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import qa.qdb.api.client.model.Comment;
import qa.qdb.api.client.model.Post;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;
import qa.qdb.api.repository.DocumentRepository;
import qa.qdb.api.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    private Document testDocument;
    private User testUser;
    private Post testPost;
    private Comment testComment;
    private User testUser2;

    @BeforeAll
    void setUp() {
        testUser = User.builder()
                .username("test1")
                .password("$2a$12$hDOBbp4ZhgWoJiel57ldP.GgOGX/eWJB9N3YUV81O4P.hIqLLBbbS")
                .build();
        testUser2 = User.builder()
                .username("test2")
                .password("$2a$12$hDOBbp4ZhgWoJiel57ldP.GgOGX/eWJB9N3YUV81O4P.hIqLLBbbS")
                .build();
        testDocument = Document.builder()
                .name("test doc 1")
                .type(MediaType.APPLICATION_PDF_VALUE)
                .file(new byte[1])
                .owner(testUser)
                .build();
        Document testDocument2 = Document.builder()
                .name("test doc 2")
                .type(MediaType.APPLICATION_PDF.getType())
                .file(new byte[1])
                .owner(testUser2)
                .build();
        userRepository.save(testUser);
        userRepository.save(testUser2);
        testDocument = documentRepository.save(testDocument);
        documentRepository.save(testDocument2);
    }

    @Test
    void whenGetDocuments_thenReturnOk() {
        ResponseEntity<List> result = restTemplate.withBasicAuth("test1", "pass")
                .getForEntity("/api/v1/documents", List.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void whenGetDocuments_thenReturnOnlyOwnDocuments() {
        ResponseEntity<List> result = restTemplate.withBasicAuth("test1", "pass")
                .getForEntity("/api/v1/documents", List.class);

        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void whenGetDocument_thenReturnDocumentFile() {

        ResponseEntity<byte[]> result = restTemplate.withBasicAuth("test1", "pass")
                .getForEntity("/api/v1/documents/" + testDocument.getId(), byte[].class);

        assertEquals(testDocument.getFile().length, Objects.requireNonNull(result.getBody()).length);
    }

    @Test
    void whenAddFile_thenReturnSavedDocument() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.pdf"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<Document> result = restTemplate.withBasicAuth("test2", "pass")
                .postForEntity("/api/v1/documents", request, Document.class);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("test.pdf", Objects.requireNonNull(result.getBody()).getName());
        assertEquals(testUser2, result.getBody().getOwner());
    }

    @Test
    void whenDeletedDocument_thenDeleted() {
        Document testDocument3 = Document.builder()
                .name("test doc 3")
                .type(MediaType.APPLICATION_PDF.getType())
                .file(new byte[1])
                .owner(testUser)
                .build();
        testDocument3 = documentRepository.save(testDocument3);

        restTemplate.withBasicAuth("test1", "pass")
                .delete("/api/v1/documents/" + testDocument3.getId());

        assertNull(documentRepository.findById(testDocument3.getId()).orElse(null));
    }

}
