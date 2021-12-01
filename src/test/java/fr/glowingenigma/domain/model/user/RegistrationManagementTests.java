package fr.glowingenigma.domain.model.user;


import fr.glowingenigma.domain.common.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegistrationManagementTests {

    private UserRepository repositoryMock;
    private PasswordEncryptor passwordEncryptorMock;
    private RegistrationManagement instance;

    @BeforeEach
    public void setUp(){
        repositoryMock = mock(UserRepository.class);
        passwordEncryptorMock = mock(PasswordEncryptor.class);
        instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
    }

    @Test
    public void register_existingUsername_shouldFail() throws RegistrationException {
        assertThrows(UsernameExistsException.class, () -> {
            String username = "exist";
            String emailAddress = "blabla@glowingenigma.com";
            String password = "MyPassword!";
            // We just return an empty user object to indicate an existing user
            when(repositoryMock.findByUsername(username)).thenReturn(new User());
            instance.register(username, emailAddress, password);
        });
    }

    @Test
    public void register_existingEmailAddress_shouldFail() throws RegistrationException {
        assertThrows(EmailAddressExistsException.class, () -> {
            String username = "test";
            String emailAddress = "exist@glowingenigma.com";
            String password = "MyPassword!";
            // We just return an empty user object to indicate an existing user
            when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(new User());
            instance.register(username, emailAddress, password);
        });
    }

    @Test
    public void register_uppercaseEmailAddress_shouldSucceedAndBecomeLowercase() throws RegistrationException {
        String username = "test";
        String emailAddress = "Blabla@GlowingEnigma.com";
        String password = "MyPassword!";
        instance.register(username, emailAddress, password);
        User userToSave = User.create(username, emailAddress.toLowerCase(), password);
        verify(repositoryMock).save(userToSave);
    }

    @Test
    public void register_newUser_shouldSucceed() throws RegistrationException {
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        String password = "MyPassword!";
        String encryptedPassword = "EncryptedPassword";
        User newUser = User.create(username, emailAddress, encryptedPassword);

        // Setup repository mock
        // Return null to indicate no user exists
        when(repositoryMock.findByUsername(username)).thenReturn(null);
        when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(null);
        doNothing().when(repositoryMock).save(newUser);
        // Setup passwordEncryptor mock
        when(passwordEncryptorMock.encrypt(password)).thenReturn("EncryptedPassword");

        User savedUser = instance.register(username, emailAddress, password);
        InOrder inOrder = inOrder(repositoryMock);
        inOrder.verify(repositoryMock).findByUsername(username);
        inOrder.verify(repositoryMock).findByEmailAddress(emailAddress);
        inOrder.verify(repositoryMock).save(newUser);
        verify(passwordEncryptorMock).encrypt(password);
        assertEquals("EncryptedPassword", encryptedPassword, savedUser.getPassword());
    }

}
