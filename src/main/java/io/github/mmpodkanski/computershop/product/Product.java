package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.product.enums.ECategory;
import io.github.mmpodkanski.computershop.product.enums.ECondition;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Name of product can not be blank!")
    private String name;
    @NotBlank(message = "Description can not be blank!")
    private String description;
    @NotBlank(message = "Please add product code!")
    private String code;
    @NotNull(message = "Please choose category!")
    @Enumerated(EnumType.STRING)
    private ECategory category;
    @NotNull(message = "Please type price of product!")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double price;
    @Enumerated(EnumType.STRING)
    private ECondition condition = ECondition.NEW;
    private int quantity;
    // TODO: in future - upload file
    private String imgLogoUrl;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    @PersistenceConstructor
    protected Product() {
    }

    public Product(
            final int id,
            final String name,
            final String description,
            final String code,
            final ECategory category,
            final double price,
            final ECondition condition,
            final int quantity,
            final String imgLogoUrl
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.category = category;
        this.price = price;
        this.condition = condition;
        this.quantity = quantity;
        this.imgLogoUrl = imgLogoUrl;
    }

    public void increaseStock() {
        quantity++;
    }

    public void decreaseStock() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    public ECategory getCategory() {
        return category;
    }

    void setCategory(ECategory category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    public ECondition getCondition() {
        return condition;
    }

    void setCondition(ECondition condition) {
        this.condition = condition;
    }

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(int stock) {
        this.quantity = stock;
    }

    public String getImgLogoUrl() {
        return imgLogoUrl;
    }

    void setImgLogoUrl(final String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }

    Calendar getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    Calendar getUpdatedAt() {
        return updatedAt;
    }

    void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update(
            final String name,
            final String description,
            final String code,
            final ECategory category,
            final double price,
            final ECondition condition,
            final int quantity,
            final String imgLogoUrl
    ) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.category = category;
        this.price = price;
        this.condition = condition;
        this.quantity = quantity;
        this.imgLogoUrl = imgLogoUrl;
    }
}