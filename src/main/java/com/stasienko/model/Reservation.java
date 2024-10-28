package com.stasienko.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.Date;

@Entity
@Table(name="reservation")
public class Reservation implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @Column(name="realization_date")
    private LocalDate realizationDate;

    @Column(name="is_realized")
    private Boolean isRealized;

    @Column(name="status")
    private Integer status;

    public Reservation() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public LocalDate getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(LocalDate realizationDate) {
        this.realizationDate = realizationDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getRealized() {
        return isRealized;
    }

    public void setRealized(Boolean realized) {
        isRealized = realized;
    }
}
