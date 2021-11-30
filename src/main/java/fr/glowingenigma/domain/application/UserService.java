package fr.glowingenigma.domain.application;

import fr.glowingenigma.domain.application.commands.RegistrationCommand;
import fr.glowingenigma.domain.model.user.RegistrationException;

public interface UserService {

    /**
     * Register a new user with username, email address, and password
     * @param command instance of <code>RegistrationCommand</code>
     * @throws RegistrationException when registration failed. Possible reasons are:
     *                               1) username already exists
     *                               2) Email address already exists
     */
    void register(RegistrationCommand command) throws RegistrationException;

}
