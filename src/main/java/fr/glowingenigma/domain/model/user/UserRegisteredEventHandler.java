package fr.glowingenigma.domain.model.user;

import fr.glowingenigma.domain.model.user.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler {

    private final static Logger log = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    @EventListener(UserRegisteredEvent.class)
    public void handleEvent(UserRegisteredEvent event) {
        log.debug("Handling `{}` registration event", event.getUser().getEmailAddress());
    }

}
