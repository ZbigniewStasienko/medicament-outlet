package com.stasienko.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="\"User\"")
public class User implements java.io.Serializable {
    @Id
    @Column(updatable = false, nullable = false, columnDefinition="TEXT")
    private String id;

    @Column(name="name", nullable=false, columnDefinition="TEXT")
    private String name;

    @Column(name="surname", nullable=false, columnDefinition="TEXT")
    private String surname;

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
