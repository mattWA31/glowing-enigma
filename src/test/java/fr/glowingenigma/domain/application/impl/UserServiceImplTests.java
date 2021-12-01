package fr.glowingenigma.domain.application.impl;

import fr.glowingenigma.domain.application.commands.RegistrationCommand;
import fr.glowingenigma.domain.common.event.DomainEventPublisher;
import fr.glowingenigma.domain.common.mail.MailManager;
import fr.glowingenigma.domain.common.mail.MessageVariable;
import fr.glowingenigma.domain.model.user.*;
import fr.glowingenigma.domain.model.user.event.UserRegisteredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    private RegistrationManagement registrationManagementMock;
    private DomainEventPublisher domainEventPublisherMock;
    private MailManager mailManagerMock;
    private UserServiceImpl instance;

    @BeforeEach
    public void setUp(){
        registrationManagementMock = mock(RegistrationManagement.class);
        domainEventPublisherMock = mock(DomainEventPublisher.class);
        mailManagerMock = mock(MailManager.class);
        instance = new UserServiceImpl(registrationManagementMock, domainEventPublisherMock, mailManagerMock);
    }

    @Test
    public void register_nullCommand_shouldFail() throws RegistrationException {
        assertThrows(IllegalArgumentException.class, () -> {
            instance.register(null);
        });
    }

    @Test
    public void register_existingUsername_shouldFail() throws RegistrationException {
        assertThrows(RegistrationException.class, () -> {
            String username = "existing";
            String emailAddress = "blabla@glowingenigma.com";
            String password = "MyPassword!";
            doThrow(UsernameExistsException.class).when(registrationManagementMock)
                    .register(username, emailAddress, password);

            RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
            instance.register(command);
        });
    }

    @Test
    public void register_existingEmailAddress_shouldFail() throws RegistrationException {
        assertThrows(RegistrationException.class, () -> {
            String username = "test";
            String emailAddress = "blabla@glowingenigma.com";
            String password = "MyPassword!";
            doThrow(EmailAddressExistsException.class).when(registrationManagementMock)
                    .register(username, emailAddress, password);

            RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
            instance.register(command);
        });
    }

    @Test
    public void register_validCommand_shouldSucceed() throws RegistrationException {
        String username = "test";
        String emailAddress = "blabla@glowingenigma.com";
        String password = "MyPassword!";
        User newUser = User.create(username, emailAddress, password);
        when(registrationManagementMock.register(username, emailAddress, password))
                .thenReturn(newUser);
        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

        instance.register(command);

        verify(mailManagerMock).send(
                emailAddress,
                "Welcome to GlowingEnigma",
                "welcome.ftl",
                MessageVariable.from("user", newUser)
        );
        verify(domainEventPublisherMock).publish(new UserRegisteredEvent(newUser));
    }

}
