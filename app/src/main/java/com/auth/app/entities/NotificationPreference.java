package com.auth.app.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.DeliveryStatus;
import com.auth.app.entities.enums.NotificationFrequency;
import com.auth.app.entities.enums.NotificationPriority;
import com.auth.app.entities.enums.NotificationStatus;
import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
    name = "notification_preferences",
    indexes = {
        @Index(name = "idx_recipient_id", columnList = "recipient_id"),
        @Index(name = "idx_notification_type_id", columnList = "notification_type_id"),
        @Index(name = "idx_is_enabled", columnList = "isEnabled")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_preference", columnNames = {"recipient_id", "notification_type_id", "channel_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"recipient", "notificationType", "channel"})
public class NotificationPreference extends BaseEntity {
    @Builder.Default
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;
    
    @Builder.Default
    @Column(name = "delivery_time_start")
    private LocalTime deliveryTimeStart = LocalTime.of(8, 0);
    
    @Builder.Default
    @Column(name = "delivery_time_end")
    private LocalTime deliveryTimeEnd = LocalTime.of(22, 0);
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "frequency", nullable = false)
    private NotificationFrequency frequency = NotificationFrequency.IMMEDIATE;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @NotNull(message = "Recipient is required")
    private Recipient recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id", nullable = false)
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;
}
