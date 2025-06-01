package com.auth.app.entities;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;
import org.apache.kafka.common.protocol.types.Field.Bool;

import com.auth.app.entities.enums.NotificationPriority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name="notification_types",
    indexes = {
        @Index(name="idx_type_code", columnList = "typeCode"),
        @Index(name="idx_priority", columnList = "priorityLevel"),
        @Index(name="idx_channel_id", columnList = "channel_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"channel", "templates", "notifications"})
public class NotificationType extends BaseEntity {
    @NotBlank(message="TypeName is required")
    @Size(max=100, message="Type name cannot exceed 100 characters")
    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName;

    @NotBlank(message="TypeCode is required")
    @Size(max=50, message="TypeCode cannot exceed 50 characters")
    @Column(name="type_code", nullable = false, unique=true, length = 50)
    private String typeCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name="priority_level", nullable = false)
    private NotificationPriority priorityLevel = NotificationPriority.MEDIUM;

    @Builder.Default
    @Column(name="is_active", nullable = false)
    private Boolean isActive = true;

    //RelationShip 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="channel_id", nullable = false)
    @NotBlank(message="Channel is required")
    private NotificationChannel channel;

    @OneToMany(mappedBy = "notificationType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificationTemplate> templates = new ArrayList<>();

    @OneToMany(mappedBy = "notificationType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();
}
