package io.github.mmpodkanski.computershop.order;

import io.github.mmpodkanski.computershop.customer.Customer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double totalCost;
    @OneToMany
    private Set<OrderItem> items;
    @ManyToOne
    private Customer customer;

    @PersistenceConstructor
    protected Order() {
    }

    public Order(
            final double totalCost,
            final Set<OrderItem> items,
            final Customer customer,
            final Calendar createdAt,
            final Calendar updatedAt
    ) {
        this.totalCost = totalCost;
        this.items = items;
        this.customer = customer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    double getTotalCost() {
        return totalCost;
    }

    void setTotalCost(final double totalCost) {
        this.totalCost = totalCost;
    }

    Set<OrderItem> getItems() {
        return items;
    }

    void setItems(final Set<OrderItem> items) {
        this.items = items;
    }

    Customer getCustomer() {
        return customer;
    }

    void setCustomer(final Customer customer) {
        this.customer = customer;
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
