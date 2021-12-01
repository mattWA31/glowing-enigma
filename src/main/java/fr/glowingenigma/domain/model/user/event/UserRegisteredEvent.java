package fr.glowingenigma.domain.model.user.event;

import fr.glowingenigma.domain.common.event.DomainEvent;
import fr.glowingenigma.domain.model.user.User;
import org.springframework.util.Assert;

import java.util.Objects;

public class UserRegisteredEvent extends DomainEvent {

    private final User user;

    public UserRegisteredEvent(User user) {
        super(user);
        Assert.notNull(user, "Parameter `user` must not be null");
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegisteredEvent that = (UserRegisteredEvent) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "user='" + user + '\'' +
                "timestamp='" + getTimestamp() + '\'' +
                '}';
    }
}
