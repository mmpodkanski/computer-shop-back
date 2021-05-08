package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.product.Product;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "cart")
class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customerId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    @PersistenceConstructor
    protected CartItem() {
    }

    public CartItem(
            final Product product,
            final int quantity,
            final int customerId
    ) {
        this.product = product;
        this.quantity = quantity;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public Product getProduct() {
        return product;
    }

    void setProduct(final Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
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
