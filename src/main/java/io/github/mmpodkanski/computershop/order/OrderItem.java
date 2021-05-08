package io.github.mmpodkanski.computershop.order;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int quantity;
    @NotNull
    private double price;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    @PersistenceConstructor
    protected OrderItem() {
    }

    public OrderItem(final int quantity, final double price) {
        this.quantity = quantity;
        this.price = price;
    }

    int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    int getQuantity() {
        return quantity;
    }

    void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    double getPrice() {
        return price;
    }

    void setPrice(final double price) {
        this.price = price;
    }

    Calendar getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(final Calendar createdAt) {
        this.createdAt = createdAt;
    }

    Calendar getUpdatedAt() {
        return updatedAt;
    }

    void setUpdatedAt(final Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }


}
