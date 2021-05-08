package io.github.mmpodkanski.computershop.product.vo;

import io.github.mmpodkanski.computershop.DomainEvent;
import io.github.mmpodkanski.computershop.product.enums.ECategory;

import java.time.Instant;
import java.util.Optional;

public class ProductEvent implements DomainEvent {
    public enum EState {
        ADDED, UPDATED, REMOVED
    }

    private final Instant occurredOn;
    private final ProductSourceId id;
    private final EState state;
    private final Data data;

    public ProductEvent(final ProductSourceId id, final EState state) {
        this(id, state, null);
    }

    public ProductEvent(final ProductSourceId id, final EState state, final Data data) {
        this.id = id;
        this.state = state;
        this.data = data;
        this.occurredOn = Instant.now();
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    public Optional<ProductSourceId> getSourceId() {
        return Optional.ofNullable(id);
    }

    public EState getState() {
        return state;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private final ECategory category;
        private final boolean inStock;
        private final double price;

        public Data(final ECategory category, final boolean inStock, final double price) {
            this.category = category;
            this.inStock = inStock;
            this.price = price;
        }

        public ECategory getCategory() {
            return category;
        }

        public boolean isInStock() {
            return inStock;
        }

        public double getPrice() {
            return price;
        }
    }
}
