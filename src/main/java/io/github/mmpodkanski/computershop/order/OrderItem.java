package io.github.mmpodkanski.computershop.order;

import io.github.mmpodkanski.computershop.product.Product;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "items")
class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Product product;
    @NotNull
    private int quantity;
    @NotNull
    private BigDecimal price;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    @PersistenceConstructor
    protected OrderItem() {
    }

    OrderItem(final int quantity, final Product product, final BigDecimal price) {
        this.quantity = quantity;
        this.product = product;
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

    Product getProduct() {
        return product;
    }

    void setProduct(final Product product) {
        this.product = product;
    }

    BigDecimal getPrice() {
        return price;
    }

    void setPrice(final BigDecimal price) {
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
