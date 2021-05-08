package io.github.mmpodkanski.computershop;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class DomainEventPublisher {
    private final ApplicationEventPublisher innerPublisher;

    public DomainEventPublisher(final ApplicationEventPublisher innerPublisher) {
        this.innerPublisher = innerPublisher;
    }

    public void publish(final DomainEvent event) {
        innerPublisher.publishEvent(event);
    }
}