package io.github.mmpodkanski.computershop;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccurredOn();
}
