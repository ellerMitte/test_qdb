package qa.qdb.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class DocumentRepositoryIT {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DocumentRepository documentRepository;
    private Document testDocument1;
    private Document testDocument2;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = User.builder()
                .username("test user1")
                .password("test pass")
                .build();
        testUser2 = User.builder()
                .username("test user2")
                .password("test pass")
                .build();
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);
        testDocument1 = Document.builder()
                .name("test doc1")
                .owner(testUser1)
                .build();
        testDocument2 = Document.builder()
                .name("test doc2")
                .owner(testUser2)
                .build();
        entityManager.persistAndFlush(testDocument1);
        entityManager.persistAndFlush(testDocument2);
    }

    @Test
    void whenFindAllByOwner_thenReturnDocumentList() {
        List<Document> expectedList = List.of(testDocument1);

        List<Document> actualList = documentRepository.findAllByOwner(testUser1);

        assertEquals(expectedList, actualList, "find all documents by user");
    }
}
