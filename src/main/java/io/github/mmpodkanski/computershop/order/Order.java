package io.github.mmpodkanski.computershop.order;

import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.order.enums.EOrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal totalCost;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderItem> items;
    @ManyToOne
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private EOrderStatus status;
    private String paymentId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    @PersistenceConstructor
    protected Order() {
    }

    Order(
            final BigDecimal totalCost,
            final Set<OrderItem> items,
            final Customer customer,
            final EOrderStatus status
    ) {
        this.totalCost = totalCost;
        this.items = items;
        this.customer = customer;
        this.status = status;
    }

    void cancelOrder() {
        this.status = EOrderStatus.CANCELLED;
    }

    int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    BigDecimal getTotalCost() {
        return totalCost;
    }

    void setTotalCost(final BigDecimal totalCost) {
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

    EOrderStatus getStatus() {
        return status;
    }

    void setStatus(final EOrderStatus status) {
        this.status = status;
    }

    String getPaymentId() {
        return paymentId;
    }

    void setPaymentId(final String paymentId) {
        this.paymentId = paymentId;
    }
}
