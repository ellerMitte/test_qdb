package qa.qdb.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qa.qdb.api.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryIT {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindByUsername_thenReturnOptionalUser() {
        User testUser = User.builder()
                .username("test user")
                .password("test password")
                .email("test email")
                .build();
        entityManager.persistAndFlush(testUser);

        User foundUser = userRepository.findByUsername("test user").orElseThrow();

        assertEquals(testUser, foundUser, "find user by username");
    }
}
