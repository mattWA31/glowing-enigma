package fr.glowingenigma.infrastructure.repository;

import fr.glowingenigma.domain.model.user.RegistrationException;
import fr.glowingenigma.domain.model.user.User;
import fr.glowingenigma.domain.model.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertNull;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class HibernateUserRepositoryTests {

    @TestConfiguration
    public static class UserRepositoryTestContextConfiguration {
        @Bean
        public UserRepository userRepository(EntityManager entityManager){
            return new HibernateUserRepository(entityManager);
        }
    }

    @Autowired
    private HibernateUserRepository repository;

    @Test
    public void  save_nullUsernameUser_shouldFail() {
        assertThrows(PersistenceException.class, () -> {
            User invalidUser = User.create(null, "blabla@glowingenigma.com", "MyPassword!");
            repository.save(invalidUser);
        });
    }

    @Test
    public void save_nullEmailAddressUser_shouldFail() {
        assertThrows(PersistenceException.class, () -> {
            User invalidUser = User.create("test", null, "MyPassword!");
            repository.save(invalidUser);
        });

    }

    @Test
    public void save_nullPasswordUser_shouldFail() {
        assertThrows(PersistenceException.class, () -> {
            User inavlidUser = User.create("test", "blabla@glowingenigma.com", null);
            repository.save(inavlidUser);
        });

    }

    @Test
    public void save_validUser_shouldSuccess() {
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        assertNotNull("New user's id should be generated", newUser.getId());
        assertNotNull("New user's created date should be generated", newUser.getCreatedDate());
        assertEquals(username, newUser.getUsername());
        assertEquals(emailAddress, newUser.getEmailAddress());
        assertEquals("", newUser.getFirstName());
        assertEquals("", newUser.getLastName());
    }

    @Test
    public void save_usernameAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        User alreadyExist = User.create(username, emailAddress, "MyPassword!");
        repository.save(alreadyExist);

        try {
            User newUser = User.create(username, "new@glowingenigma.com", "MyPassword!");
            repository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void save_emailAddressAlreadyExist_shouldFail() {
        // Create already exist user
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        User alreadyExist = User.create(username, emailAddress, "MyPassword!");
        repository.save(alreadyExist);

        try {
            User newUser = User.create("new", emailAddress, "MyPassword!");
            repository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
        String emailAddress = "blabla@glowingenigma.com";
        User user = repository.findByEmailAddress(emailAddress);
        assertNull("No user should by found", user);
    }

    @Test
    public void findByEmailAddress_exist_shouldReturnResult() {
        String emailAddress = "blabla@glowingenigma.com";
        String username = "test";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        User found = repository.findByEmailAddress(emailAddress);
        assertEquals("test", username, found.getUsername());
    }

    @Test
    public void findByUsername_notExist_shouldReturnEmptyResult() {
        String username = "test";
        User user = repository.findByUsername(username);
        assertNull("No user should by found", user);
    }

    @Test
    public void findByUsername_exist_shouldReturnResult() {
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        repository.save(newUser);
        User found = repository.findByUsername(username);
        assertEquals("blabla@glowingenigma.com", emailAddress, found.getEmailAddress());
    }

}
