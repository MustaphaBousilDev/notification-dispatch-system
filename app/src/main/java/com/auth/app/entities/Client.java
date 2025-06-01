package com.auth.app.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "clients")
public class Client {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Getter
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Setter
    @Getter
    @Column(name="last_name", nullable = false)
    private String lastName;

    @Setter
    @Getter
    @Column(unique = true, nullable=false)
    private String email;

    @Setter
    @Getter
    @Column(name="phone_number")
    private String phoneNumber;

    @Setter
    @Getter
    @Column(name="preferred_language", nullable = false)
    private String preferredLanguage;

    @Setter
    @Getter
    @Column(name="app_token")
    private String appToken;

    @Setter
    @Getter
    private boolean active= true;

    @Setter
    @Getter
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Setter
    @Getter
    @Column(name="updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}