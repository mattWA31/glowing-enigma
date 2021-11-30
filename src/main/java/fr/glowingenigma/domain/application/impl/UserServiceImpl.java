package fr.glowingenigma.domain.application.impl;

import fr.glowingenigma.domain.application.UserService;
import fr.glowingenigma.domain.application.commands.RegistrationCommand;
import fr.glowingenigma.domain.model.user.RegistrationException;
import fr.glowingenigma.domain.model.user.RegistrationManagement;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    //private RegistrationManagement registrationManagement;
    //private DomainEventPublisher domainEventPublisher;
    //private MailManager mailManager;

    //public UserServiceImpl(RegistrationManagement registrationManagement, DomainEventPublisher domainEventPublisher, MailManager mailManager) {
    //    this.registrationManagement = registrationManagement;
    //    this.domainEventPublisher = domainEventPublisher;
    //    this.mailManager = mailManager;
    //}

    @Override
    public void register(RegistrationCommand command) throws RegistrationException {

    }
}
