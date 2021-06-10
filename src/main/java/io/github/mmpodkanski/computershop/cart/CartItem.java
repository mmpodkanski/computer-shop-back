package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.customer.Customer;
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
    @OneToOne
    private Customer customer;
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

    CartItem(
            final Product product,
            final int quantity,
            final Customer customer
    ) {
        this.product = product;
        this.quantity = quantity;
        this.customer = customer;
    }

    void decreaseQuantity() {
        quantity--;
    }

    int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    Customer getCustomer() {
        return customer;
    }

    void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    Product getProduct() {
        return product;
    }

    void setProduct(final Product product) {
        this.product = product;
    }

    int getQuantity() {
        return quantity;
    }

    void setQuantity(final int quantity) {
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
