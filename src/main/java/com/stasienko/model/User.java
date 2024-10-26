package com.stasienko.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="users")
public class User implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    public User() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
