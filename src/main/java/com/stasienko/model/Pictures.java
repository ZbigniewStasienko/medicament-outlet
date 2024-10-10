package com.stasienko.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name="pictures")
public class Pictures implements java.io.Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name="picture", nullable=false, columnDefinition="TEXT")
    private String picture;

    public Pictures() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
