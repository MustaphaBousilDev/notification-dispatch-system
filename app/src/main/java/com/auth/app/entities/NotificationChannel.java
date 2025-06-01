package com.auth.app.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name="notification_channels",
    indexes = {
        @Index(name="idx_channel_code", columnList ="channelCode"),
        @Index(name="idx_is_active", columnList = "isActive")

    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "notificationTypes")
public class NotificationChannel extends BaseEntity {
    
    @NotBlank(message = "Channel name is required")
    @Size(max = 50, message = "Channel name cannot exceed 50 characters")
    @Column(name = "channel_name", nullable = false, unique = true, length = 50)
    private String channelName;
    
    @NotBlank(message = "Channel code is required")
    @Size(max = 20, message = "Channel code cannot exceed 20 characters")
    @Column(name = "channel_code", nullable = false, unique = true, length = 20)
    private String channelCode;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    // Relationships
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificationType> notificationTypes = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificationPreference> preferences = new ArrayList<>();
    
}