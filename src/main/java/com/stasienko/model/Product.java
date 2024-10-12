package com.stasienko.model;

import jakarta.persistence.*;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name="product")
public class Product implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @Column(name="expiration_date")
    private LocalDate expirationDate;

    @Column(name="is_reserved")
    private Boolean isReserved;

    @Column(name="base_price")
    private Double basePrice;

    @Column(name="price")
    private Double price;

    public Product() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean reserved) {
        isReserved = reserved;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
