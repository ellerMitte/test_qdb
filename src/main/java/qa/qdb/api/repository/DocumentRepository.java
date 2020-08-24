package qa.qdb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qa.qdb.api.model.Document;
import qa.qdb.api.model.User;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByOwner(User owner);
}
