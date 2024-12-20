package com.stasienko.model;

import com.stasienko.service.UUIDConverter;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="pharmacy")
public class Pharmacy implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Column(name="address", nullable=false, columnDefinition="TEXT")
    private String address;

    @Column(name="phone_number", columnDefinition="TEXT")
    private String phoneNumber;

    @Column(name="name", nullable=false, columnDefinition="TEXT")
    private String name;

    @Column(name="email", columnDefinition="TEXT")
    private String email;

    @Column(name="latitude", columnDefinition="TEXT")
    private String latitude;

    @Column(name="longitude", columnDefinition="TEXT")
    private String longitude;

    @Column(name="opening_hours", columnDefinition="TEXT")
    private String openingHours;

    public Pharmacy() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setId(String id) {
        this.id = UUIDConverter.convertStringToUUID(id);
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
