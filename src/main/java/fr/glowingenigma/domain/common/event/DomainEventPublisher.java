package fr.glowingenigma.domain.common.event;

public interface DomainEventPublisher {

    void publish(DomainEvent event);

}
